package com.tco.city.persistance.repository;

import com.tco.city.persistance.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends PagingAndSortingRepository<City, Long> {
  Page<City> findByNameContainingIgnoreCase(String name, PageRequest pageRequest);

}
