package ch.lfg.lfg_source.main;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.AnswerEntity;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.service.MyService;

public class MainFacade extends ViewModel {
  private MainActivity mainActivity;
  private MyService service;

  public MainFacade(MainActivity activity) {
    this.mainActivity = activity;
    service = new MyService(activity);
  }

  public void getMyGroups() {
    final String url = mainActivity.getString(R.string.getMyGroupsConnectString);
    service.sendMessageGroups(url);
  }

  public void getMatchGroups() {
    final String url = mainActivity.getString(R.string.getMatchGroupsConnectString);
    service.sendMessageGroups(url);
  }

  public void getMyProfile() {
    final String url = mainActivity.getString(R.string.getMyProfileConnectString);
    service.sendMessageLoginUser(url);
  }

  public void getUsers(Group group) {
    final String url = mainActivity.getString(R.string.getUsersConnectString) + group.getGroupId();
    service.sendMessageMatchUsers(url);
  }

  public void deleteGroup(Group group) {
    final String url =
        mainActivity.getString(R.string.deleteGroupConnectString) + group.getGroupId();
    service.sendMessageDeleteGroup(url);
  }

  public void updateGroup(Group group) {
    final String url = mainActivity.getString(R.string.updateGroupConnectString);
    service.sendMessageEditGroup(group, url);
  }

  public void newGroup(Group group) {
    final String url = mainActivity.getString(R.string.newGroupConnectString);
    service.sendMessageNewGroup(group, url);
  }

  public void updateUser(User user) {
    final String url = mainActivity.getString(R.string.updateUserConnectString);
    service.sendMessageEditUser(user, url);
  }

  public void newUser(User user) {
    final String url = mainActivity.getString(R.string.newUserConnectString);
    service.sendMessageNewUser(user, url);
  }

  public void getGroupSuggestions() {
    final String url = mainActivity.getString(R.string.getGroupSuggestionsConnectString);
    service.sendMessageGetGroupSuggestions(url);
  }

  public void getUserSuggestions(Group group) {
    final String url =
        mainActivity.getString(R.string.getUserSuggestionsConnectString) + group.getGroupId();
    service.sendMessageGetUserSuggestions(url);
  }

  public void setAnswer(AnswerEntity answer, String type) {
    final String url =
        mainActivity.getString(R.string.mainUrlConnectString)
            + type
            + mainActivity.getString(R.string.setAnswerConnectStringFragment);
    service.sendMessageAnswer(answer, url);
  }

  public MyService getService() {
    return service;
  }
}
