package com.mareksawicki.WeatherApp.repository;

import com.mareksawicki.WeatherApp.entity.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {

  Forecast findByLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(String localization, LocalDate date, LocalDateTime todayMidnight);

}
