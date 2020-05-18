package com.example.lfg_source.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lfg_source.entity.AnswerEntity;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.GroupSuggestion;
import com.example.lfg_source.entity.LoginEntity;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.entity.UserSuggestion;

import java.util.ArrayList;
import java.util.List;

public class MyService {
  private String token;
  private MutableLiveData<User> loginUser = new MutableLiveData<>();
  private MutableLiveData<List<Group>> myGroups = new MutableLiveData<>();
  private MutableLiveData<List<Group>> matchGroups = new MutableLiveData<>();
  private MutableLiveData<List<User>> matchUsers = new MutableLiveData<>();
  private MutableLiveData<String> loginResult = new MutableLiveData<>();
  private MutableLiveData<String> loginFailMessage = new MutableLiveData<>();
  private MutableLiveData<List<UserSuggestion>> userSuggestion = new MutableLiveData<>();
  private MutableLiveData<List<GroupSuggestion>> groupSuggestion = new MutableLiveData<>();

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

  public LiveData<String> getLoginResult() {
    return loginResult;
  }

  public void setLoginResult(String data) {
    loginResult.setValue(data);
  }

  public LiveData<String> getLoginFailMessage() {
    return loginFailMessage;
  }

  public void setLoginFailMessage(String message) {
    loginFailMessage.setValue(message);
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

  public MutableLiveData<List<GroupSuggestion>> getGroupSuggestions() {
    return groupSuggestion;
  }

  public void sendMessageLoginUser() {
    final String url = "http://152.96.56.38:8080/User";
    RestClientGetUser<User> task = new RestClientGetUser<>(this, token, url, User.class);
    task.execute();
  }

  public void sendMessageMyGroup() {
    final String url = "http://152.96.56.38:8080/User/MyGroups";
    RestClientGetList<Group[]> task = new RestClientGetList<>(this, token, url, Group[].class);
    task.execute();
  }

  public void sendMessageMatchGroups() {
    final String url = "http://152.96.56.38:8080/User/Matches";
    RestClientGetList<Group[]> task = new RestClientGetList<>(this, token, url, Group[].class);
    task.execute();
  }

  public void sendMessageMatchUsers(Group group) {
    final String url = "http://152.96.56.38:8080/Group/Matches/" + group.getGroupId();
    RestClientGetList<User[]> task = new RestClientGetList<>(this, token, url, User[].class);
    task.execute();
  }

  public void sendMessageDeleteGroup(Group group) {
    final String url = "http://152.96.56.38:8080/Group/" + group.getGroupId();
    RestClientDeleteGroup task = new RestClientDeleteGroup(token);
    task.execute(url);
  }

  public void sendMessageEditUser(User user) {
    final String url = "http://152.96.56.38:8080/User/update";
    RestClientPatch<User> task = new RestClientPatch<>(user, token, User.class);
    task.execute(url);
  }

  public void sendMessageEditGroup(Group group) {
    final String url = "http://152.96.56.38:8080/Group/update";
    RestClientPatch<Group> task = new RestClientPatch<>(group, token, Group.class);
    task.execute(url);
  }

  public void sendMessageNewGroup(Group group) {
    final String url = "http://152.96.56.38:8080/Group";
    RestClientPost<Group> task = new RestClientPost<>(group, token, url, Group.class);
    task.execute();
  }

  public void sendMessageNewUser(User user) {
    final String url = "http://152.96.56.38:8080/User";
    RestClientPost<User> task = new RestClientPost<>(user, token, url, User.class);
    task.execute();
  }

  public void sendMessageAnswer(AnswerEntity answer, String type) {
    final String url = "http://152.96.56.38:8080/" + type + "/MatchesAnswer";
    RestClientPost<AnswerEntity> task =
        new RestClientPost<>(answer, token, url, AnswerEntity.class);
    task.execute();
  }

  public void login(String username, String password) {
    final String url = "http://152.96.56.38:8080/User/login";
    LoginEntity registerData = new LoginEntity(username, password);
    sendMessage(registerData, url);
  }

  public void sendMessage(LoginEntity loginEntity, String url) {
    RestClientPostLogin task = new RestClientPostLogin(this, loginEntity);
    task.execute(url);
  }

  public void register(String username, String password) {
    final String url = "http://152.96.56.38:8080/User/register";
    LoginEntity registerData = new LoginEntity(username, password);
    sendMessage(registerData, url);
  }

  public void sendMessageGetUserSuggestions(Group group) {
    final String url = "http://152.96.56.38:8080/Group/Suggestions/" + group.getGroupId();
    RestClientGetList<UserSuggestion[]> task =
        new RestClientGetList<>(this, token, url, UserSuggestion[].class);
    task.execute();
  }

  public void sendMessageGetGroupSuggestions() {
    final String url = "http://152.96.56.38:8080/User/Suggestions";
    RestClientGetList<GroupSuggestion[]> task =
        new RestClientGetList<>(this, token, url, GroupSuggestion[].class);
    task.execute();
  }
}
