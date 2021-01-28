package com.mareksawicki.WeatherApp.entity;

import com.mareksawicki.WeatherApp.enums.WeatherSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forecast {

  @Id
  @GeneratedValue
  private long id;
  @OneToOne(mappedBy = "forecast")
  private WeatherForecast weatherForecast;
  private WeatherSource source;
  private String localization;
  private LocalDate forecastDate;
  private LocalDateTime forecastAcquiredDate;

}
