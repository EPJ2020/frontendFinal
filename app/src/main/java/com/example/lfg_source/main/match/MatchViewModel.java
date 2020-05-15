package com.example.lfg_source.main.match;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;

import java.util.List;

public class MatchViewModel extends ViewModel {
  private MutableLiveData<List<Group>> dataGroup;
  private MutableLiveData<List<User>> dataUser;
  private String token;

  public void setToken(String token) {
    this.token = token;
  }

  public void setDataGroup(List<Group> dataGroup) {
    this.dataGroup.setValue(dataGroup);
  }

  public void setDataUser(List<User> dataUser) {
    this.dataUser.setValue(dataUser);
  }

  public MutableLiveData<List<Group>> getDataGroup() {
    if (dataGroup == null) {
      dataGroup = new MutableLiveData<>();
    }
    return dataGroup;
  }

  public MutableLiveData<List<User>> getDataUser() {
    if (dataUser == null) {
      dataUser = new MutableLiveData<>();
    }
    return dataUser;
  }

  public void sendMessage(int userID) {
    final String url = "http://152.96.56.38:8080/User/Matches/";
    RestClientMatchGroup task = new RestClientMatchGroup(this, token);
    task.execute(url);
  }

  public void sendMessage(Group group) {
    final String url = "http://152.96.56.38:8080/Group/Matches/" + group.getGroupId();
    RestClientMatchUser task = new RestClientMatchUser(this, token);
    task.execute(url);
  }
}
