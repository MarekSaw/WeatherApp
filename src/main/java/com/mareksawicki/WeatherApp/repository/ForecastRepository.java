package com.mareksawicki.WeatherApp.repository;

import com.mareksawicki.WeatherApp.entity.Forecast;
import com.mareksawicki.WeatherApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

  Forecast findByLocalizationAndForecastDateAndForecastAcquiredDateIsAfter(String localization, LocalDate date, LocalDateTime todayMidnight);
  List<Forecast> findAllByUsersContaining(User user);

}
