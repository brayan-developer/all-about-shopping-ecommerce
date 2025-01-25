package MicroservicePago.entity;

import MicroservicePago.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shipping_addresses")
@Getter
@Setter
public class ShippingAddress extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 100)
    private String street;

    @Column(length = 60)
    private String addressLine2;

    @Column(length = 10)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "shipping_district_id", nullable = false)
    private ShippingDistrict shippingDistrict;
}