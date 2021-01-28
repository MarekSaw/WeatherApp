package com.mareksawicki.WeatherApp.model.weatherbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

}
