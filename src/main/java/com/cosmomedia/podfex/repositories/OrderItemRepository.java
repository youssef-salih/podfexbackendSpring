package com.cosmomedia.podfex.repositories;


import com.cosmomedia.podfex.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
