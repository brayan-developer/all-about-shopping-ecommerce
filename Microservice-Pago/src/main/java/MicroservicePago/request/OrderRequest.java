package MicroservicePago.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderRequest {


    @NotNull
    private Long shippingAddressId;

    @NotNull
    @Min(9)
    private String contactPhone;
}
