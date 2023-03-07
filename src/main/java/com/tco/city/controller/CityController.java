package com.tco.city.controller;

import com.tco.city.persistance.entity.City;
import com.tco.city.persistance.service.impl.CityService;
import com.tco.city.storage.IFileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@CrossOrigin
@Controller
@RequestMapping("/api")
public class CityController {

    private CityService service;
    private IFileStorageService storageService;

    CityController(CityService service, IFileStorageService storageService) {
        this.service = service;
        this.storageService = storageService;
    }

    @GetMapping("/getData")
    public ResponseEntity<Page<City>> getData(
        @RequestParam("limit") int limit,
        @RequestParam("page") int page,
        @RequestParam("filter") String filter
    ) {
        Page<City> cities = service.getByPage(limit, page, filter);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }


    @PostMapping("/city/{id}")
    public ResponseEntity updateCity (
        @PathVariable String id,
        @RequestBody City city) {
        City persisted = service.get(Long.parseLong(id));
        persisted.setName(city.getName());
        service.add(persisted);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/city/{id}/updatePhoto")
    public ResponseEntity save (
        @PathVariable String id,
        @RequestParam("file") MultipartFile file) throws IOException {
        try {
            if (!file.isEmpty()) {
                storeImage(Long.parseLong(id), file.getInputStream());
            }
        } catch (IOException e) {
            return new ResponseEntity("Failed to upload image", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg").body(file);
    }

    private void storeImage(Long id, InputStream inputStream) {
        storageService.save(id.toString(), inputStream);
    }



}
