package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.User;

import java.util.List;

public interface UserService {

  User createNewUser(User user);
  List<User> getAllUsers();
  User getUserByUsername(String username);
  boolean userExistsByUsername(String username);
  User updateUserByUsername(String username, User user);
  boolean removeUserByUsername(String username);

}
