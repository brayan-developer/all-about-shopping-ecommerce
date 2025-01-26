package ProductMicroservice.controller;

import ProductMicroservice.dto.*;
import ProductMicroservice.model.CoverImage;
import ProductMicroservice.payload.ApiResponse;
import ProductMicroservice.dto.PaginatedResponseDTO;
import ProductMicroservice.service.ICoverImageService;
import ProductMicroservice.service.IProductService ;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static ProductMicroservice.constants.GeneralConstants.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final IProductService productService;
    private final ICoverImageService coverImagesService;

    @GetMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        ProductDetailDto productDetail = productService.getProductById(id);
        return new ResponseEntity<>(ApiResponse.success(productDetail), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse> getByCategoryAndBrandAndPrice(@PathVariable Long categoryId,
                                                                     @RequestParam(required = false) Long brand,
                                                                     @RequestParam(required = false) BigDecimal minPrice,
                                                                     @RequestParam(required = false) BigDecimal maxPrice,
                                                             @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page){
        PaginatedResponseDTO<ProductPreviewDTO> products = productService.getByCategoryAndBrandAndPriceRange(categoryId,
                brand, minPrice, maxPrice, page);
        return new ResponseEntity<>(ApiResponse.success(products), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}/price-range")
    public ResponseEntity<ApiResponse> getPriceRangeByCategoryAndBrand(@PathVariable Long categoryId,
                                                                          @RequestParam(required = false) Long brand) {
        return new ResponseEntity<>(ApiResponse.success(productService.getPriceRangeByCategoryAndBrand(categoryId,brand)),
                HttpStatus.OK);
    }


    @GetMapping("/random/categories/{categoryId}")
    public ResponseEntity<ApiResponse> getRandomProductsByCategory(@PathVariable Long categoryId) {
        List<ProductPreviewDTO> products = productService.getRandomProductsByCategory(categoryId);
        return new ResponseEntity<>(ApiResponse.success(products) , HttpStatus.OK);
    }



    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<ApiResponse> getBySearchTerm(@Valid @PathVariable @Size(min = MIN_SEARCH_TERM_LENGTH)
                                                           String searchTerm,
                                                       @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page) {
        PaginatedResponseDTO<ProductPreviewDTO> products = productService.getBySearchTerm(searchTerm, page);
        return new ResponseEntity<>(ApiResponse.success(products), HttpStatus.OK);
    }


    @GetMapping("/search/{searchTerm}/price-range")
    public ResponseEntity<ApiResponse> getPriceRangeBySearchTerm(@Valid @PathVariable @Size(min= MIN_SEARCH_TERM_LENGTH)
                                                                      String searchTerm){
        return ResponseEntity.ok(ApiResponse.success(productService.getPriceRangeBySearchTerm(searchTerm)));
    }


    @GetMapping
    public ResponseEntity<ApiResponse> getByIds(@RequestParam List<Long> ids){
        return new ResponseEntity<>(ApiResponse.success(productService.getProductsByIds(ids)), HttpStatus.OK);
    }


    @GetMapping("/cover-images/groups/{id}")
    public ResponseEntity<ApiResponse> getCoverImagesByCoverGroup(@PathVariable int id){
        List<CoverImage> coverImages = coverImagesService.getByCoverGroup(id);
        return new ResponseEntity<>(ApiResponse.success(coverImages), HttpStatus.OK);
    }



}

