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
import com.example.lfg_source.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserSwipeFragment extends SwipeFragment {
  private Group groupThatSearches;
  private TextView firstName;
  private UserSwipeViewModel mViewModel;
  private List<User> usersToSwipe = new ArrayList<>();
  private boolean isInit = true;

  public UserSwipeFragment(Group groupThatSearches) {
    this.groupThatSearches = groupThatSearches;
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
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(UserSwipeViewModel.class);
    mViewModel.setGroup(groupThatSearches);
    final Observer<List<User>> userObserver =
        new Observer<List<User>>() {
          @Override
          public void onChanged(List<User> users) {
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
          usersToSwipe.get(0).getLastName(),
          usersToSwipe.get(0).getDescription(),
          usersToSwipe.get(0).getTags());
      super.setProgress();
      this.firstName.setText(usersToSwipe.get(0).getFirstName());
      usersToSwipe.remove(0);
    } else {
      super.setViewElements(
          "Zurzeit wurden leider keine Passenden Personen gefunden", "", new ArrayList<String>());
      this.firstName.setText("");
    }
  }

  @Override
  public int getUserId() {
    if (usersToSwipe.isEmpty()) {
      return -1;
    }
    return usersToSwipe.get(0).getId();
  }

  @Override
  public int getGroupId() {
    return groupThatSearches.getGroupId();
  }

  @Override
  public void sendMessage(AnswerEntity answer) {
    final String url = "http://152.96.56.38:8080/Group/MatchesAnswer";
    RestClientAnswerPost task = new RestClientAnswerPost(answer);
    task.execute(url);
  }
}
