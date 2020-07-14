package com.talbn1.shield.controllers;

import com.talbn1.shield.domain.Photo;
import com.talbn1.shield.exceptions.ValidationException;
import com.talbn1.shield.model.PhotoDto;
import com.talbn1.shield.services.PhotoService;
import com.talbn1.shield.services.PhotoServiceIml;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * @author talbn on 7/11/2020
 **/
@RestController
@Controller
@RequestMapping("/api/v1/photo/")

public class PhotoRestController {

    private final PhotoService photoService ;

    public PhotoRestController(PhotoService photoService){
        this.photoService = photoService;
    }

    @GetMapping("download")
    public List<Photo> downloadAll() throws Exception {

        String rawJson =  PhotoServiceIml.request("https://s3.amazonaws.com/shielddevtest/photo.txt").
                replaceAll("\\uFEFF", "");

        JSONObject photosJsonObject = new JSONObject(rawJson);
        JSONArray photoList =  photosJsonObject.getJSONArray("photos");
        List<Photo> rvPhotos = photoService.mapPhotoObjects(photoList);
        photoService.saveToDb(rvPhotos);
        return rvPhotos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDto> getPhotoById(@PathVariable("id") Long id){
        return new ResponseEntity<>(photoService.getById(id),HttpStatus.OK);
    }

    @PostMapping(path = "newPhoto")
    public ResponseEntity saveNewPhoto(@Valid @RequestBody  PhotoDto photoDto) throws ValidationException {
        photoDto.setFileSize(photoService.getFileSize(photoDto.getUrl()));
        photoDto.setLocalPath(photoService.getLocalPath(photoDto.getId().toString(), photoDto.getUrl()));
        photoService.downloadAfterInitial(photoDto);
        return new ResponseEntity<>(photoService.savePhoto(photoDto), HttpStatus.CREATED);
    }

    @PostMapping(path = "newPhotos")
    public ResponseEntity<PhotoDto> saveNewPhotos( @RequestBody  List<@Valid PhotoDto> list) throws ValidationException {
        for (PhotoDto dto: list) {
            photoService.downloadAfterInitial(dto);
        }
        return new ResponseEntity<>(photoService.savePhotos(list), HttpStatus.CREATED);
    }
}