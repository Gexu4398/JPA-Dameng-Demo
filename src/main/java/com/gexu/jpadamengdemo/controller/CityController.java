package com.gexu.jpadamengdemo.controller;

import com.gexu.jpadamengdemo.model.City;
import com.gexu.jpadamengdemo.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "城市管理")
@RestController
@RequestMapping("city")
@Slf4j
public class CityController {

  private final CityService cityService;

  @Autowired
  public CityController(CityService cityService) {

    this.cityService = cityService;
  }

  @Operation(summary = "城市列表")
  @GetMapping
  @SneakyThrows
  public Page<City> getCities(
      @RequestParam(name = "q", required = false, defaultValue = "") String keyword,
      Pageable pageable) {

    return cityService.getCities(keyword, pageable);
  }

  @Operation(summary = "城市详情")
  @GetMapping("{id}")
  @SneakyThrows
  public City getCity(@PathVariable Long id) {

    return cityService.getCity(id);
  }

  @Operation(summary = "新增城市")
  @PostMapping
  @SneakyThrows
  public City newCity(@RequestBody City city) {

    return cityService.newCity(city);
  }

  @Operation(summary = "新增城市")
  @PutMapping("{id}")
  @SneakyThrows
  public City updateCity(@PathVariable Long id, @RequestParam String name) {

    return cityService.updateCity(id, name);
  }

  @Operation(summary = "删除城市")
  @DeleteMapping("{id}")
  @SneakyThrows
  public void deleteCity(@PathVariable Long id) {

    cityService.deleteCity(id);
  }
}
