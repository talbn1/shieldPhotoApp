package com.talbn1.shield.controllers;

import com.talbn1.shield.domain.Photo;
import com.talbn1.shield.repositories.PhotoEntityRepository;
import com.talbn1.shield.repositories.PhotoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author talbn on 7/13/2020
 **/

@RequestMapping("/photos")
@Controller
public class PhotoController {

    private final PhotoRepository photoRepository;
    private final PhotoEntityRepository photoEntityRepository;

    public PhotoController(PhotoRepository photoRepository, PhotoEntityRepository photoEntityRepository) {
        this.photoRepository = photoRepository;
        this.photoEntityRepository = photoEntityRepository;
    }

    @RequestMapping({"/list", "list.html"})
    public String getPhotosList(Model model){
        model.addAttribute("photos", photoRepository.findAll());
        return "photosList1";
    }

    @RequestMapping(value = "/album/{id}")
    public String findPhotosByAlbum(Model model, @PathVariable("id") long id){
        model.addAttribute("photos",photoEntityRepository.findByAlbumId(id) );
        return "byAlbum";
    }
}
