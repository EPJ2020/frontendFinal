package com.example.lfg_source.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;

import java.util.List;

public class MainViewModel extends ViewModel {

  private MutableLiveData<User> loginUser = new MutableLiveData<>();
  private MutableLiveData<List<Group>> groups = new MutableLiveData<>();

  LiveData<User> getLoginUser() {
    return loginUser;
  }

  public void setLoginUser(User data) {
    loginUser.setValue(data);
  }

  public MutableLiveData<List<Group>> getGroups() {
    return groups;
  }

  public void setDataGroup(List<Group> groups) {
    this.groups.postValue(groups);
  }

  public void sendMessageUser(String token) {
    final String url = "http://152.96.56.38:8080/User";
    RestClientLoginUser task = new RestClientLoginUser(this, token);
    task.execute(url);
  }

  public void sendMessageGroup(String token) {
    final String url = "http://152.96.56.38:8080//User/MyGroups";
    RestClientGroup task = new RestClientGroup(this, token);
    task.execute(url);
  }
}
