package ProductMicroservice.service.impl;

import ProductMicroservice.model.CoverImage;
import ProductMicroservice.repository.CoverImageRepository;
import ProductMicroservice.service.ICoverImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static ProductMicroservice.constants.CacheConstants.GET_COVER_IMAGES_BY_GROUP;

@RequiredArgsConstructor
@Service
public class CoverImageServiceImp implements ICoverImageService {

    private final CoverImageRepository coverImageRepository;
    @Override
    @Cacheable(GET_COVER_IMAGES_BY_GROUP)
    public List<CoverImage> getByCoverGroup(int coverGroup) {
        return coverImageRepository.findByCoverGroup(coverGroup);
    }
}
