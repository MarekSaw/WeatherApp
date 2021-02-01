package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service("compoundWeatherService")
public class CompoundWeatherServiceImpl implements WeatherService {

  private final static LocalDate TOMORROW = LocalDate.now().plusDays(1);
  private final List<WeatherService> serviceList;

  public CompoundWeatherServiceImpl(@Qualifier("weatherBitService") WeatherService weatherBit, @Qualifier("openWeatherService") WeatherService openWeather) {
    this.serviceList = List.of(weatherBit, openWeather);
  }

  @Override
  public WeatherForecast getForecast(String city) {
    return getForecast(city, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(String city, LocalDate date) {
    return getAverage(serviceList.stream()
      .map(weatherService -> weatherService.getForecast(city, date))
      .collect(Collectors.toList()));
  }

  @Override
  public WeatherForecast getForecast(double lat, double lon) {
    return getForecast(lat, lon, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(double lat, double lon, LocalDate date) {
    return getAverage(serviceList.stream()
    .map(weatherService -> weatherService.getForecast(lat, lon, date))
    .collect(Collectors.toList()));
  }

  protected WeatherForecast getAverage(List<WeatherForecast> list) {
    return WeatherForecast.builder()
      .temperature(list.stream().map(WeatherForecast::getTemperature).reduce(0.0, Double::sum) / list.size())
      .pressure(list.stream().map(WeatherForecast::getPressure).reduce(0.0, Double::sum) / list.size())
      .humidity(list.stream().map(WeatherForecast::getHumidity).reduce(0.0, Double::sum) / list.size())
      .windSpeed(list.stream().map(WeatherForecast::getWindSpeed).reduce(0.0, Double::sum) / list.size())
      .windDeg(list.stream().map(WeatherForecast::getWindDeg).reduce(0.0, Double::sum) / list.size())
      .build();
  }
}
