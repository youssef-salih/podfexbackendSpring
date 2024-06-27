package com.cosmomedia.podfex.entities;

import com.cosmomedia.podfex.enums.Colors;
import com.cosmomedia.podfex.enums.Sizes;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Sizes size;

    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private Colors color;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;
}
