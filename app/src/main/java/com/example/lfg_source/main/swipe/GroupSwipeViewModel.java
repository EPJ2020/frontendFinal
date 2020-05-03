package com.example.lfg_source.main.swipe;

import androidx.lifecycle.MutableLiveData;

import com.example.lfg_source.entity.Group;
import com.example.lfg_source.rest.RestClientGroupSwipe;

import java.util.List;

public class GroupSwipeViewModel<Groups> extends SwipeViewModel {

  private MutableLiveData<List<Group>> data;

  public void setData(List<Group> data) {
    this.data.setValue(data);
  }

  public MutableLiveData<List<Group>> getData() {
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
    final String url = "http://152.96.56.38:8080/User/Suggestions/" + userId;
    RestClientGroupSwipe task = new RestClientGroupSwipe(this);
    task.execute(url);
  }
}
