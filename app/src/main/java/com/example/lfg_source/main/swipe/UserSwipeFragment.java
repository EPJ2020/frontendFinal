package com.example.lfg_source.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.AnswerEntity;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.UserSuggestion;

import java.util.ArrayList;
import java.util.List;

public class UserSwipeFragment extends SwipeFragment {
  private Group groupThatSearches;
  private TextView firstName;
  private TextView gender;
  private TextView age;
  private UserSwipeViewModel mViewModel;
  private List<UserSuggestion> usersToSwipe = new ArrayList<>();
  private boolean isInit = true;
  private String token;

  public UserSwipeFragment(Group groupThatSearches, String token) {
    this.groupThatSearches = groupThatSearches;
    this.token = token;
  }

  public Group getGroupThatSearches() {
    return groupThatSearches;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.setGestureSwipe();
    View view = inflater.inflate(R.layout.user_swipe_fragment, container, false);
    super.getViewElements(view);
    firstName = view.findViewById(R.id.firstname);
    gender = view.findViewById(R.id.gender);
    age = view.findViewById(R.id.age);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(UserSwipeViewModel.class);
    mViewModel.setGroup(groupThatSearches);
    mViewModel.setToken(token);
    final Observer<List<UserSuggestion>> userObserver =
        new Observer<List<UserSuggestion>>() {
          @Override
          public void onChanged(List<UserSuggestion> users) {
            usersToSwipe.addAll(users);
            if (isInit) {
              showSuggestion();
            }
            isInit = false;
          }
        };
    mViewModel.getData().observe(getViewLifecycleOwner(), userObserver);
    mViewModel.sendMessage();
  }

  @Override
  public void showSuggestion() {
    if (usersToSwipe.size() < 3) {
      mViewModel.sendMessage();
    }
    if (!usersToSwipe.isEmpty()) {
      super.setViewElements(
          usersToSwipe.get(0).getUser().getLastName(),
          usersToSwipe.get(0).getUser().getDescription(),
          usersToSwipe.get(0).getUser().getTags());
      super.setProgress(usersToSwipe.get(0).getPercent());
      this.firstName.setText(usersToSwipe.get(0).getUser().getFirstName());
      usersToSwipe.remove(0);
      age.setVisibility(View.GONE);
      gender.setVisibility(View.GONE);
      if (usersToSwipe.size() > 0 && usersToSwipe.get(0).getUser().getGender() != null) {
        gender.setText("Geschlecht: " + usersToSwipe.get(0).getUser().getGender());
        gender.setVisibility(View.VISIBLE);
      }
      if (usersToSwipe.size() > 0 && usersToSwipe.get(0).getUser().getAge() != null) {
        gender.setText("Alter: " + usersToSwipe.get(0).getUser().getGender());
        gender.setVisibility(View.VISIBLE);
      }
    } else {
      super.setViewElements(
          "", "Zurzeit wurden leider keine Passenden Personen gefunden", new ArrayList<String>());
      this.firstName.setText("");
      gender.setVisibility(View.GONE);
      age.setVisibility(View.GONE);
    }
  }

  @Override
  public int getUserId() {
    if (usersToSwipe.isEmpty()) {
      return -1;
    }
    return usersToSwipe.get(0).getUser().getId();
  }

  @Override
  public int getGroupId() {
    return groupThatSearches.getGroupId();
  }

  @Override
  public void sendMessage(AnswerEntity answer) {
    final String url = "http://152.96.56.38:8080/Group/MatchesAnswer";
    RestClientAnswerPost task = new RestClientAnswerPost(answer, token);
    task.execute(url);
  }
}
