package com.talbn1.shield.repositories;

import com.talbn1.shield.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author talbn on 7/11/2020
 **/
public interface PhotoRepository extends JpaRepository<Photo,Long> {
}
