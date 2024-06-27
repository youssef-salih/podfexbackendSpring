package com.cosmomedia.podfex.entities;

import com.cosmomedia.podfex.enums.StatusAdmin;
import com.cosmomedia.podfex.enums.StatusUser;
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
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte[] preview;
    private String orderNo;
    private Double price;
    private StatusUser statusUser;
    private StatusAdmin statusAdmin;
    private Integer quantity;
    private String type;
    //to be displayed to admins (ready for printing)
    private Boolean confirmed;

    @ManyToOne(optional = true)
    @JoinColumn(name = "personnel_id", foreignKey = @ForeignKey(name = "fk_orders_personnel"))
    private Users personnel;

    @ManyToOne(optional = true)
    @JoinColumn(name = "seller_id", foreignKey = @ForeignKey(name = "fk_orders_seller"))
    private Users seller;

    @ManyToOne
    private Product product;

    @OneToOne(fetch = FetchType.EAGER)
    private Orders order;

    @ManyToOne
    private Client client;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "images", joinColumns = @JoinColumn())
    @Column(name = "file_data", columnDefinition = "bytea")
    private List<byte[]> images;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

}
