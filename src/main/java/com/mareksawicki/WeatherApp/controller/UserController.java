package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.config.jwt.JwtUtils;
import com.mareksawicki.WeatherApp.entity.User;
import com.mareksawicki.WeatherApp.exception.UserAlreadyExistsException;
import com.mareksawicki.WeatherApp.model.JwtResponse;
import com.mareksawicki.WeatherApp.model.LoginRequest;
import com.mareksawicki.WeatherApp.model.MessageResponse;
import com.mareksawicki.WeatherApp.service.UserDetailsImpl;
import com.mareksawicki.WeatherApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @GetMapping
  public ResponseEntity<?> getUser(@RequestParam(required = false) String username) {
    return Objects.nonNull(username) ? ResponseEntity.ok(userService.getUserByUsername(username)) : ResponseEntity.ok(userService.getAllUsers());
  }

  @PostMapping("/register")
  public ResponseEntity<?> createNewUser(@Valid @RequestBody User user) {
    User createdUser = userService.createNewUser(user);
    createdUser.setPassword(null);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
  }

  @PutMapping
  public ResponseEntity<?> updateUser(@RequestParam String username,
                                      @RequestBody User user) {
    User updatedUser = userService.updateUserByUsername(username, user);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping
  public ResponseEntity<?> removeUser(@RequestParam String username) {
    return userService.removeUserByUsername(username) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
  }

}
