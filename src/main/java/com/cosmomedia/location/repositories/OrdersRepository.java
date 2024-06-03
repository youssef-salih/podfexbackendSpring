package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long> {


    Optional<Orders> findByOrderNo(String orderNo);

    List<Orders> findByType(String type);

    List<Orders> findByTypeNotLike(String type);
}
