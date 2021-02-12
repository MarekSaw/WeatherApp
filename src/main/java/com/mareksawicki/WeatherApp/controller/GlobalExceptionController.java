package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.exception.ForecastNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionController {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ForecastNotFoundException.class)
  public Error forecastNotFoundExceptionHandler(ForecastNotFoundException forecastNotFoundException) {
    return new Error(forecastNotFoundException.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(HttpClientErrorException.class)
  public Error httpClientErrorExceptionHandler(HttpClientErrorException httpClientErrorException) {
    return new Error(httpClientErrorException.getMessage());
  }

}
