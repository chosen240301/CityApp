package com.tco.city.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {
  String name;
  List<String> roles;
}
