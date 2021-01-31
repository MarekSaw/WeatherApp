package com.mareksawicki.WeatherApp.model.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Coordinates {

  @JsonProperty("lon")
  private double longitude;
  @JsonProperty("lat")
  private double latitude;
  @JsonProperty("name")
  private String cityName;

}
