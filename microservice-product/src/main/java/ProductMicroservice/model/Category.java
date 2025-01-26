package ProductMicroservice.model;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id") // Relationship with the parent category
    private Category parentCategory; // If null, it is a parent category


}
