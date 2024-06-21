package com.cosmomedia.location.entities;

import com.cosmomedia.location.enums.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;

//    @PrePersist
//    @PreUpdate
//    private void validateUserRole() {
//        if (users.getRole() != Roles.CUSTOMER) {
//            throw new IllegalArgumentException("Only users with CLIENT role can have a balance.");
//        }
//    }
}
