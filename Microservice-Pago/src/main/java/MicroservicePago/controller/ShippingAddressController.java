package MicroservicePago.controller;


import MicroservicePago.dto.ShippingAddressDTO;
import MicroservicePago.payload.ApiResponse;
import MicroservicePago.request.ShippingAddressRequest;
import MicroservicePago.service.IShippingAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static MicroservicePago.constants.GeneralConstants.ID_IN_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/shipments/addresses")
public class ShippingAddressController {


    private final IShippingAddressService shippingAddressService;


    @GetMapping
    public ResponseEntity<ApiResponse> getByUserId(@RequestHeader("${gateway.custom-headers.user-id}") Long userId){
        return new ResponseEntity<>(ApiResponse.success(shippingAddressService.getByUserId(userId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveShippingAddress(@RequestBody ShippingAddressRequest addressRequest,
                                                           @RequestHeader("${gateway.custom-headers.user-id}")Long userId){
            ShippingAddressDTO shippingAddress = shippingAddressService.saveShippingAddress(addressRequest,userId);
            return new ResponseEntity<>(ApiResponse.success(shippingAddress), HttpStatus.CREATED);
    }

    @PutMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> updateShippingAddress(@PathVariable Long id,
                                                             @RequestHeader("${gateway.custom-headers.user-id}")Long userId,
                                                             @Valid @RequestBody ShippingAddressRequest addressRequest){
        ShippingAddressDTO shippingAddress = shippingAddressService.updateShippingAddress(id, addressRequest, userId);
        return new ResponseEntity<>(ApiResponse.success(shippingAddress), HttpStatus.OK);
    }
}
