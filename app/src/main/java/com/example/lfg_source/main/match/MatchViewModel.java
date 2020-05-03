package com.example.lfg_source.main.match;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.rest.RestClientMatchGroup;
import com.example.lfg_source.rest.RestClientMatchUser;

import java.util.List;

public class MatchViewModel extends ViewModel {
  private MutableLiveData<List<Group>> dataGroup;
  private MutableLiveData<List<User>> dataUser;
  private MutableLiveData<List<Group>> dataGroupAdmin;

  public void setDataGroup(List<Group> dataGroup) {
    this.dataGroup.setValue(dataGroup);
  }

  public void setDataGroupAdmin(List<Group> dataGroupAdmin) {
    this.dataGroupAdmin.setValue(dataGroupAdmin);
  }

  public MutableLiveData<List<Group>> getDataGroupAdmin() {
    if (dataGroupAdmin == null) {
      dataGroupAdmin = new MutableLiveData<>();
    }
    return dataGroupAdmin;
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
    final String url = "http://152.96.56.38:8080/User/Matches/" + userID;
    RestClientMatchGroup task = new RestClientMatchGroup(this, false);

    task.execute(url);
  }

  public void sendMessageAdmin(int userID) {
    final String url = "http://152.96.56.38:8080/User/Matches/" + userID;
    RestClientMatchGroup task = new RestClientMatchGroup(this, true);

    task.execute(url);
  }

  public void sendMessage(Group group) {
    final String url = "http://152.96.56.38:8080/Group/Matches/" + group.getOwnerId();
    RestClientMatchUser task = new RestClientMatchUser(this);
    task.execute(url);
  }
}
