package com.example.lfg_source.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.LoginEntity;
import com.example.lfg_source.entity.LoginFormState;

public class LoginViewModel extends ViewModel {

  private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
  private MutableLiveData<String> loginResult = new MutableLiveData<>();
  private MutableLiveData<String> loginFailMessage = new MutableLiveData<>();

  LiveData<LoginFormState> getLoginFormState() {
    return loginFormState;
  }

  LiveData<String> getLoginFailMessage() {
    return loginFailMessage;
  }

  LiveData<String> getLoginResult() {
    return loginResult;
  }

  public void login(String username, String password) {
    final String url = "http://152.96.56.38:8080/User/login";
    LoginEntity registerData = new LoginEntity(username, password);
    sendMessage(registerData, url);
  }

  public void register(String username, String password) {
    final String url = "http://152.96.56.38:8080/User/register";
    LoginEntity registerData = new LoginEntity(username, password);
    sendMessage(registerData, url);
  }

  public void loginDataChanged(String username, String password) {
    if (!isUserNameValid(username)) {
      loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
    } else if (!isPasswordValid(password)) {
      loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
    } else {
      loginFormState.setValue(new LoginFormState(true));
    }
  }

  private boolean isUserNameValid(String username) {
    if (username == null) {
      return false;
    }
    if (username.contains("@")) {
      return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    } else {
      return !username.trim().isEmpty();
    }
  }

  private boolean isPasswordValid(String password) {
    return password != null && password.trim().length() > 5;
  }

  public void setLoginData(String data) {
    loginResult.setValue(data);
  }

  public void setLoginFailMessage(String message) {
    loginFailMessage.setValue(message);
  }

  public void sendMessage(LoginEntity loginEntity, String url) {
    RestClientLogin task = new RestClientLogin(this, loginEntity);
    task.execute(url);
  }
}
