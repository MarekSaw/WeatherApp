package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.Forecast;
import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.enums.WeatherSource;
import com.mareksawicki.WeatherApp.exception.ForecastNotFoundException;
import com.mareksawicki.WeatherApp.model.weatherbit.WeatherBitForecast;
import com.mareksawicki.WeatherApp.repository.ForecastRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service("weatherBitService")
public class WeatherBitServiceImpl implements WeatherService {
  private final static String URI_PATTERN_WEATHER_CITY = "https://api.weatherbit.io/v2.0/forecast/daily?key=%s&city=%s&units=M";
  private final static String URI_PATTERN_WEATHER_COORDINATES = "https://api.weatherbit.io/v2.0/forecast/daily?key=%s&units=M&lat=%f&lon=%f";
  private final static WeatherSource WEATHER_BIT = WeatherSource.WEATHER_BIT;
  private final static LocalDate TOMORROW = LocalDate.now().plusDays(1);
  private final static LocalDateTime TODAY_MIDNIGHT = LocalDate.now().atStartOfDay();
  private final RestTemplate restTemplate;

  @Value("${weatherbit.apikey}")
  private String weatherBitApikey;
  private WeatherForecast weatherForecast;

  public WeatherBitServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public WeatherForecast getForecast(String city) {
    return getForecast(city, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(String city, LocalDate date) {
    String uri = String.format(URI_PATTERN_WEATHER_CITY, weatherBitApikey, city);
    weatherForecast = getForecastForDate(uri, date);
    return weatherForecast;
  }

  @Override
  public WeatherForecast getForecast(double lat, double lon) {
    return getForecast(lat, lon, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(double lat, double lon, LocalDate date) {
    String uri = String.format(URI_PATTERN_WEATHER_COORDINATES, weatherBitApikey, lat, lon).replace(',','.');
    weatherForecast = getForecastForDate(uri, date);
    return weatherForecast;
  }

  protected WeatherForecast getForecastForDate(String uri, LocalDate date) {
    WeatherBitForecast weatherBitForecast = restTemplate.getForObject(uri, WeatherBitForecast.class);
    if (weatherBitForecast != null) {
      return weatherBitForecast.getDailyForecast().stream()
        .filter(d -> LocalDate.parse(d.getDate()).equals(date))
        .findFirst()
        .orElseThrow(() -> new ForecastNotFoundException("There is no forecast for given date"))
        .toWeatherForecast();
    } else {
      throw new ForecastNotFoundException("Could not find forecast for given location");
    }
  }

}
