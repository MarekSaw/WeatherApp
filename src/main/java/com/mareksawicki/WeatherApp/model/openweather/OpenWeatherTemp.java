package com.mareksawicki.WeatherApp.model.openweather;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenWeatherTemp {

  private Double min;
  private Double max;

}
