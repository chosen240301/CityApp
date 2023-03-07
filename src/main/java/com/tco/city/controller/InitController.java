package com.tco.city.controller;

import com.tco.city.persistance.entity.City;
import com.tco.city.persistance.service.impl.CityService;
import com.tco.city.storage.IFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@CrossOrigin
@Controller
@RequestMapping("/init")
@Slf4j
public class InitController {

  final private static String COMMA_DELIMITER = ",";

  private static boolean IS_RUNNING = false;

  private final int ATTEMPTS = 10;
  private final int INTERVAL = 5000;

  private CityService cityService;
  private IFileStorageService fileStorage;

  InitController(CityService cityService, IFileStorageService fileStorage) {
    this.cityService = cityService;
    this.fileStorage = fileStorage;
  }

  @GetMapping("/") public ResponseEntity init() {
    if (IS_RUNNING)
      return new ResponseEntity(HttpStatus.OK);
    IS_RUNNING = true;
    cityService.deleteAll();
    fileStorage.deleteAll();
    ClassPathResource resource = new ClassPathResource("init/cities.csv");

    try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      br.readLine();
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(COMMA_DELIMITER);
        addData(values[0], values[1], new URL(values[2]));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    IS_RUNNING = false;
    return new ResponseEntity(HttpStatus.OK);
  }

  private void addData(String id, String name, URL imageUrl) {
    for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
      try {
        URLConnection urlConnection = imageUrl.openConnection();
        City city = new City(null, name);
        cityService.add(city);
        fileStorage.save(city.getId().toString(), urlConnection.getInputStream());
        break;
      } catch (FileNotFoundException e) {
        //404 error
        log.warn("No image " + id);
        break;
      } catch (IOException e) {
        //443 error
        log.error("Failed to get image " + id);
        try {
          Thread.sleep(INTERVAL);
        } catch (InterruptedException ex) {
          //NOP;
        }
      }
    }

  }
}


