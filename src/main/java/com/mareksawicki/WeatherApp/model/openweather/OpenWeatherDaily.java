package com.mareksawicki.WeatherApp.model.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenWeatherDaily {

  private Integer dt;
  @JsonProperty("temp")
  private OpenWeatherTemp openWeatherTemp;
  private Double pressure;
  private Double humidity;
  @JsonProperty("wind_speed")
  private Double windSpeed;
  @JsonProperty("wind_deg")
  private Double windDeg;

  public WeatherForecast toWeatherForecast() {
    return WeatherForecast.builder()
      .temperature((openWeatherTemp.getMax() + openWeatherTemp.getMin()) / 2)
      .pressure(pressure)
      .humidity(humidity)
      .windSpeed(windSpeed)
      .windDeg(windDeg)
      .build();
  }
}
