package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.exception.ForecastNotFoundException;
import com.mareksawicki.WeatherApp.exception.UserAlreadyExistsException;
import com.mareksawicki.WeatherApp.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UsernameNotFoundException.class)
  public Error usernameNotFoundExceptionHandler(UsernameNotFoundException usernameNotFoundException) {
    return new Error(usernameNotFoundException.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserAlreadyExistsException.class)
  public Error userAlreadyExistsExceptionHandler(UserAlreadyExistsException userAlreadyExistsException) {
    return new Error(userAlreadyExistsException.getMessage());
  }

}
