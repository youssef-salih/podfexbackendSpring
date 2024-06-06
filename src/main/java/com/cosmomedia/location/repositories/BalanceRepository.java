package com.cosmomedia.location.repositories;


import com.cosmomedia.location.entities.Balance;
import com.cosmomedia.location.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
}
