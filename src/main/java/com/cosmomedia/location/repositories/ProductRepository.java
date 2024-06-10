package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);

    Page<Product> findByNameAndActiveTrue(String name, Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);


    @Override
    Optional<Product> findById(Long id);
}
