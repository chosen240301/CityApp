package com.tco.city.persistance.service;

import com.tco.city.persistance.entity.City;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICityService {
  City get(Long id);
  List<City> getAll();
  void add(City city);
  Page<City> getByPage(int limit, int page, String filter);

  void deleteAll();
}
