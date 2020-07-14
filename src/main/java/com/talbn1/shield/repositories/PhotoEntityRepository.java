package com.talbn1.shield.repositories;

import com.talbn1.shield.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author talbn on 7/13/2020
 **/
public interface PhotoEntityRepository extends JpaRepository<Photo, Long> {

    @Query("SELECT t FROM Photo t WHERE t.albumId = ?1")
    List<Photo> findByAlbumId(Long albumId);

}
