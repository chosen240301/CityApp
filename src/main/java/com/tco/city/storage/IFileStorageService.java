package com.tco.city.storage;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;


public interface IFileStorageService {
  void save(String name, InputStream inputStream);

  void remove(String name);
  Resource load(String name);

  void deleteAll();
}
