package com.talbn1.shield.controllers;

import com.talbn1.shield.domain.Photo;
import com.talbn1.shield.repositories.PhotoEntityRepository;
import com.talbn1.shield.repositories.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PhotoController.class)
class PhotoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    PhotoEntityRepository photoEntityRepository;

}