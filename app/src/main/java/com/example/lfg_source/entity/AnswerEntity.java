package com.example.lfg_source.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnswerEntity {
  @JsonProperty("groupId")
  private int groupId;

  @JsonProperty("userId")
  private int userId;

  @JsonProperty("answer")
  private Boolean answer;

  public AnswerEntity() {}

  public AnswerEntity(int groupId, int userId, Boolean answer) {
    this.groupId = groupId;
    this.userId = userId;
    this.answer = answer;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setAnswer(Boolean answer) {
    this.answer = answer;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Boolean getAnswer() {
    return answer;
  }
}
