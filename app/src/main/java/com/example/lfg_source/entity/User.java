package com.example.lfg_source.entity;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
  @JsonProperty("userId")
  private Integer id;

  @JsonProperty("lastName")
  private String lastName;

  @JsonProperty("firstName")
  private String firstName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phoneNumber")
  private String phone;

  @JsonProperty("description")
  private String description;

  @JsonProperty("tags")
  private ArrayList<String> tags;

  @JsonProperty("active")
  private boolean active;

  @JsonProperty("sex")
  private String gender;

  @JsonProperty("age")
  private String age;

  public User() {
    this.id = 0;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String name) {
    this.lastName = name;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ArrayList<String> getTags() {
    return tags;
  }

  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @NonNull
  @Override
  public String toString() {
    return firstName;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void changeAttributes(
      String description,
      boolean active,
      String firstName,
      String phone,
      String lastName,
      String email,
      ArrayList<String> tags) {
    this.description = description;
    this.active = active;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.tags = tags;
  }
}
