package MicroservicePago.dto;

import MicroservicePago.entity.ShippingDistrict;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShippingAddressDTO {

    private Long id;
    private String street;
    private String addressLine2;
    private String zipCode;
    private ShippingDistrict shippingDistrict;
}
