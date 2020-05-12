package com.example.lfg_source.main.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lfg_source.entity.Group;

import java.util.List;

public class HomeViewModel extends ViewModel {
  private MutableLiveData<List<Group>> groupList;
  private String token;

  public void setToken(String token) {
    this.token = token;
  }

  public void setData(List<Group> data) {
    this.groupList.setValue(data);
  }

  public MutableLiveData<List<Group>> getData() {
    if (groupList == null) {
      groupList = new MutableLiveData<>();
    }
    return groupList;
  }

  public void sendMessageGroup() {
    final String url = "http://152.96.56.38:8080/User/MyGroups";
    RestClientHome task = new RestClientHome(this, token);
    task.execute(url);
  }
}
