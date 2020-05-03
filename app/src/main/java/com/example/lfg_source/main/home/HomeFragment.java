package com.example.lfg_source.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.main.MainActivity;
import com.example.lfg_source.main.edit.GroupEditFragment;
import com.example.lfg_source.main.edit.UserEditFragment;
import com.example.lfg_source.main.swipe.GroupSwipeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

  private final User loggedInUser;
  private HomeViewModel mViewModel;
  private List<Group> groupList = new ArrayList<>();
  private HomeListAdapter homeListAdapter;
  private View yourProfileView = null;

  public HomeFragment(User loggedInUser) {
    this.loggedInUser = loggedInUser;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_fragment, container, false);

    yourProfileView = view.findViewById(R.id.yourProfile);
    final TextView yourProfileText = yourProfileView.findViewById(R.id.homeListEntryName);
    yourProfileText.setText("Your Profile");
    yourProfileView.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            yourProfileView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            setSelected(null);
            return false;
          }
        });

    yourProfileView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            GroupSwipeFragment nextFrag = new GroupSwipeFragment(loggedInUser.getId());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, nextFrag);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        });

    yourProfileView.setBackgroundColor(getResources().getColor(R.color.colorAccent));

    ImageButton editProfileButton = yourProfileView.findViewById(R.id.editButton);
    editProfileButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            UserEditFragment nextFrag = new UserEditFragment(loggedInUser);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, nextFrag);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        });

    final RecyclerView recyclerView = view.findViewById(R.id.groupSelect);

    homeListAdapter = new HomeListAdapter(groupList, recyclerView, this, loggedInUser);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    DividerItemDecoration itemDecorator =
        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
    itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
    recyclerView.addItemDecoration(itemDecorator);

    recyclerView.setAdapter(homeListAdapter);

    ImageButton newGroupButton = view.findViewById(R.id.newGroupButton);
    newGroupButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            GroupEditFragment nextFrag = new GroupEditFragment(loggedInUser);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, nextFrag);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        });

    return view;
  }

  public void setSelected(Group group) {
    if (group != null) {
      yourProfileView.setBackgroundColor(Color.WHITE);
    }
    homeListAdapter.deselectGroups();
    ((MainActivity) getActivity()).selectedGroup = group;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    final Observer<List<Group>> userObserver =
        new Observer<List<Group>>() {
          @Override
          public void onChanged(List<Group> groups) {
            groupList = new ArrayList<>();
            groupList.addAll(groups);
            homeListAdapter.changeGroupList(groupList);
            homeListAdapter.notifyDataSetChanged();
          }
        };
    mViewModel.getData().observe(getViewLifecycleOwner(), userObserver);
    mViewModel.sendMessage(loggedInUser.getId());
  }
}
