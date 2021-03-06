package com.mareksawicki.WeatherApp.model.weatherbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mareksawicki.WeatherApp.entity.WeatherForecast;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WeatherBitDaily {

  @JsonProperty("temp")
  private Double temperature;
  @JsonProperty("pres")
  private Double pressure;
  @JsonProperty("rh")
  private Double humidity;
  @JsonProperty("wind_spd")
  private Double windSpeed;
  @JsonProperty("wind_dir")
  private Double windDeg;
  @JsonProperty("valid_date")
  private String date;

  public WeatherForecast toWeatherForecast() {
    return WeatherForecast.builder()
      .humidity(this.humidity)
      .pressure(this.pressure)
      .temperature(this.temperature)
      .windDeg(this.windDeg)
      .windSpeed(this.windSpeed)
      .build();
  }

}
