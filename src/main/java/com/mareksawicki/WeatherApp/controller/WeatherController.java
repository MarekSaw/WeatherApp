package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.exception.ForecastNotFoundException;
import com.mareksawicki.WeatherApp.service.ForecastService;
import com.mareksawicki.WeatherApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/weather-api")
public class WeatherController {

  private final WeatherService weatherService;
  private final ForecastService forecastService;

  public WeatherController(@Qualifier("compoundWeatherService") WeatherService weatherService, ForecastService forecastService) {
    this.weatherService = weatherService;
    this.forecastService = forecastService;
  }

  @GetMapping("/forecast")
  public ResponseEntity<?> getAllForecasts(@RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {
    return ResponseEntity.ok(forecastService.getAllForecasts(page, size));
  }

  @GetMapping("/weather-forecast")
  public ResponseEntity<?> getWeatherForecast(@RequestParam(required = false) String city,
                                              @RequestParam(required = false) Double lat,
                                              @RequestParam(required = false) Double lon,
                                              @RequestParam(required = false) String date) {
    WeatherForecast weatherForecast;
    if (Objects.nonNull(city)) {
      weatherForecast = Objects.nonNull(date) ? weatherService.getForecast(city, LocalDate.parse(date)) : weatherService.getForecast(city);
    } else if (Objects.nonNull(lat) && Objects.nonNull(lon)) {
      weatherForecast = Objects.nonNull(date) ? weatherService.getForecast(lat, lon, LocalDate.parse(date)) : weatherService.getForecast(lat, lon);
    } else {
      throw new ForecastNotFoundException("One of required parameters was not given");
    }
    return ResponseEntity.ok(weatherForecast);
  }

  @DeleteMapping("/forecast/{id}")
  public ResponseEntity<?> deleteForecast(@PathVariable Long id) {
    return forecastService.removeForecastById(id) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
  }

}
