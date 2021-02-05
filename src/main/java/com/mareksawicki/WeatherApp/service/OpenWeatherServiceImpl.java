package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import com.mareksawicki.WeatherApp.exception.ForecastNotFoundException;
import com.mareksawicki.WeatherApp.model.openweather.Coordinates;
import com.mareksawicki.WeatherApp.model.openweather.OpenWeatherCoordinates;
import com.mareksawicki.WeatherApp.model.openweather.OpenWeatherForecast;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service("openWeatherService")
public class OpenWeatherServiceImpl implements WeatherService {

  private final static String URI_PATTERN_WEATHER = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
  private final static String URI_PATTERN_DAILY = "https://api.openweathermap.org/data/2.5/onecall?lat=%f&lon=%f&exclude=minutely,hourly&units=metric&appid=%s";
  private final static LocalDate TOMORROW = LocalDate.now().plusDays(1);
  private final RestTemplate restTemplate;

  @Value("${openweather.apikey}")
  private String openWeatherApikey;
  private WeatherForecast weatherForecast;

  public OpenWeatherServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public WeatherForecast getForecast(String city) {
    return getForecast(city, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(String city, LocalDate date) {
    Coordinates coordinates = getCoordinates(city);
    String uri = String.format(URI_PATTERN_DAILY, coordinates.getLatitude(), coordinates.getLongitude(), openWeatherApikey);
    weatherForecast = getForecastForDate(uri, date);
    return weatherForecast;
  }

  @Override
  public WeatherForecast getForecast(double lat, double lon) {
    return getForecast(lat, lon, TOMORROW);
  }

  @Override
  public WeatherForecast getForecast(double lat, double lon, LocalDate date) {
    String uri = String.format(URI_PATTERN_DAILY, lat, lon, openWeatherApikey);
    weatherForecast = getForecastForDate(uri, date);
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
