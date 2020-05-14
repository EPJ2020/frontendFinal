package com.example.lfg_source.main.swipe;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class GroupSwipeViewModel<GroupSuggestion> extends androidx.lifecycle.ViewModel {

  private MutableLiveData<List<GroupSuggestion>> data;
  private String token;

  public void setData(List<GroupSuggestion> data) {
    this.data.setValue(data);
  }

  public void setToken(String token) {
    this.token = token;
  }

  public MutableLiveData<List<GroupSuggestion>> getData() {
    if (data == null) {
      data = new MutableLiveData<>();
    }
    return data;
  }

  private int userId;

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void sendMessage() {
    final String url = "http://152.96.56.38:8080/User/Suggestions";
    RestClientGroupSwipe task = new RestClientGroupSwipe(this, token);
    task.execute(url);
  }
}
