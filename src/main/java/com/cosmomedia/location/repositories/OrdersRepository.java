package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
