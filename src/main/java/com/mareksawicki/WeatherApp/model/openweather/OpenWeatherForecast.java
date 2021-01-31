package com.mareksawicki.WeatherApp.model.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenWeatherForecast {

  @JsonProperty("daily")
  private List<OpenWeatherDaily> dailyForecast;

}
