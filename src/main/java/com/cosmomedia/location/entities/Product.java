package com.cosmomedia.location.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "surface_id", referencedColumnName = "id")
    private Surface surface;

    @ManyToMany(mappedBy = "products")
    @ToString.Exclude
    private List<Orders> orders;

}
