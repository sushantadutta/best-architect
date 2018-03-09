package com.bestarchitect.createservice.controller;

import com.bestarchitect.model.dto.CreateProfileDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BestArchitectCreateControllerImpl implements BestArchitectCreateController<CreateProfileDTO> {

    @Override
    public ResponseEntity<?> create(CreateProfileDTO incomingMessage, String apiVersion) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}