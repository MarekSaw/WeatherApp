package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.Forecast;
import com.mareksawicki.WeatherApp.exception.WrongPageException;
import com.mareksawicki.WeatherApp.repository.ForecastRepository;
import com.mareksawicki.WeatherApp.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ForecastServiceImpl implements ForecastService {

  private final ForecastRepository forecastRepository;
  private final UserRepository userRepository;

  public ForecastServiceImpl(ForecastRepository forecastRepository, UserRepository userRepository) {
    this.forecastRepository = forecastRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<Forecast> getAllForecasts() {
    return forecastRepository.findAll();
  }

  @Override
  public List<Forecast> getAllForecasts(Integer page, Integer size) {
    if (!Objects.nonNull(page)) {
      page = 1;
    }
    if (!Objects.nonNull(size)) {
      size = 10;
    }
    if (page <=0 || size <=0) {
      throw new WrongPageException("Page and size value cannot be less than 1!");
    }
    Sort sort = Sort.by("forecastDate").ascending();
    Pageable pageable = PageRequest.of(page -1, size, sort);
    return forecastRepository.findAll(pageable).getContent();
  }

  @Override
  public List<Forecast> getAllForecastsByUserId(Long userId) {
    return forecastRepository.findAllByUsersContaining(userRepository.findById(userId).orElseThrow());
  }

  @Override
  public boolean removeForecastById(Long id) {
    if (forecastRepository.existsById(id)) {
      forecastRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
