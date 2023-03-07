package com.tco.city.controller;

import com.tco.city.storage.IFileStorageService;
import org.springframework.stereotype.Controller;

@Controller
public class ImageController {

  private IFileStorageService fileStorageService;

  ImageController(IFileStorageService fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

}
