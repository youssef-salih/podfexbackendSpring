package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByLastName(String lastName);

    Page<Users> findByDeletedAtNotNullOrderByDeletedAtDesc(Pageable pageable);
    Page<Users> findAll(Specification<Users> specification, Pageable pageable);


    @Override
    Optional<Users> findById(Long aLong);
}
