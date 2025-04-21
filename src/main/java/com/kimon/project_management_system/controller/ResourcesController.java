package com.kimon.project_management_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourcesController {
    @GetMapping("/resources")
    public ResponseEntity<String> getResources(){
        return new ResponseEntity<>("Resources accessed", HttpStatus.ACCEPTED);
    }
}
