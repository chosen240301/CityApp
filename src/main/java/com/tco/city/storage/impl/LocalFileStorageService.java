package com.tco.city.storage.impl;

import com.tco.city.storage.IFileStorageService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LocalFileStorageService implements IFileStorageService {

  private final Path rootPath = Paths.get("images");

  LocalFileStorageService() {
    initStorage();
  }

  public void initStorage() {
    try {
      Files.createDirectories(rootPath);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize local storage");
    }
  }

  @Override public void save(String name, InputStream inputStream) {
    try {
      Path file = rootPath.resolve(name);
      Files.createDirectories(file.getParent());
      Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override public Resource load(String name) {
    Path file = rootPath.resolve(name);
    try {
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        return new ClassPathResource("static/images/no_photo.png");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(String name) {
    Path file = rootPath.resolve(name);
    try {
      Files.deleteIfExists(file);
    } catch (IOException e) {
      //NOP;
    }
  }

    @Override
    public void deleteAll() {
      try {
        Files.list(Paths.get("./images")).forEach(path -> {
          try {
            Files.delete(path);
          } catch (IOException e) {
            //NOP;
          }
        });
      } catch (IOException e) {
        //NOP;
      }
    }

}
