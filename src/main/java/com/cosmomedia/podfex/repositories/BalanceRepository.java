package com.cosmomedia.podfex.repositories;


import com.cosmomedia.podfex.entities.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance findByUsers_Email(String email);
}
