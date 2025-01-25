package MicroservicePago.service;

import MicroservicePago.entity.Order;
import MicroservicePago.entity.OrderPayment;
import MicroservicePago.enums.OrderStatus;
import MicroservicePago.event.producer.ReservationEventProducer;
import MicroservicePago.exception.PaymentProcessingException;
import MicroservicePago.exception.ResourceNotFoundException;
import MicroservicePago.repository.OrderRepository;
import MicroservicePago.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static MicroservicePago.constants.GeneralConstants.FIELD_ID;
import static MicroservicePago.constants.PaymentConstants.*;

@RequiredArgsConstructor
@Service
public class PaymentService {


    private final MercadoPagoService mercadoPagoService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationEventProducer reservationEventProducer;

    @Transactional
    public void processPaymentNotification(String payload) throws PaymentProcessingException {
        try {
            JsonNode notificationData = parsePayload(payload);
            System.out.println("notiData: " + notificationData);
            String action = getActionFromNotification(notificationData);
            String paymentId = getPaymentIdFromNotification(notificationData);
            System.out.println("paymentId: " + paymentId);
            JsonNode paymentData = mercadoPagoService.getPaymentDetails(paymentId);

            switch (action) {
                case PAYMENT_CREATED -> handlePaymentCreated(paymentData);
                case PAYMENT_CANCELED -> handlePaymentCanceled(paymentId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing payment notification: " + e);
        }
    }

    private void handlePaymentCreated(JsonNode paymentData) throws PaymentProcessingException {
        String status = paymentData.get("status").asText();

        if (STATUS_APPROVED.equals(status)) {
            confirmPayment(paymentData);
        } else if (STATUS_PENDING.equals(status)) {
            String paymentMethodId = paymentData.get(PAYMENT_METHOD_ID).asText();
            if ("pagoefectivo_atm".equals(paymentMethodId)) {
                increaseReservationTime(paymentData);
            }
        }
    }

    private JsonNode parsePayload(String payload) throws PaymentProcessingException {
        try {
            return new ObjectMapper().readTree(payload);
        } catch (Exception e) {
            throw new PaymentProcessingException("Failed to parse payload: " + e);
        }
    }

    private String getActionFromNotification(JsonNode notificationData) {
        return notificationData.path("action").asText(null);
    }

    private String getPaymentIdFromNotification(JsonNode notificationData) {
        return notificationData.path("data").path(PAYMENT_ID).asText(null);
    }

    private void confirmPayment(JsonNode paymentData) throws PaymentProcessingException {
        if (STATUS_APPROVED.equals(paymentData.get("status").asText())) {
            Order order = getOrderFromPaymentData(paymentData);
            if (OrderStatus.PENDING_PAYMENT.equals(order.getStatus())) {
                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
                createOrderPayment(paymentData, order);
                reservationEventProducer.confirmReservation(order.getId());
            }
        }
    }

    public Long extractOrderIdFromPaymentData(JsonNode paymentData) {
        return paymentData.get(METADATA).get("order_id").asLong();
    }

    private void handlePaymentCanceled(String paymentId) {
        JsonNode paymentData = mercadoPagoService.getPaymentDetails(paymentId);
        if (paymentData != null && paymentData.has(METADATA)) {
            Order order = getOrderFromPaymentData(paymentData);
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            reservationEventProducer.cancelReservation(order.getId());
        }
    }


    private void increaseReservationTime(JsonNode paymentData) {
        Long orderId = extractOrderIdFromPaymentData(paymentData);
        reservationEventProducer.extendReservation(orderId);
    }

    private Order getOrderFromPaymentData(JsonNode paymentData) {
        Long orderId = extractOrderIdFromPaymentData(paymentData);
        return getOrderById(orderId);
    }

    private void createOrderPayment(JsonNode paymentData, Order order) {
        OrderPayment orderPayment = OrderPayment.builder()
                .order(order)
                .paymentId(paymentData.get(PAYMENT_ID).asText())
                .paymentMethod(paymentData.get(PAYMENT_METHOD_ID).asText())
                .datePaid(LocalDateTime.parse(paymentData.get("date_approved").asText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .amount(new BigDecimal(paymentData.get("transaction_details").get("total_paid_amount").asText()))
                .build();

        paymentRepository.save(orderPayment);
    }

    private Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class.getName(), FIELD_ID, id));
    }
}
