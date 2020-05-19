package com.example.lfg_source.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lfg_source.entity.AnswerEntity;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.GroupSuggestion;
import com.example.lfg_source.entity.LoginEntity;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.entity.UserSuggestion;

import java.util.List;

public class MyService extends ViewModel {
  private String token;
  private MutableLiveData<User> loginUser = new MutableLiveData<>();
  private MutableLiveData<List<Group>> myGroups = new MutableLiveData<>();
  private MutableLiveData<List<Group>> matchGroups = new MutableLiveData<>();
  private MutableLiveData<List<User>> matchUsers = new MutableLiveData<>();
  private MutableLiveData<List<UserSuggestion>> userSuggestion = new MutableLiveData<>();
  private MutableLiveData<List<GroupSuggestion>> groupSuggestion = new MutableLiveData<>();
  private MutableLiveData<String> loginResult = new MutableLiveData<>();
  private MutableLiveData<String> loginFailMessage = new MutableLiveData<>();

  public MyService(String token) {
    this.token = token;
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
    RestClientGetUser<User> task = new RestClientGetUser<>(this, token, url, User.class);
    task.execute();
  }

  public void sendMessageGroups(String url) {
    RestClientGetList<Group[]> task = new RestClientGetList<>(this, token, url, Group[].class);
    task.execute();
  }

  public void sendMessageMatchUsers(String url) {
    RestClientGetList<User[]> task = new RestClientGetList<>(this, token, url, User[].class);
    task.execute();
  }

  public void sendMessageDeleteGroup(String url) {
    RestClientDeleteGroup task = new RestClientDeleteGroup(token);
    task.execute(url);
  }

  public void sendMessageEditUser(User user, String url) {
    RestClientPatch<User> task = new RestClientPatch<>(user, token, User.class);
    task.execute(url);
  }

  public void sendMessageEditGroup(Group group, String url) {
    RestClientPatch<Group> task = new RestClientPatch<>(group, token, Group.class);
    task.execute(url);
  }

  public void sendMessageNewGroup(Group group, String url) {
    RestClientPost<Group> task = new RestClientPost<>(group, token, url, Group.class);
    task.execute();
  }

  public void sendMessageNewUser(User user, String url) {
    RestClientPost<User> task = new RestClientPost<>(user, token, url, User.class);
    task.execute();
  }

  public void sendMessageAnswer(AnswerEntity answer, String url) {
    RestClientPost<AnswerEntity> task =
        new RestClientPost<>(answer, token, url, AnswerEntity.class);
    task.execute();
  }

  public void sendMessageAutentification(LoginEntity loginEntity, String url) {
    RestClientPostLogin task = new RestClientPostLogin(this, loginEntity, url);
    task.execute();
  }

  public void sendMessageGetUserSuggestions(String url) {
    RestClientGetList<UserSuggestion[]> task =
        new RestClientGetList<>(this, token, url, UserSuggestion[].class);
    task.execute();
  }

  public void sendMessageGetGroupSuggestions(String url) {
    RestClientGetList<GroupSuggestion[]> task =
        new RestClientGetList<>(this, token, url, GroupSuggestion[].class);
    task.execute();
  }
}
