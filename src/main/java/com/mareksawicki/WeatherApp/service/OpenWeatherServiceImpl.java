package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.Forecast;
import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.enums.WeatherSource;
import com.mareksawicki.WeatherApp.exception.ForecastNotFoundException;
import com.mareksawicki.WeatherApp.model.openweather.Coordinates;
import com.mareksawicki.WeatherApp.model.openweather.OpenWeatherCoordinates;
import com.mareksawicki.WeatherApp.model.openweather.OpenWeatherForecast;
import com.mareksawicki.WeatherApp.repository.ForecastRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service("openWeatherService")
public class OpenWeatherServiceImpl implements WeatherService {

  private final static String URI_PATTERN_WEATHER = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
  private final static String URI_PATTERN_DAILY = "https://api.openweathermap.org/data/2.5/onecall?lat=%f&lon=%f&exclude=minutely,hourly&units=metric&appid=%s";
  private final static WeatherSource OPEN_WEATHER = WeatherSource.OPEN_WEATHER;
  private final static LocalDate TOMORROW = LocalDate.now().plusDays(1);
  private final static LocalDateTime TODAY_MIDNIGHT = LocalDate.now().atStartOfDay();
  private final RestTemplate restTemplate;
  private final ForecastRepository forecastRepository;

  @Value("${openweather.apikey}")
  private String openWeatherApikey;
  private Forecast previousForecast;
  private WeatherForecast weatherForecast;

  public OpenWeatherServiceImpl(RestTemplate restTemplate, ForecastRepository forecastRepository) {
    this.restTemplate = restTemplate;
    this.forecastRepository = forecastRepository;
  }

  @Override
  public WeatherForecast getForecast(String city) {
    return getForecast(city, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(String city, LocalDate date) {
    previousForecast = forecastRepository.findBySourceAndLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(OPEN_WEATHER, city, date, TODAY_MIDNIGHT);
    if (previousForecast != null) {
      System.out.println("Returning cached forecast!");
      return previousForecast.getWeatherForecast();
    }
    Coordinates coordinates = getCoordinates(city);
    String uri = String.format(URI_PATTERN_DAILY, coordinates.getLatitude(), coordinates.getLongitude(), openWeatherApikey);
    weatherForecast = getForecastForDate(uri, date);
    forecastRepository.save(Forecast.builder()
      .weatherForecast(weatherForecast)
      .source(OPEN_WEATHER)
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
    previousForecast = forecastRepository.findBySourceAndLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(OPEN_WEATHER, String.format("%f;%f;", lat, lon), date, TODAY_MIDNIGHT);
    if (previousForecast != null) {
      System.out.println("Returning cached forecast!");
      return previousForecast.getWeatherForecast();
    }
    String uri = String.format(URI_PATTERN_DAILY, lat, lon, openWeatherApikey);
    weatherForecast = getForecastForDate(uri, date);
    forecastRepository.save(Forecast.builder()
      .weatherForecast(weatherForecast)
      .source(OPEN_WEATHER)
      .localization(String.format("%f;%f;", lat, lon))
      .forecastDate(date)
      .forecastAcquiredDate(LocalDateTime.now())
      .build());
    return weatherForecast;
  }

  protected WeatherForecast getForecastForDate(String uri, LocalDate date) {
    OpenWeatherForecast openWeatherForecast = restTemplate.getForObject(uri, OpenWeatherForecast.class);
    if (openWeatherForecast != null) {
      return openWeatherForecast.getDailyForecast().stream()
        .filter(d -> LocalDate.ofEpochDay(d.getDt() / (3600 * 24)).equals(date))
        .findFirst()
        .orElseThrow(() -> new ForecastNotFoundException("There is no forecast for given date"))
        .toWeatherForecast();
    } else {
      throw new ForecastNotFoundException("Could not find forecast for given location");
    }
  }

  protected Coordinates getCoordinates(String city) {
    String uri = String.format(URI_PATTERN_WEATHER, city, openWeatherApikey);
    OpenWeatherCoordinates coordinates = restTemplate.getForObject(uri, OpenWeatherCoordinates.class);
    if (coordinates != null) {
      return coordinates.getCoordinates();
    } else {
      throw new ForecastNotFoundException("Could not find coordinates for given city");
    }
  }

}
