package ProductMicroservice.service;

import ProductMicroservice.model.CoverImage;

import java.util.List;

public interface ICoverImageService {

    List<CoverImage> getByCoverGroup(int coverGroup);
}
