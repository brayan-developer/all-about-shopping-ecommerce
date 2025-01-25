package MicroservicePago.service.impl;

import MicroservicePago.dto.CartItemDTO;
import MicroservicePago.dto.OrderDTO;
import MicroservicePago.dto.PaginatedResponseDTO;
import MicroservicePago.entity.Order;
import MicroservicePago.entity.OrderItem;
import MicroservicePago.entity.ShippingAddress;
import MicroservicePago.enums.OrderStatus;
import MicroservicePago.event.producer.ReservationEventProducer;
import MicroservicePago.exception.PaymentProcessingException;
import MicroservicePago.exception.ResourceNotFoundException;
import MicroservicePago.exception.UnauthorizedAccessException;
import MicroservicePago.mapper.OrderMapper;
import MicroservicePago.payload.ApiResponse;
import MicroservicePago.repository.OrderRepository;
import MicroservicePago.repository.ShippingAddressRepository;
import MicroservicePago.request.OrderRequest;
import MicroservicePago.service.IOrderService;
import MicroservicePago.service.MercadoPagoService;
import MicroservicePago.service.OrderValidatorService;
import MicroservicePago.service.PaymentService;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

import static MicroservicePago.constants.ErrorMessageConstants.UNAUTHORIZED_ACCESS;
import static MicroservicePago.constants.GeneralConstants.*;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final MercadoPagoService mercadoPagoService;
    private final OrderValidatorService orderValidatorService;
    private final OrderMapper orderMapper;
    private final ReservationEventProducer reservationEventProducer;
    private final PaymentService paymentService;


    @Override
    public OrderDTO getOrderById(Long id, Long userId) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Order.class.getSimpleName(), FIELD_ID , id));

        if (!Objects.equals(order.getUserId(), userId)){
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }
        return orderMapper.toDto(order);
    }

    @Override
    public PaginatedResponseDTO<OrderDTO> getOrdersByUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<Order> orders = orderRepository.findByUserIdAndStatus(userId, OrderStatus.PAID, pageable);
        return new PaginatedResponseDTO<>(orders.map(orderMapper::toDto));
    }

    @Transactional
    @Override
    public ResponseEntity<ApiResponse> createOrder(OrderRequest orderRequest, Long userId, String token) {
        try {
            ApiResponse response = orderValidatorService.validateStock(userId);

            if(!response.isSuccess()){
                return new ResponseEntity<>(ApiResponse.error(response.getMessage(), response.getData()),
                        HttpStatus.CONFLICT);
            }

            List<CartItemDTO> cartItems = (List<CartItemDTO>) response.getData();

            Order order = saveOrder( userId, cartItems,orderRequest);

            List<PreferenceItemRequest> items = mercadoPagoService.createPreferenceItems(cartItems);

            BigDecimal shippingCost = getShippingPrice(order.getShippingAddress());
            Preference preference = mercadoPagoService.createPaymentPreference(order.getId(), items, shippingCost);

            reservationEventProducer.createReservation(order.getId(), token);

            return new ResponseEntity<>(ApiResponse.success(preference.getId()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Transactional
    @Override
    public void handlePaymentNotification(String payload) {
        try {
            paymentService.processPaymentNotification(payload);
        } catch (Exception e) {
            throw new PaymentProcessingException(e.getMessage());
        }
    }


    private List<OrderItem> createOrderItems(List<CartItemDTO> cartItems, Order order){
        return cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .productVariantId(cartItem.getVariantId())
                        .unitPrice(cartItem.getPrice())
                        .quantity(cartItem.getQuantity())
                        .order(order)
                        .build())
                .toList();
    }


    private ShippingAddress getShippingAddress(Long id){
        return shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ShippingAddress.class.getSimpleName(), FIELD_ID, id));
    }

    private BigDecimal getShippingPrice(ShippingAddress shippingAddress){
        return shippingAddress.getShippingDistrict().getShippingPrice();
    }


    private BigDecimal calculateOrderTotal(List<CartItemDTO> cartItems, BigDecimal shippingPrice){
        BigDecimal itemsTotal = cartItems.stream().map(item ->
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return itemsTotal.add(shippingPrice);
    }

    @Transactional
    private Order saveOrder( Long userId, List<CartItemDTO> cartItems, OrderRequest orderRequest) {
        ShippingAddress shippingAddress = getShippingAddress(orderRequest.getShippingAddressId());

        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.PENDING_PAYMENT)
                .shippingAddress(shippingAddress)
                .contactPhone(orderRequest.getContactPhone())
                .build();

        order.setOrderItems(createOrderItems(cartItems, order));
        order.setTotalAmount(calculateOrderTotal(cartItems, getShippingPrice(shippingAddress)));

        return orderRepository.save(order);
    }

}
