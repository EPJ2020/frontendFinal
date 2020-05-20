package ch.lfg.lfg_source.service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.AnswerEntity;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.GroupSuggestion;
import ch.lfg.lfg_source.entity.LoginEntity;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.entity.UserSuggestion;
import ch.lfg.lfg_source.main.MainActivity;

public class MyService extends ViewModel {
  private MainActivity activity;
  private MutableLiveData<User> loginUser = new MutableLiveData<>();
  private MutableLiveData<List<Group>> myGroups = new MutableLiveData<>();
  private MutableLiveData<List<Group>> matchGroups = new MutableLiveData<>();
  private MutableLiveData<List<User>> matchUsers = new MutableLiveData<>();
  private MutableLiveData<List<UserSuggestion>> userSuggestion = new MutableLiveData<>();
  private MutableLiveData<List<GroupSuggestion>> groupSuggestion = new MutableLiveData<>();
  private MutableLiveData<String> loginResult = new MutableLiveData<>();
  private MutableLiveData<String> loginFailMessage = new MutableLiveData<>();

  public MyService(MainActivity activity) {
    this.activity = activity;
  }

  public LiveData<User> getLoginUser() {
    return loginUser;
  }

  public void setLoginUser(User data) {
    loginUser.setValue(data);
  }

  public MutableLiveData<List<Group>> getMyGroups() {
    return myGroups;
  }

  public void setDataMyGroup(List<Group> groups) {
    this.myGroups.postValue(groups);
  }

  public MutableLiveData<List<Group>> getMatchGroups() {
    return matchGroups;
  }

  public void setDataMatchGroup(List<Group> groups) {
    this.matchGroups.postValue(groups);
  }

  public MutableLiveData<List<User>> getMatchUsers() {
    return matchUsers;
  }

  public void setDataMatchUsers(List<User> users) {
    this.matchUsers.postValue(users);
  }

  public void setUserSuggestions(List<UserSuggestion> data) {
    this.userSuggestion.setValue(data);
  }

  public MutableLiveData<List<UserSuggestion>> getUserSuggestions() {
    return userSuggestion;
  }

  public void setGroupSuggestions(List<GroupSuggestion> data) {
    this.groupSuggestion.setValue(data);
  }

  public void setLoginData(String data) {
    loginResult.setValue(data);
  }

  public LiveData<String> getLoginResult() {
    return loginResult;
  }

  public void setLoginFailMessage(String message) {
    loginFailMessage.setValue(message);
  }

  public LiveData<String> getLoginFailMessage() {
    return loginFailMessage;
  }

  public MutableLiveData<List<GroupSuggestion>> getGroupSuggestions() {
    return groupSuggestion;
  }

  public void sendMessageLoginUser(String url) {
    RestClientGetUser<User> task = new RestClientGetUser<>(this, getToken(), url, User.class);
    task.execute();
  }

  public void sendMessageGroups(String url) {
    RestClientGetList<Group[]> task = new RestClientGetList<>(this, getToken(), url, Group[].class);
    task.execute();
  }

  public void sendMessageMatchUsers(String url) {
    RestClientGetList<User[]> task = new RestClientGetList<>(this, getToken(), url, User[].class);
    task.execute();
  }

  public void sendMessageDeleteGroup(String url) {
    RestClientDeleteGroup task = new RestClientDeleteGroup(getToken());
    task.execute(url);
  }

  public void sendMessageEditUser(User user, String url) {
    RestClientPatch<User> task = new RestClientPatch<>(user, getToken(), User.class);
    task.execute(url);
  }

  public void sendMessageEditGroup(Group group, String url) {
    RestClientPatch<Group> task = new RestClientPatch<>(group, getToken(), Group.class);
    task.execute(url);
  }

  public void sendMessageNewGroup(Group group, String url) {
    RestClientPost<Group> task = new RestClientPost<>(group, getToken(), url, Group.class);
    task.execute();
  }

  public void sendMessageNewUser(User user, String url) {
    RestClientPost<User> task = new RestClientPost<>(user, getToken(), url, User.class);
    task.execute();
  }

  public void sendMessageAnswer(AnswerEntity answer, String url) {
    RestClientPost<AnswerEntity> task =
        new RestClientPost<>(answer, getToken(), url, AnswerEntity.class);
    task.execute();
  }

  public void sendMessageAutentification(LoginEntity loginEntity, String url) {
    RestClientPostLogin task = new RestClientPostLogin(this, loginEntity, url);
    task.execute();
  }

  public void sendMessageGetUserSuggestions(String url) {
    RestClientGetList<UserSuggestion[]> task =
        new RestClientGetList<>(this, getToken(), url, UserSuggestion[].class);
    task.execute();
  }

  public void sendMessageGetGroupSuggestions(String url) {
    RestClientGetList<GroupSuggestion[]> task =
        new RestClientGetList<>(this, getToken(), url, GroupSuggestion[].class);
    task.execute();
  }

  private String getToken(){
    SharedPreferences preferences =
            activity.getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE);
    return preferences.getString(activity.getString(R.string.usertoken), null);
  }
}
