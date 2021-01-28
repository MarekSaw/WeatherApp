package com.mareksawicki.WeatherApp.exception;

public class ForecastNotFoundException extends RuntimeException{

  public ForecastNotFoundException(String message) {
    super(message);
  }
}
