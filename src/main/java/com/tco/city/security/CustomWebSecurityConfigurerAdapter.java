package com.tco.city.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter {

  @Bean
  public UserDetailsService users() {
    UserDetails user = User.builder()
        .username("user")
        .password("$2a$12$TVTqP98lrfRzpYM1C8dVlewZSLzUqCQphvm1XbDRE9p5vYemGduPC")
        .roles("ALLOW_VIEW")
        .build();
    UserDetails admin = User.builder()
        .username("admin")
        .password("$2a$12$W1tCh.crSWLAUkGe47yPxO7EZ10AuMGnpr7fWGmA5a8VFczyM4aZm")
        .roles("ALLOW_VIEW", "ALLOW_EDIT")
        .build();
    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/api/images/**").permitAll()
        .antMatchers(HttpMethod.GET, "/api/**").hasAuthority("ROLE_ALLOW_VIEW")
        .antMatchers(HttpMethod.POST, "/api/**").hasAuthority("ROLE_ALLOW_EDIT")
        .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
        .anyRequest().authenticated()
        .and().httpBasic()
        .and().csrf().disable();;
    return http.build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
