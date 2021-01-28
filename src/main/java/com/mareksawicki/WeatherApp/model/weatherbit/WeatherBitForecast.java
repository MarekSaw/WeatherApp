package com.mareksawicki.WeatherApp.model.weatherbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WeatherBitForecast {

  @JsonProperty("data")
  private List<WeatherBitDaily> dailyForecast;
}
