package com.Inventory.service.impl;

import com.Inventory.dto.CartItemDTO;
import com.Inventory.dto.ProductOutOfStockDTO;
import com.Inventory.dto.ProductInventoryDTO;
import com.Inventory.mapper.ProductInventoryMapper;
import com.Inventory.model.ProductReservation;
import com.Inventory.payload.ApiResponse;
import com.Inventory.repository.InventoryRepository;
import com.Inventory.service.HandleStockService;
import com.Inventory.service.InventoryService;
import com.Inventory.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.Inventory.constants.GeneralConstants.INSUFFICIENT_STOCK_MESSAGE;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {


    private final InventoryRepository inventoryRepository;
    private final ProductInventoryMapper productInventoryMapper;
    private final ReservationService reservationService;
    private final HandleStockService stockService;


    @Override
    public List<ProductInventoryDTO> getReducedInventories(List<Long> variantIds) {
        Map<Long , Integer> reservations = castReservationsToMap(
                reservationService.findReservationsActiveByVariants(variantIds));

        List<ProductInventoryDTO> inventories = getInventoriesByVariantIds(variantIds);

        inventories.forEach( i -> {
            var reservation = reservations.get(i.getVariantId());
            if(reservation != null){
                i.setStock(i.getStock() - reservation);
            }
        });

        return inventories;
    }



    @Override
    public ApiResponse checkAvailableStock() {

        List<CartItemDTO> cartItems = stockService.getCartByUser().getCartItems();
        List<ProductInventoryDTO> inventories = getReducedInventories(extractVariantsId(cartItems));
        Map<Long , Integer> inventoriesMap = castInventoriesToMap(inventories);
        List<ProductOutOfStockDTO> outOfStock = new ArrayList<>();

        cartItems.forEach(item -> {
            var inventory = inventoriesMap.get(item.getVariantId());
            if(inventory != null && inventory < item.getQuantity()){
                outOfStock.add(new ProductOutOfStockDTO(item.getVariantId(), item.getQuantity(),
                       inventory < 0 ? 0 : inventory));
            }

            if(inventory == null) {
                outOfStock.add(new ProductOutOfStockDTO(item.getVariantId(), item.getQuantity(), 0));
            }

        });

        if (!outOfStock.isEmpty()){
            return new ApiResponse( INSUFFICIENT_STOCK_MESSAGE,false, outOfStock);
        }
        return new ApiResponse("OK", true, cartItems);

    }

    public ApiResponse checkAvailableStock2() {
        List<CartItemDTO> cartItems = stockService.getCartByUser().getCartItems();
        Map<Long, Integer> inventoriesMap = castInventoriesToMap(getReducedInventories(extractVariantsId(cartItems)));

        List<ProductOutOfStockDTO> outOfStock = cartItems.stream()
                .map(item -> {
                    int availableStock = inventoriesMap.getOrDefault(item.getVariantId(), 0);
                    if (availableStock < item.getQuantity()) {
                        return new ProductOutOfStockDTO(
                                item.getVariantId(),
                                item.getQuantity(),
                                Math.max(availableStock, 0)
                        );
                    }
                    return null; // No está fuera de stock
                })
                .filter(Objects::nonNull) // Filtra los que no están fuera de stock
                .collect(Collectors.toList());

        if (!outOfStock.isEmpty()) {
            return new ApiResponse( INSUFFICIENT_STOCK_MESSAGE,false, outOfStock);
        }

        return new ApiResponse("OK", true, cartItems);
    }


    private List<ProductInventoryDTO> getInventoriesByVariantIds(List<Long> variantIds) {
        return inventoryRepository.findByVariantIdIn(variantIds).stream()
                .map(productInventoryMapper::toDto)
                .collect(Collectors.toList());
    }


    private List<Long> extractVariantsId(List<CartItemDTO> cartItems){
        return cartItems.stream().map(CartItemDTO::getVariantId).collect(Collectors.toList());
    }


    private Map<Long, Integer> castReservationsToMap(List<ProductReservation> reservations){
        return reservations.stream().collect(Collectors.toMap(
                ProductReservation::getVariantId,
                ProductReservation::getReservedQuantity,
                Integer::sum
        ));
    }

    private Map<Long , Integer> castInventoriesToMap(List<ProductInventoryDTO> inventories){
        return inventories.stream().collect(Collectors.toMap(ProductInventoryDTO::getVariantId,
                ProductInventoryDTO::getStock));
    }


}
