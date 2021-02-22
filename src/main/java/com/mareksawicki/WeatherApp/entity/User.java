package com.mareksawicki.WeatherApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

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

  @Size(min = 4,max = 20,message = "Username length must be between 4 and 20.")
  @Pattern(regexp = "^(?![_ -])(?:(?![_ -]{2})[\\w -]){4,20}(?<![_ -])$", message = "Username can only consist of letters, numbers, dashes and underscores, cannot start and end with dashes or underscores.")
  @Column(nullable = false,unique = true)
  private String username;

  @Size(min = 4,message = "Password length must be min 4")
  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-.,_]).{8,}$", message = "Password must contain at least one upper and lower case letter, one digit, one special character and be minimum 8 length.")
  @Column(nullable = false)
  private String password;

  @Email(message = "An example email address is example@domain.com")
  @Size(min = 1,message = "Email field cannot be empty")
  @Column(nullable = false,unique = true)
  private String email;

  @Column(nullable = false)
  private String role;

  @Column(columnDefinition = "boolean not null default false")
  private Boolean enabled;

  @ManyToMany
  private Set<Forecast> forecasts;

}
