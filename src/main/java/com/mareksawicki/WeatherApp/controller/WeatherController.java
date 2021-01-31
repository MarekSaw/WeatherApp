package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;


@RestController
@RequestMapping("/weather-api/forecast")
public class WeatherController {

  private final WeatherService weatherService;

  public WeatherController(@Qualifier("compoundWeatherService") WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @GetMapping
  public ResponseEntity<?> getForecastForCity(@RequestParam(required = false) String city,
                                              @RequestParam(required = false) Double lat,
                                              @RequestParam(required = false) Double lon,
                                              @RequestParam(required = false) String date) {
    WeatherForecast weatherForecast;
    if (Objects.nonNull(city)) {
      weatherForecast = Objects.nonNull(date) ? weatherService.getForecast(city, LocalDate.parse(date)) : weatherService.getForecast(city);
    } else if (Objects.nonNull(lat) && Objects.nonNull(lon)) {
      weatherForecast = Objects.nonNull(date) ? weatherService.getForecast(lat, lon, LocalDate.parse(date)) : weatherService.getForecast(lat, lon);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.ok(weatherForecast);
  }

}
