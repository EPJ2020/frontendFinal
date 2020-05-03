package com.example.lfg_source.main.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lfg_source.entity.Group;
import com.example.lfg_source.rest.RestClientHome;

import java.util.List;

public class HomeViewModel extends ViewModel {
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

  public void sendMessage(int userID) {
    final String url = "http://152.96.56.38:8080/User/MyGroups/" + userID;
    RestClientHome task = new RestClientHome();
    task.setHomeViewModel(this);
    task.execute(url);
  }
}
