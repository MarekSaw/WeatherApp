package com.mareksawicki.WeatherApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {

  @RequestMapping("/")
  public String angularStart() {
    return "index.html";
  }
}
