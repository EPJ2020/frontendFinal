package com.example.lfg_source.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Group {
  @JsonProperty("groupId")
  private int groupId;

  @JsonProperty("ownerId")
  private int ownerId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("tags")
  private ArrayList<String> tags;

  @JsonProperty("active")
  private boolean active;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phoneNumber")
  private String phoneNumber;

  private String location;

  public Group() {}

  public Group(int adminId) {
    ownerId = adminId;
    groupId = 0;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public int getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getLocation() { return location; }

  public void setLocation(String location) { this.location = location; }

  public void changeAttributes(
      String description,
      boolean active,
      String groupName,
      String phoneNumber,
      String email,
      ArrayList<String> tags) {
    this.active = active;
    this.description = description;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.name = groupName;
    this.tags = tags;
  }
}
