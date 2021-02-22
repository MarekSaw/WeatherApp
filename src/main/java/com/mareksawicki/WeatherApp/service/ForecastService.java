package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.Forecast;

import java.util.List;

public interface ForecastService {
  List<Forecast> getAllForecasts();
  List<Forecast> getAllForecasts(Integer page, Integer size);
  List<Forecast> getAllForecastsByUserId(Long userId);
  boolean removeForecastById(Long id);
}
