package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.service.ForecastService;
import com.mareksawicki.WeatherApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/weather-api/forecast")
public class WeatherController {

  private final WeatherService weatherService;
  private final ForecastService forecastService;

  public WeatherController(@Qualifier("compoundWeatherService") WeatherService weatherService, ForecastService forecastService) {
    this.weatherService = weatherService;
    this.forecastService = forecastService;
  }

  @GetMapping
  public ResponseEntity<?> getForecast(@RequestParam(required = false) String city,
                                              @RequestParam(required = false) Double lat,
                                              @RequestParam(required = false) Double lon,
                                              @RequestParam(required = false) String date,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {
    WeatherForecast weatherForecast;
    if (Objects.nonNull(city)) {
      weatherForecast = Objects.nonNull(date) ? weatherService.getForecast(city, LocalDate.parse(date)) : weatherService.getForecast(city);
    } else if (Objects.nonNull(lat) && Objects.nonNull(lon)) {
      weatherForecast = Objects.nonNull(date) ? weatherService.getForecast(lat, lon, LocalDate.parse(date)) : weatherService.getForecast(lat, lon);
    } else {
      return ResponseEntity.ok(forecastService.getAllForecasts(page, size));
    }
    return ResponseEntity.ok(weatherForecast);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteForecast(@PathVariable Long id) {
    return forecastService.removeForecastById(id) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
  }

}
