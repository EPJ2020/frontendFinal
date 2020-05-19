package com.example.lfg_source.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.AnswerEntity;
import com.example.lfg_source.entity.GroupSuggestion;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.main.MainActivity;
import com.example.lfg_source.main.MainFacade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GroupSwipeFragment extends SwipeFragment {
  private User user;
  private List<GroupSuggestion> groupsToSwipe = new ArrayList<>();
  private TextView location;
  private int swipedGroupId;
  private MainFacade facade;

  public GroupSwipeFragment(User loggedInUser) {
    user = loggedInUser;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    facade = new MainFacade((MainActivity) this.getActivity());
    super.setGestureSwipe();
    View view = inflater.inflate(R.layout.group_swipe_fragment, container, false);
    super.getViewElements(view);
    location = view.findViewById(R.id.location);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final Observer<List<GroupSuggestion>> userObserver =
        new Observer<List<GroupSuggestion>>() {
          @Override
          public void onChanged(List<GroupSuggestion> groups) {
            groupsToSwipe.addAll(groups);
            groupsToSwipe = new ArrayList<>(new HashSet<>(groupsToSwipe));
            showSuggestion();
          }
        };
    facade.getService().getGroupSuggestions().observe(getViewLifecycleOwner(), userObserver);
    facade.getGroupSuggestions();
  }

  @Override
  public void showSuggestion() {
    if (groupsToSwipe.size() < 3) {
      facade.getGroupSuggestions();
    }
    if (!groupsToSwipe.isEmpty()) {
      super.setViewElements(
          groupsToSwipe.get(0).getGroup().getName(),
          groupsToSwipe.get(0).getGroup().getDescription(),
          groupsToSwipe.get(0).getGroup().getTags());
      location.setVisibility(View.GONE);
      if (groupsToSwipe.get(0).getGroup().getLocation() != null) {
        location.setText("Treffpunkt der Gruppe: " + groupsToSwipe.get(0).getGroup().getLocation());
        location.setVisibility(View.VISIBLE);
      }
      super.setProgress(groupsToSwipe.get(0).getPercent());
      swipedGroupId = groupsToSwipe.get(0).getGroup().getGroupId();
      groupsToSwipe.remove(0);
    } else {
      super.setViewElements(
          "", "Zurzeit wurden leider keine Passenden Gruppen gefunden", new ArrayList<String>());
      location.setVisibility(View.GONE);
    }
  }

  @Override
  public int getUserId() {
    return user.getId();
  }

  @Override
  public int getGroupId() {
    int groupId = swipedGroupId;
    swipedGroupId = -1;
    return groupId;
  }

  @Override
  public void sendMessage(AnswerEntity answer) {
    facade.setAnswer(answer, "User");
  }
}
