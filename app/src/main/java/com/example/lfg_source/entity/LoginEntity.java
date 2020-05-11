package com.example.lfg_source.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginEntity {
  @JsonProperty("username")
  private String username;

  @JsonProperty("password")
  private String password;

  public LoginEntity(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
