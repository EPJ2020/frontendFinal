package com.example.lfg_source.main;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.example.lfg_source.entity.AnswerEntity;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.service.MyService;

public class MainFacade extends ViewModel {
  private MyService service;
  private String token;

  public MainFacade(MainActivity activity) {
    SharedPreferences preferences = activity.getSharedPreferences("LFG", Context.MODE_PRIVATE);
    token = preferences.getString("USERTOKEN", null);
    service = new MyService(token);
  }

  public void getMyGroups() {
    final String url = "http://152.96.56.38:8080/User/MyGroups";
    service.sendMessageGroups(url);
  }

  public void getMatchGroups() {
    final String url = "http://152.96.56.38:8080/User/Matches";
    service.sendMessageGroups(url);
  }

  public void getMyProfile() {
    final String url = "http://152.96.56.38:8080/User";
    service.sendMessageLoginUser(url);
  }

  public void getUsers(Group group) {
    final String url = "http://152.96.56.38:8080/Group/Matches/" + group.getGroupId();
    service.sendMessageMatchUsers(url);
  }

  public void deleteGroup(Group group) {
    final String url = "http://152.96.56.38:8080/Group/" + group.getGroupId();
    service.sendMessageDeleteGroup(url);
  }

  public void updateGroup(Group group) {
    final String url = "http://152.96.56.38:8080/Group/update";
    service.sendMessageEditGroup(group, url);
  }

  public void newGroup(Group group) {
    final String url = "http://152.96.56.38:8080/Group";
    service.sendMessageNewGroup(group, url);
  }

  public void updateUser(User user) {
    final String url = "http://152.96.56.38:8080/User/update";
    service.sendMessageEditUser(user, url);
  }

  public void newUser(User user) {
    final String url = "http://152.96.56.38:8080/User";
    service.sendMessageNewUser(user, url);
  }

  public void getGroupSuggestions() {
    final String url = "http://152.96.56.38:8080/User/Suggestions";
    service.sendMessageGetGroupSuggestions(url);
  }

  public void getUserSuggestions(Group group) {
    final String url = "http://152.96.56.38:8080/Group/Suggestions/" + group.getGroupId();
    service.sendMessageGetUserSuggestions(url);
  }

  public void setAnswer(AnswerEntity answer, String type) {
    final String url = "http://152.96.56.38:8080/" + type + "/MatchesAnswer";
    service.sendMessageAnswer(answer, url);
  }

  public MyService getService() {
    return service;
  }
}
