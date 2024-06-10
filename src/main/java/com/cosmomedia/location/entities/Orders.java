package com.cosmomedia.location.entities;

import com.cosmomedia.location.enums.StatusAdmin;
import com.cosmomedia.location.enums.StatusUser;
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
    @ManyToOne
    private Users personnel;

    @ManyToOne
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
