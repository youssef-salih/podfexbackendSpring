package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
