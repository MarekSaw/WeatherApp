package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;

import java.time.LocalDate;

public interface WeatherService {

  WeatherForecast getForecast(String city);
  WeatherForecast getForecast(String city, LocalDate date);
  WeatherForecast getForecast(double lat, double lon);
  WeatherForecast getForecast(double lat, double lon, LocalDate date);

}
