package com.mareksawicki.WeatherApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Forecast {

  @Id
  @GeneratedValue
  private long id;
  @OneToOne(cascade = CascadeType.ALL)
  private WeatherForecast weatherForecast;
  private String localization;
  private LocalDate forecastDate;
  private LocalDateTime forecastAcquiredDate;

}
