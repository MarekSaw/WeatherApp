package com.mareksawicki.WeatherApp.service;

import com.mareksawicki.WeatherApp.entity.User;
import com.mareksawicki.WeatherApp.exception.UserAlreadyExistsException;
import com.mareksawicki.WeatherApp.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserDetailsServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if(Objects.isNull(user)){
      throw new UsernameNotFoundException("User with username " + username + " was not found");
    }
    return UserDetailsImpl.build(user);
  }

  @Override
  public User createNewUser(User user) {
    if(!userRepository.existsByUsername(user.getUsername()) && !userRepository.existsByEmail(user.getEmail())) {
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      if(Objects.isNull(user.getEnabled())) {
        user.setEnabled(true);
      }
      if(Objects.isNull(user.getRole())) {
        user.setRole("ROLE_USER");
      }
      userRepository.save(user);
      return user;
    }
    throw new UserAlreadyExistsException("User with given username or email already exists.");
  }

  @Override
  public List<User> getAllUsers() {
    List<User> users = userRepository.findAll();
    users.forEach(user -> user.setPassword(null));
    return users;
  }

  @Override
  public User getUserByUsername(String username) {
    if (userRepository.existsByUsername(username)){
      User user = userRepository.findByUsername(username);
      user.setPassword(null);
      return user;
    }
    throw new UsernameNotFoundException("User with given username does not exist.");
  }

  @Override
  public User updateUserByUsername(String username, User user) {
    if (!username.equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
      throw new UserAlreadyExistsException("User with given username already exists.");
    }
    if (!getUserByUsername(username).getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
      throw new UserAlreadyExistsException("Given email is already taken.");
    }
    User updatingUser = getUserByUsername(username);
    user.setId(updatingUser.getId());
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    if (Objects.isNull(user.getRole())) {
      user.setRole(updatingUser.getRole());
    }
    if (Objects.isNull(user.getEnabled())) {
      user.setEnabled(updatingUser.getEnabled());
    }
    return userRepository.save(user);
  }

  @Override
  public User updateUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public boolean removeUserById(Long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }


}
