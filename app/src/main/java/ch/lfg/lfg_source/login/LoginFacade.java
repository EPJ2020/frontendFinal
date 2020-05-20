package ch.lfg.lfg_source.login;

import android.app.Activity;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.LoginEntity;
import ch.lfg.lfg_source.entity.LoginFormState;
import ch.lfg.lfg_source.service.MyService;

public class LoginFacade extends ViewModel {
  private Activity activity;
  private MyService service;
  private static final int PASSWORD_LENGTH = 6;

  private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

  protected LoginFacade(Activity activity) {
    this.activity = activity;
    service = new MyService(null);
  }

  protected void login(String username, String password) {
    final String url = activity.getString(R.string.loginConnectString);
    LoginEntity registerData = new LoginEntity(username, password);
    service.sendMessageAutentification(registerData, url);
  }

  protected void register(String username, String password) {
    final String url = activity.getString(R.string.registerConnectString);
    LoginEntity registerData = new LoginEntity(username, password);
    service.sendMessageAutentification(registerData, url);
  }

  protected MyService getService() {
    return service;
  }

  LiveData<LoginFormState> getLoginFormState() {
    return loginFormState;
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
    return password != null && password.trim().length() >= PASSWORD_LENGTH;
  }
}
