package ch.lfg.lfg_source.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.AnswerEntity;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.UserSuggestion;
import ch.lfg.lfg_source.main.MainActivity;
import ch.lfg.lfg_source.main.MainFacade;

public class UserSwipeFragment extends SwipeFragment {
  private static final int GET_MORE_SUGGETIONS = 3;

  private Group groupThatSearches;
  private TextView firstName;
  private TextView gender;
  private TextView age;
  private List<UserSuggestion> usersToSwipe = new ArrayList<>();
  private MainFacade facade;
  private int swipedUserId;

  public UserSwipeFragment(Group groupThatSearches) {
    this.groupThatSearches = groupThatSearches;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.setGestureSwipe();
    facade = new MainFacade((MainActivity) this.getActivity());
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
    final Observer<List<UserSuggestion>> userObserver =
        new Observer<List<UserSuggestion>>() {
          @Override
          public void onChanged(List<UserSuggestion> users) {
            usersToSwipe.addAll(users);
            usersToSwipe = new ArrayList<>(new HashSet<>(usersToSwipe));
            showSuggestion();
          }
        };
    facade.getService().getUserSuggestions().observe(getViewLifecycleOwner(), userObserver);
    facade.getUserSuggestions(groupThatSearches);
  }

  @Override
  public void showSuggestion() {
    if (usersToSwipe.size() < GET_MORE_SUGGETIONS) {
      facade.getUserSuggestions(groupThatSearches);
    }
    if (!usersToSwipe.isEmpty()) {
      setSuggestion();
    } else {
      super.setViewElements("", getString(R.string.noPersonsToSwipe), new ArrayList<String>());
      this.firstName.setText("");
      gender.setVisibility(View.GONE);
      age.setVisibility(View.GONE);
    }
  }

  private void setSuggestion() {
    super.setViewElements(
        usersToSwipe.get(0).getUser().getLastName(),
        usersToSwipe.get(0).getUser().getDescription(),
        usersToSwipe.get(0).getUser().getTags());
    super.setProgress(usersToSwipe.get(0).getPercent());
    this.firstName.setText(usersToSwipe.get(0).getUser().getFirstName());
    age.setVisibility(View.GONE);
    gender.setVisibility(View.GONE);
    if (!usersToSwipe.isEmpty() && usersToSwipe.get(0).getUser().getGender() != null) {
      gender.setText(getString(R.string.sex) + usersToSwipe.get(0).getUser().getGender());
      gender.setVisibility(View.VISIBLE);
    }
    if (!usersToSwipe.isEmpty() && usersToSwipe.get(0).getUser().getAge() != null) {
      gender.setText(getString(R.string.age) + usersToSwipe.get(0).getUser().getAge());
      gender.setVisibility(View.VISIBLE);
    }
    swipedUserId = usersToSwipe.get(0).getUser().getId();
    usersToSwipe.remove(0);
  }

  @Override
  public int getUserId() {
    int userId = swipedUserId;
    swipedUserId = -1;
    return userId;
  }

  @Override
  public int getGroupId() {
    return groupThatSearches.getGroupId();
  }

  @Override
  public void sendMessage(AnswerEntity answer) {
    facade.setAnswer(answer, getString(R.string.group));
  }
}
