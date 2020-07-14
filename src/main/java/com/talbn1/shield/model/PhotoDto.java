package com.talbn1.shield.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Table;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author talbn on 7/12/2020
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="photo")
public class PhotoDto implements Serializable {

    @Id
    @NotNull(message = "ID cannot be null or empty")
    private Long id;

    @NotNull
    private Long albumId;

    private String title;

    @NotBlank
    private String url;

    private String thumbnailUrl;

    @Null
    private String localPath;

    @Null
    private String fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    @Null
    private OffsetDateTime createdDate;



}
