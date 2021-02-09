package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.Forecast;

import java.util.List;

public interface ForecastService {
  List<Forecast> getAllForecasts();
  List<Forecast> getAllForecasts(Integer page, Integer size);
  boolean removeForecastById(Long id);
  Long getRecordsCount();
}
