package com.cosmomedia.location.repositories;


import com.cosmomedia.location.entities.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
}
