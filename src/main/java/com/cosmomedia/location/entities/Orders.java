package com.cosmomedia.location.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double amount;
    private byte[] previewFront;
    private byte[] previewBack;

    @ElementCollection
    @CollectionTable(name = "images_front", joinColumns = @JoinColumn())
    @Column(name = "file_data", columnDefinition = "bytea")
    private List<byte[]> front;

    @ElementCollection
    @CollectionTable(name = "images_back", joinColumns = @JoinColumn())
    @Column(name = "file_data", columnDefinition = "bytea")
    private List<byte[]> back;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @ToString.Exclude
    private List<Product> products;
}
