package com.talbn1.shield.services;

import com.talbn1.shield.domain.Photo;
import com.talbn1.shield.model.PhotoDto;
import org.json.JSONArray;

import java.util.List;

/**
 * @author talbn on 7/12/2020
 **/

public interface PhotoService {
    PhotoDto getById(Long id);
    PhotoDto savePhoto(PhotoDto photo);
    PhotoDto savePhotos(List<PhotoDto> photosDto);
    List<Photo> mapPhotoObjects(JSONArray photoList);
    void saveToDb(List<Photo> rvPhotos);
    String getFileSize(String link);
    String getLocalPath(String id,String url);
    void downloadAfterInitial(PhotoDto dto);
}
