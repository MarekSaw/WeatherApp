package com.mareksawicki.WeatherApp.repository;

import com.mareksawicki.WeatherApp.entity.Forecast;
import com.mareksawicki.WeatherApp.enums.WeatherSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {

  Forecast findBySourceAndLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(WeatherSource source, String localization, LocalDate date, LocalDateTime todayMidnight);

}
