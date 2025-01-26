package ProductMicroservice.controller;

import ProductMicroservice.payload.ApiResponse;
import ProductMicroservice.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    private final ICategoryService categoryService;


    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories(){
        return new ResponseEntity<>(ApiResponse.success(categoryService.getAllCategories()), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/brands")
    public ResponseEntity<ApiResponse> getBrandsByCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(ApiResponse.success(categoryService.getBrandsByCategory(categoryId)), HttpStatus.OK);
    }

}
