package com.talbn1.shield.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * @author talbn on 7/11/2020
 **/

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Photo {

    @Id
    //@Column(length = 36, columnDefinition = "varchar(36)",unique = true,updatable = false, nullable = false)
    private Long id;
    private Long albumId;
    private String thumbnailUrl;
    private String url;
    private String title;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;
    private String fileSize;
    private String localPath;

}
