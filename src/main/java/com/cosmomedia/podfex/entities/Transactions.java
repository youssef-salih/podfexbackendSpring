package com.cosmomedia.podfex.entities;

import com.cosmomedia.podfex.enums.StatusTransaction;
import com.cosmomedia.podfex.enums.TransactionState;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity

public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    //type of transaction
    private StatusTransaction status;
    //confirmed from admin after checking
    private Boolean confirmed;
    private TransactionState transactionState;
    private String transactionNo;
    @Column(nullable = false, updatable = false)
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;


}
