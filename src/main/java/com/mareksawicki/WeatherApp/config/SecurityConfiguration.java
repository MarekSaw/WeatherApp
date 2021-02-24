package com.mareksawicki.WeatherApp.config;

import com.mareksawicki.WeatherApp.config.jwt.AuthEntryPointJwt;
import com.mareksawicki.WeatherApp.config.jwt.AuthTokenFilter;
import com.mareksawicki.WeatherApp.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final UserDetailsServiceImpl userService;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final AuthTokenFilter authenticationJwtTokenFilter;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public SecurityConfiguration(UserDetailsServiceImpl userService, AuthEntryPointJwt unauthorizedHandler, AuthTokenFilter authenticationJwtTokenFilter, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userService = userService;
    this.unauthorizedHandler = unauthorizedHandler;
    this.authenticationJwtTokenFilter = authenticationJwtTokenFilter;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .cors().and().csrf().disable()
      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
      .antMatchers("/weather-api/**","/user/**","/api/test/**","/**").permitAll()
      .anyRequest().authenticated();

    http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
  }

}
