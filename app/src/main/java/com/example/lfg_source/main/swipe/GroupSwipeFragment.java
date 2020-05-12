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

import java.util.ArrayList;
import java.util.List;

public class GroupSwipeFragment extends SwipeFragment {
  private GroupSwipeViewModel mViewModel;
  private int userId;
  private boolean isInit = true;
  private List<Group> groupsToSwipe = new ArrayList<>();
  private TextView location;
  private String token;

  public GroupSwipeFragment(int loggedInUserId, String token) {
    userId = loggedInUserId;
    this.token = token;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.setGestureSwipe();
    View view = inflater.inflate(R.layout.group_swipe_fragment, container, false);
    super.getViewElements(view);
    location = view.findViewById(R.id.location);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(GroupSwipeViewModel.class);
    mViewModel.setUserId(userId);
    mViewModel.setToken(token);
    final Observer<List<Group>> userObserver =
        new Observer<List<Group>>() {
          @Override
          public void onChanged(List<Group> groups) {
            groupsToSwipe.addAll(groups);
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
    if (groupsToSwipe.size() < 3) {
      mViewModel.sendMessage();
    }
    if (!groupsToSwipe.isEmpty()) {
      super.setViewElements(
          groupsToSwipe.get(0).getName(),
          groupsToSwipe.get(0).getDescription(),
          groupsToSwipe.get(0).getTags());
      location.setVisibility(View.GONE);
      if (groupsToSwipe.get(0).getLocation() != null) {
        location.setText("Treffpunkt der Gruppe: " + groupsToSwipe.get(0).getLocation());
        location.setVisibility(View.VISIBLE);
      }
      super.setProgress();
      groupsToSwipe.remove(0);
    } else {
      super.setViewElements(
          "", "Zurzeit wurden leider keine Passenden Gruppen gefunden", new ArrayList<String>());
      location.setVisibility(View.GONE);
    }
  }

  @Override
  public int getUserId() {
    return userId;
  }

  @Override
  public int getGroupId() {
    if (groupsToSwipe.isEmpty()) {
      return -1;
    }
    return groupsToSwipe.get(0).getGroupId();
  }

  @Override
  public void sendMessage(AnswerEntity answer) {
    final String url = "http://152.96.56.38:8080/User/MatchesAnswer";
    RestClientAnswerPost task = new RestClientAnswerPost(answer, token);
    task.execute(url);
  }
}
