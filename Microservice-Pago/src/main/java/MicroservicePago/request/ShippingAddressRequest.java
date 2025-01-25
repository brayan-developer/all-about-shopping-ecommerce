package MicroservicePago.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShippingAddressRequest {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String street;

    @NotNull
    @Size(max = 60)
    private String addressLine2;

    @NotNull
    @Pattern(regexp = "\\d{3,10}")
    private String zipCode;

    @NotNull
    private Long shipping_district_id;
}
