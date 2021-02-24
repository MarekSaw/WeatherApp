package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.Forecast;
import com.mareksawicki.WeatherApp.entity.User;
import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.repository.ForecastRepository;
import com.mareksawicki.WeatherApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service("compoundWeatherService")
public class CompoundWeatherServiceImpl implements CompoundWeatherService {

  private final static LocalDateTime TODAY_MIDNIGHT = LocalDate.now().atStartOfDay();
  private final static LocalDate TOMORROW = LocalDate.now().plusDays(1);
  private final List<WeatherService> serviceList;
  private final ForecastRepository forecastRepository;
  private final UserRepository userRepository;
  private Forecast previousForecast;
  private WeatherForecast weatherForecast;

  public CompoundWeatherServiceImpl(@Qualifier("weatherBitService") WeatherService weatherBit, @Qualifier("openWeatherService") WeatherService openWeather, ForecastRepository forecastRepository, UserRepository userRepository) {
    this.forecastRepository = forecastRepository;
    this.userRepository = userRepository;
    this.serviceList = List.of(weatherBit, openWeather);
  }

  @Override
  public WeatherForecast getForecast(Long userId, String city) {
    return getForecast(userId, city, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(Long userId, String city, LocalDate date) {
    previousForecast = forecastRepository.findByLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(city, date, TODAY_MIDNIGHT);
    if (Objects.nonNull(previousForecast)) {
      return checkIfForecastContainsUserAddIfNot(previousForecast, userId);
    }
    weatherForecast = getAverage(serviceList.stream()
      .map(weatherService -> weatherService.getForecast(city, date))
      .collect(Collectors.toList()));
    if (Objects.nonNull(userId)) {
      forecastRepository.save(Forecast.builder()
        .weatherForecast(weatherForecast)
        .localization(city)
        .forecastDate(date)
        .forecastAcquiredDate(LocalDateTime.now())
        .users(Set.of(userRepository.findById(userId).orElseThrow()))
        .build());
    } else {
      forecastRepository.save(Forecast.builder()
        .weatherForecast(weatherForecast)
        .localization(city)
        .forecastDate(date)
        .forecastAcquiredDate(LocalDateTime.now())
        .build());
    }
    return weatherForecast;
  }

  @Override
  public WeatherForecast getForecast(Long userId, double lat, double lon) {
    return getForecast(userId, lat, lon, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(Long userId, double lat, double lon, LocalDate date) {
    previousForecast = forecastRepository.findByLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(String.format("%f;%f;", lat, lon), date, TODAY_MIDNIGHT);
    if (Objects.nonNull(previousForecast)) {
      return checkIfForecastContainsUserAddIfNot(previousForecast, userId);
    }
    weatherForecast = getAverage(serviceList.stream()
      .map(weatherService -> weatherService.getForecast(lat, lon, date))
      .collect(Collectors.toList()));
    if (Objects.nonNull(userId)) {
      forecastRepository.save(Forecast.builder()
        .weatherForecast(weatherForecast)
        .localization(String.format("%f;%f;", lat, lon))
        .forecastDate(date)
        .forecastAcquiredDate(LocalDateTime.now())
        .users(Set.of(userRepository.findById(userId).orElseThrow()))
        .build());
    } else {
      forecastRepository.save(Forecast.builder()
        .weatherForecast(weatherForecast)
        .localization(String.format("%f;%f;", lat, lon))
        .forecastDate(date)
        .forecastAcquiredDate(LocalDateTime.now())
        .build());
    }
    return weatherForecast;
  }

  private WeatherForecast checkIfForecastContainsUserAddIfNot(Forecast previousForecast, Long userId) {
    System.out.println("Returning cached forecast!");
    if (Objects.nonNull(userId)) {
      User user = userRepository.findById(userId).orElse(null);
      if (!previousForecast.getUsers().contains(user)) {
        Set<User> users = previousForecast.getUsers();
        users.add(user);
        previousForecast.setUsers(users);
        forecastRepository.save(previousForecast);
      }
    }
    return previousForecast.getWeatherForecast();
  }

  private WeatherForecast getAverage(List<WeatherForecast> list) {
    return WeatherForecast.builder()
      .temperature(list.stream().map(WeatherForecast::getTemperature).reduce(0.0, Double::sum) / list.size())
      .pressure(list.stream().map(WeatherForecast::getPressure).reduce(0.0, Double::sum) / list.size())
      .humidity(list.stream().map(WeatherForecast::getHumidity).reduce(0.0, Double::sum) / list.size())
      .windSpeed(list.stream().map(WeatherForecast::getWindSpeed).reduce(0.0, Double::sum) / list.size())
      .windDeg(list.stream().map(WeatherForecast::getWindDeg).reduce(0.0, Double::sum) / list.size())
      .build();
  }
}
