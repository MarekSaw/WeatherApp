package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.repository.ForecastRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompoundWeatherServiceImplTest {

  RestTemplate restTemplate = new RestTemplate();
  ForecastRepository forecastRepository;

  WeatherService weatherBit = new WeatherBitServiceImpl(restTemplate);
  WeatherService openWeather = new OpenWeatherServiceImpl(restTemplate);

  CompoundWeatherServiceImpl weatherService = new CompoundWeatherServiceImpl(weatherBit, openWeather, forecastRepository);

  @Test
  void shouldReturnAveragedParameters() {
    WeatherForecast weatherForecast1 = WeatherForecast.builder()
      .temperature(10.0)
      .humidity(50.0)
      .pressure(1000.0)
      .windSpeed(5.0)
      .windDeg(180.0)
      .build();
    WeatherForecast weatherForecast2 = WeatherForecast.builder()
      .temperature(20.0)
      .humidity(70.0)
      .pressure(980.0)
      .windSpeed(5.0)
      .windDeg(200.0)
      .build();
    List<WeatherForecast> weatherForecasts = List.of(weatherForecast1, weatherForecast2);

    WeatherForecast weatherForecastShouldResult = WeatherForecast.builder()
      .temperature(15.0)
      .humidity(60.0)
      .pressure(990.0)
      .windSpeed(5.0)
      .windDeg(190.0)
      .build();

    WeatherForecast weatherForecastResult = weatherService.getAverage(weatherForecasts);
    assertEquals(weatherForecastShouldResult, weatherForecastResult);
  }

}
