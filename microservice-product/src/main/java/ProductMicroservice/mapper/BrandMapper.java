package ProductMicroservice.mapper;

import ProductMicroservice.dto.BrandDTO;
import ProductMicroservice.model.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandDTO toBrandDto(Brand brand);
}
