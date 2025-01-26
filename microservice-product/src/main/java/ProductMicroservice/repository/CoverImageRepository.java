package ProductMicroservice.repository;

import ProductMicroservice.model.CoverImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoverImageRepository extends JpaRepository<CoverImage, Long> {

    List<CoverImage> findByCoverGroup(int group);
}
