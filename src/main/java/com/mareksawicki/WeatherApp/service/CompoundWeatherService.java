package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;

import java.time.LocalDate;

public interface CompoundWeatherService {

  WeatherForecast getForecast(Long userId, String city);
  WeatherForecast getForecast(Long userId, String city, LocalDate date);
  WeatherForecast getForecast(Long userId, double lat, double lon);
  WeatherForecast getForecast(Long userId, double lat, double lon, LocalDate date);

}
