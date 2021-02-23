package com.mareksawicki.WeatherApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;
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

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
  @JsonIgnore
  private Set<Forecast> forecasts;


  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      ", password='" + password + '\'' +
      ", email='" + email + '\'' +
      ", role='" + role + '\'' +
      ", enabled=" + enabled +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id.equals(user.id) && username.equals(user.username) && password.equals(user.password) && email.equals(user.email) && role.equals(user.role) && enabled.equals(user.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, email, role, enabled);
  }
}
