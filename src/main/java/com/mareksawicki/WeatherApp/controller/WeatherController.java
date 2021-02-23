package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.exception.ForecastNotFoundException;
import com.mareksawicki.WeatherApp.service.CompoundWeatherService;
import com.mareksawicki.WeatherApp.service.ForecastService;
import com.mareksawicki.WeatherApp.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/weather-api")
public class WeatherController {

  private final CompoundWeatherService weatherService;
  private final ForecastService forecastService;
  private final UserService userService;

  public WeatherController(@Qualifier("compoundWeatherService") CompoundWeatherService weatherService, ForecastService forecastService, UserService userService) {
    this.weatherService = weatherService;
    this.forecastService = forecastService;
    this.userService = userService;
  }

  @GetMapping("/forecast")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getAllForecasts(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
    if (Objects.isNull(page) && Objects.isNull(size)) {
      return ResponseEntity.ok(forecastService.getAllForecasts());
    }
    return ResponseEntity.ok(forecastService.getAllForecasts(page, size));
  }

  @GetMapping("/forecast/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<?> getAllForecastsForUser(@PathVariable Long id) {
    return ResponseEntity.ok(forecastService.getAllForecastsByUserId(id));
  }

  @GetMapping("/weather-forecast-standard")
  public ResponseEntity<?> getForecastsStandard(@RequestParam(required = false) String city,
                                             @RequestParam(required = false) Double lat,
                                             @RequestParam(required = false) Double lon) {
    WeatherForecast weatherForecast;
    if (Objects.nonNull(city)) {
      weatherForecast = weatherService.getForecast(null, city);
    } else if (Objects.nonNull(lat) && Objects.nonNull(lon)) {
      weatherForecast = weatherService.getForecast(null, lat, lon);
    } else {
      throw new ForecastNotFoundException("One of required parameters was not given");
    }
    return ResponseEntity.ok(weatherForecast);
  }

  @GetMapping("/weather-forecast/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<?> getWeatherForecast(@PathVariable Long id,
                                              @RequestParam(required = false) String city,
                                              @RequestParam(required = false) Double lat,
                                              @RequestParam(required = false) Double lon,
                                              @RequestParam(required = false) String date) {
    WeatherForecast weatherForecast;
    if (Objects.nonNull(city)) {
      weatherForecast = Objects.nonNull(date) ? weatherService.getForecast(id, city, LocalDate.parse(date)) : weatherService.getForecast(id, city);
    } else if (Objects.nonNull(lat) && Objects.nonNull(lon)) {
      weatherForecast = Objects.nonNull(date) ? weatherService.getForecast(id, lat, lon, LocalDate.parse(date)) : weatherService.getForecast(id, lat, lon);
    } else {
      throw new ForecastNotFoundException("One of required parameters was not given");
    }
    return ResponseEntity.ok(weatherForecast);
  }

  @DeleteMapping("/forecast/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<?> deleteForecast(@PathVariable Long id) {
    return forecastService.removeForecastById(id) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
  }

}
