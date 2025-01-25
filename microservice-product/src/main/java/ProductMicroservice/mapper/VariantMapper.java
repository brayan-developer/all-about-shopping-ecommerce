package ProductMicroservice.mapper;

import ProductMicroservice.dto.VariantDetailDTO;
import ProductMicroservice.dto.VariantDTO;
import ProductMicroservice.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariantMapper {

    @Mapping(target = "productId" , source = "product.id")
    @Mapping(target = "name" , source = "product.name")
    @Mapping(target = "imageUrl", expression = "java(getMainImageUrl(productVariant))")
    VariantDetailDTO toDto(ProductVariant productVariant);


    default String getMainImageUrl(ProductVariant productVariant) {
        return productVariant.getProductMediaMappings()
                .stream()
                .filter(mediaUsage -> Boolean.TRUE.equals(mediaUsage.getProductMedia().getIsPrimary()))
                .map(mediaUsage -> mediaUsage.getProductMedia().getUrl())
                .findFirst()
                .orElse(null);
    }


    VariantDTO toDetailDto(ProductVariant productVariant);


}
