package ProductMicroservice.mapper;

import ProductMicroservice.dto.ProductDetailDto;
import ProductMicroservice.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "brand", target = "brand")
    @Mapping(target = "productMediaMappings", source = "productMediaMappings")
    ProductDetailDto toDetailDto(Product product);

}

