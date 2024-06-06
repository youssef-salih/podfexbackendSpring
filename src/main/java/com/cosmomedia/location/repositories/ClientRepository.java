package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}