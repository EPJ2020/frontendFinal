package com.example.lfg_source.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lfg_source.entity.User;

public class MainViewModel extends ViewModel {

  private MutableLiveData<User> loginUser = new MutableLiveData<>();

  LiveData<User> getLoginUser() {
    return loginUser;
  }

  public void setLoginUser(User data) {
    loginUser.setValue(data);
  }

  public void sendMessageUser(String token) {
    final String url = "http://152.96.56.38:8080/User";
    RestClientLoginUser task = new RestClientLoginUser(this, token);
    task.execute(url);
  }
}
