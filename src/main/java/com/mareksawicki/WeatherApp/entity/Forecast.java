package com.mareksawicki.WeatherApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

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

  @ManyToMany(fetch = FetchType.EAGER)
  @JsonIgnore
  private Set<User> users;

  @Override
  public String toString() {
    return "Forecast{" +
      "id=" + id +
      ", weatherForecast=" + weatherForecast +
      ", localization='" + localization + '\'' +
      ", forecastDate=" + forecastDate +
      ", forecastAcquiredDate=" + forecastAcquiredDate +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Forecast forecast = (Forecast) o;
    return id == forecast.id && weatherForecast.equals(forecast.weatherForecast) && localization.equals(forecast.localization) && forecastDate.equals(forecast.forecastDate) && forecastAcquiredDate.equals(forecast.forecastAcquiredDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, weatherForecast, localization, forecastDate, forecastAcquiredDate);
  }
}
