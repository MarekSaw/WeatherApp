package com.mareksawicki.WeatherApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users") // name because postgresql has word user reserved
public class User {

  @Id
  @GeneratedValue
  @Column(nullable = false, updatable = false)
  private Long id;

  @Size(min = 4,max = 20,message = "Username length must be between 4 and 20")
  @Pattern(regexp = "[\\p{IsAlphabetic}0-9-_]+", message = "Username can only consist of letters, numbers, dashes and underscores")
  @Column(nullable = false,unique = true)
  private String username;

  @Size(min = 4,message = "Password length must be min 4")
  @Column(nullable = false)
  private String password;

  @Email(message = "An example email address is example@gmail.com")
  @Size(min = 1,message = "Email field cannot be empty")
  @Column(nullable = false,unique = true)
  private String email;

  @Column(nullable = false)
  private String role;

  @Column(columnDefinition = "boolean not null default false")
  private Boolean enabled;

}
