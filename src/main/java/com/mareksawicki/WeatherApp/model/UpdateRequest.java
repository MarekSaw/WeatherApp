package com.mareksawicki.WeatherApp.model;

import com.mareksawicki.WeatherApp.entity.User;
import lombok.Data;

@Data
public class UpdateRequest {
  private LoginRequest loginRequest;
  private User User;
}
