package com.cosmomedia.podfex.repositories;

import com.cosmomedia.podfex.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}