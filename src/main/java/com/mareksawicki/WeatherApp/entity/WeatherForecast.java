package com.mareksawicki.WeatherApp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherForecast {

  @Id
  @GeneratedValue
  private Long id;
  private Double temperature;
  private Double pressure;
  private Double humidity;
  @Column(name = "wind_speed")
  private Double windSpeed;
  @Column(name = "wind_deg")
  private Double windDeg;

  @OneToOne(fetch = FetchType.LAZY)
  @Getter(value = AccessLevel.NONE)
  private Forecast forecast;

}
