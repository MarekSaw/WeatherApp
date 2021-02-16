package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.entity.User;
import com.mareksawicki.WeatherApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<?> getUser(@RequestParam(required = false) String username) {
    return Objects.nonNull(username) ? ResponseEntity.ok(userService.getUserByUsername(username)) : ResponseEntity.ok(userService.getAllUsers());
  }

  @PostMapping
  public ResponseEntity<?> createNewUser(@RequestParam User user) {
    User createdUser = userService.createNewUser(user);
    if(Objects.isNull(createdUser)) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping
  public ResponseEntity<?> updateUser(@RequestParam String username,
                                      @RequestParam User user) {
    return ResponseEntity.ok(userService.updateUserByUsername(username, user));
  }

  @DeleteMapping
  public ResponseEntity<?> removeUser(@RequestParam String username) {
    return userService.removeUserByUsername(username) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
  }

}
