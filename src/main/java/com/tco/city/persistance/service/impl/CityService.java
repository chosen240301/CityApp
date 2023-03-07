package com.tco.city.persistance.service.impl;

import com.tco.city.persistance.entity.City;
import com.tco.city.persistance.repository.CityRepository;
import com.tco.city.persistance.service.ICityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService implements ICityService {
  CityRepository repository;
  UserDetailsService userDetailsService;

  CityService(CityRepository repository, UserDetailsService userDetailsService) {
    this.repository = repository;
    this.userDetailsService = userDetailsService;
  }

  @Override public City get(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public Page<City> getByPage(int limit, int page, String filter) {
    PageRequest pageRequest = PageRequest.of(page, limit);
    Page<City> cityPage = repository.findByNameContainingIgnoreCase(filter, pageRequest);
    return cityPage;
  }

  @Override
  public List<City> getAll() {
    return (List<City>) repository.findAll();
  }

  @Override
  public void add(City city) {
    repository.save(city);
  }

  @Override public void deleteAll() {
    repository.deleteAll();
  }

}
