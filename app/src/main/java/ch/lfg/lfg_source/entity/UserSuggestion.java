package ch.lfg.lfg_source.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSuggestion {
  @JsonProperty("object")
  private User user;

  @JsonProperty("percentage")
  private int percent;

  public int getPercent() {
    return percent;
  }

  public void setPercent(int percent) {
    this.percent = percent;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
