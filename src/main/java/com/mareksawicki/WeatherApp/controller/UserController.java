package com.mareksawicki.WeatherApp.controller;

import com.mareksawicki.WeatherApp.config.jwt.JwtUtils;
import com.mareksawicki.WeatherApp.entity.User;
import com.mareksawicki.WeatherApp.model.JwtResponse;
import com.mareksawicki.WeatherApp.model.LoginRequest;
import com.mareksawicki.WeatherApp.model.UpdateRequest;
import com.mareksawicki.WeatherApp.repository.UserRepository;
import com.mareksawicki.WeatherApp.service.UserDetailsImpl;
import com.mareksawicki.WeatherApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@CrossOrigin(origins = "https://weather-broadcast-app.herokuapp.com")
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  public UserController(UserService userService, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
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

  @PutMapping("/update")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateRequest updateRequest) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        updateRequest.getLoginRequest().getUsername(), updateRequest.getLoginRequest().getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    User updatedUser = userService.updateUserByUsername(updateRequest.getLoginRequest().getUsername(), updateRequest.getUser());

    String jwt = jwtUtils.generateJwtToken(updatedUser.getUsername());

    List<String> roles = List.of(updatedUser.getRole());

    return ResponseEntity.ok(new JwtResponse(jwt, updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(), roles));
  }

  @PutMapping("/admin-update")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> updateUserByAdmin(@RequestBody User user) {
    User updatedUser = userRepository.findByUsername(user.getUsername());
    updatedUser.setRole(user.getRole());
    updatedUser.setEnabled(user.getEnabled());
    userService.updateUser(updatedUser);
    updatedUser.setPassword(null);

    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> removeUser(@PathVariable Long id) {
    return userService.removeUserById(id) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
  }

}
