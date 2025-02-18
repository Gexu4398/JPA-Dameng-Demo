package com.gexu.jpadamengdemo.service;

import cn.hutool.core.util.StrUtil;
import com.gexu.jpadamengdemo.model.City;
import com.gexu.jpadamengdemo.model.City_;
import com.gexu.jpadamengdemo.repository.CityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CityService {

  private final CityRepository cityRepository;

  @Autowired
  public CityService(CityRepository cityRepository) {

    this.cityRepository = cityRepository;
  }

  public City newCity(@Nonnull City city) {

    final var optionalCity = cityRepository.findOne(Specification.where(nameIs(city.getName())));

    if (optionalCity.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return cityRepository.save(city);
  }

  public Page<City> getCities(String keyword, Pageable pageable) {

    return cityRepository.findAll(Specification.where(nameLike(keyword)), pageable);
  }

  public City getCity(Long id) {

    return cityRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  public City updateCity(Long id, String name) {

    final var city = getCity(id);
    if (StrUtil.equals(city.getName(), name)) {
      return city;
    }
    final var optionalCity = cityRepository.findOne(Specification.where(nameIs(name)));
    if (optionalCity.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    city.setName(name);
    return cityRepository.save(city);
  }

  public void deleteCity(Long id) {

    final var city = getCity(id);
    cityRepository.delete(city);
  }

  private @Nonnull Specification<City> nameIs(String name) {

    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(City_.NAME), name);
  }

  private @Nonnull Specification<City> nameLike(String keyword) {

    return (root, query, criteriaBuilder) -> {
      if (StrUtil.isBlank(keyword)) {
        return criteriaBuilder.and();
      }
      return criteriaBuilder.like(root.get(City_.NAME), "%" + keyword + "%");
    };
  }
}
