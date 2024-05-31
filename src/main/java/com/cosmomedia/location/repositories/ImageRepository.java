package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
