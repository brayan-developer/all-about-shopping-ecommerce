package MicroservicePago.service;

import MicroservicePago.dto.CartItemDTO;
import MicroservicePago.exception.PaymentProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {
    private static final String BASE_URL = "https://api.mercadopago.com/v1/payments/";

    @Value("${mercado_pago.access_token}")
    private String ACCESS_TOKEN;

    public JsonNode getPaymentDetails(String paymentId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                BASE_URL + paymentId,
                HttpMethod.GET,
                entity,
                JsonNode.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        throw new PaymentProcessingException("Error getting payment details: " + response.getBody());

    }


    public List<PreferenceItemRequest> createPreferenceItems(List<CartItemDTO> cartItems) {
        List<PreferenceItemRequest> items = new ArrayList<>();
        cartItems.forEach(cartItem -> items.add(PreferenceItemRequest.builder()
                .id(String.valueOf(cartItem.getVariantId()))
                .quantity(cartItem.getQuantity())
                .currencyId("PEN")
                .unitPrice((cartItem.getPrice()))
                .build()));
        return items;
    }


    public Preference createPaymentPreference(Long orderId, List<PreferenceItemRequest> items,
                                               BigDecimal shippingCost) throws Exception{
        PreferenceShipmentsRequest shipments = PreferenceShipmentsRequest.builder()
                .cost(shippingCost)
                .mode("not_specified")
                .build();

        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                .installments(1) //a single payment
                .build();

        OffsetDateTime expirationDateTo = OffsetDateTime.now().plusDays(1);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .shipments(shipments)
                .paymentMethods(paymentMethods)
                .metadata(Map.of("order_id", orderId))
                .expires(true)
                .expirationDateTo(expirationDateTo)
                .build();

        MercadoPagoConfig.setAccessToken(ACCESS_TOKEN);
        return new PreferenceClient().create(preferenceRequest);
    }
}
