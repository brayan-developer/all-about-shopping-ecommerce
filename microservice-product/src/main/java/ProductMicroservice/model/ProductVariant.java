package ProductMicroservice.model;

import ProductMicroservice.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
public class ProductVariant extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    private BigDecimal price;

    @Column(length = 100, nullable = false, unique = true)
    private String sku;

    @OneToMany(mappedBy = "productVariant", fetch = FetchType.EAGER)
    private List<ProductMediaMapping> productMediaMappings = new ArrayList<>();


    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<ProductVariantAttribute> variantAttributes = new ArrayList<>();


}
