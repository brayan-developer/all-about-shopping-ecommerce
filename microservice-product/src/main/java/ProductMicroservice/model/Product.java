package ProductMicroservice.model;

import ProductMicroservice.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@EqualsAndHashCode(callSuper = false)
@Getter @Setter
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 80)
    private String name;

    private String description;

    private BigDecimal price;

    private String mainImage;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private Double discount;

    private Double qualification;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"),
            indexes = {@Index(name = "idx_category_id", columnList = "category_id")})
    private Set<Category> categories = new HashSet<>();


    @OneToMany(mappedBy = "product")
    private List<ProductMediaMapping> productMediaMappings = new ArrayList<>();


}
