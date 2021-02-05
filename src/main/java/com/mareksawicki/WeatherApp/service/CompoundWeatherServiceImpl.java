package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.Forecast;
import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.repository.ForecastRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("compoundWeatherService")
public class CompoundWeatherServiceImpl implements WeatherService {

  private final static LocalDateTime TODAY_MIDNIGHT = LocalDate.now().atStartOfDay();
  private final static LocalDate TOMORROW = LocalDate.now().plusDays(1);
  private final List<WeatherService> serviceList;
  private final ForecastRepository forecastRepository;
  private Forecast previousForecast;
  private WeatherForecast weatherForecast;

  public CompoundWeatherServiceImpl(@Qualifier("weatherBitService") WeatherService weatherBit, @Qualifier("openWeatherService") WeatherService openWeather, ForecastRepository forecastRepository) {
    this.forecastRepository = forecastRepository;
    this.serviceList = List.of(weatherBit, openWeather);
  }

  @Override
  public WeatherForecast getForecast(String city) {
    return getForecast(city, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(String city, LocalDate date) {
    previousForecast = forecastRepository.findByLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(city, date, TODAY_MIDNIGHT);
    if (Objects.nonNull(previousForecast)) {
      System.out.println("Returning cached forecast!");
      return previousForecast.getWeatherForecast();
    }
    weatherForecast = getAverage(serviceList.stream()
      .map(weatherService -> weatherService.getForecast(city, date))
      .collect(Collectors.toList()));
    forecastRepository.save(Forecast.builder()
      .weatherForecast(weatherForecast)
      .localization(city)
      .forecastDate(date)
      .forecastAcquiredDate(LocalDateTime.now())
      .build());
    return weatherForecast;
  }

  @Override
  public WeatherForecast getForecast(double lat, double lon) {
    return getForecast(lat, lon, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(double lat, double lon, LocalDate date) {
    previousForecast = forecastRepository.findByLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(String.format("%f;%f;", lat, lon), date, TODAY_MIDNIGHT);
    if (Objects.nonNull(previousForecast)) {
      System.out.println("Returning cached forecast!");
      return previousForecast.getWeatherForecast();
    }
    weatherForecast = getAverage(serviceList.stream()
      .map(weatherService -> weatherService.getForecast(lat, lon, date))
      .collect(Collectors.toList()));
    forecastRepository.save(Forecast.builder()
      .weatherForecast(weatherForecast)
      .localization(String.format("%f;%f;", lat, lon))
      .forecastDate(date)
      .forecastAcquiredDate(LocalDateTime.now())
      .build());
    return weatherForecast;
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
