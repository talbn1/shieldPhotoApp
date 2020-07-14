package com.talbn1.shield.mappers;

import com.talbn1.shield.domain.Photo;
import com.talbn1.shield.model.PhotoDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * @author talbn on 7/12/2020
 **/
@Service
@AllArgsConstructor
public class Mapper{

    public PhotoDto photoToPhotoDto(Optional<Photo> photo) {

        PhotoDto dto = new PhotoDto();
        if(photo.isPresent()){
            try {
                dto.setAlbumId(photo.get().getAlbumId());
                dto.setId(photo.get().getId());
                dto.setUrl(photo.get().getUrl());
                dto.setThumbnailUrl(photo.get().getThumbnailUrl());
                dto.setTitle(photo.get().getTitle());
                dto.setFileSize(photo.get().getFileSize());
                dto.setLocalPath(photo.get().getLocalPath());

            }catch (NullPointerException nullPointerException){
                System.out.println("->photoToPhotoDto::photoToPhotoDto-> NullPointerException Caught");
                nullPointerException.printStackTrace();
            }
            catch (Exception Exception){
                System.out.println("->photoToPhotoDto::photoToPhotoDto-> Exception Caught");
                Exception.printStackTrace();
            }
        }
        return dto;
    }

    public Photo photoDtoTophoto(PhotoDto dto) {

        Photo photo =  new Photo();
        if (dto != null) {
            try {
                photo = new Photo();
                photo.setId(dto.getId());
                photo.setAlbumId(dto.getAlbumId());
                photo.setTitle(dto.getTitle());
                photo.setUrl(dto.getUrl());
                photo.setThumbnailUrl(dto.getThumbnailUrl());
                photo.setLocalPath(dto.getLocalPath());
                photo.setFileSize(dto.getFileSize());
                return photo;
            } catch (NullPointerException nullPointerException) {
                System.out.println("->photoToPhotoDto::photoToPhotoDto-> NullPointerException Caught");
                nullPointerException.printStackTrace();
            } catch (Exception Exception) {
                System.out.println("->photoToPhotoDto::photoToPhotoDto-> Exception Caught");
                Exception.printStackTrace();
            }
        }
        return photo;
    }
}
