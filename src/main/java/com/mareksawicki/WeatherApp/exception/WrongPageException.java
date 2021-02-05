package com.mareksawicki.WeatherApp.exception;

public class WrongPageException extends RuntimeException{

  public WrongPageException(String message) {
    super(message);
  }

}
