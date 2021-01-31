package com.mareksawicki.WeatherApp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mareksawicki.WeatherApp.enums.WeatherSource;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Configuration
public class BeanConfiguration {

  @Bean(name = "WEATHER_BIT")
  public WeatherSource getWeatherBit() {
    return WeatherSource.WEATHER_BIT;
  }

  @Bean(name = "OPEN_WEATHER")
  public WeatherSource getOpenWeather() {
    return WeatherSource.OPEN_WEATHER;
  }

  @Bean(name = "TOMORROW")
  public LocalDate getTomorrowDate() {
    return LocalDate.now().plusDays(1);
  }

  @Bean(name = "TODAY_MIDNIGHT")
  public LocalDateTime getTodayMidnight() {
    return LocalDate.now().atStartOfDay();
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

}
