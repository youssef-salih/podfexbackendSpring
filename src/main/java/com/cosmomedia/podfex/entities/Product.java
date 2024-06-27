package com.cosmomedia.podfex.entities;


import com.cosmomedia.podfex.enums.Colors;
import com.cosmomedia.podfex.enums.Sizes;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private byte[] image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "surface_id", referencedColumnName = "id")
    private Surface surface;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Sizes.class)
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<Sizes> sizes;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Colors.class)
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<Colors> colors;

    private Boolean active;

    public Product(Long productId) {
        this.id = productId;
    }
}
