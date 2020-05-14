package com.example.lfg_source.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupSuggestion {
  @JsonProperty("object")
  private Group group;

  @JsonProperty("percentage")
  private int percent;

  public int getPercent() {
    return percent;
  }

  public void setPercent(int precent) {
    this.percent = precent;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }
}
