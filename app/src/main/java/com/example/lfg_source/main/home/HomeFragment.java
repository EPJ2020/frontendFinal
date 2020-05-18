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
import com.example.lfg_source.service.MyService;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

  private User loggedInUser;
  private List<Group> groupList = new ArrayList<>();
  private HomeListAdapter homeListAdapter;
  private View yourProfileView = null;
  private MyService service;
  private MyService mainService;
  private int selectedGroupPosition;

  public HomeFragment(MyService service, User loggedInUser) {
    this.service = service;
    this.loggedInUser = loggedInUser;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_fragment, container, false);
    ((MainActivity) getActivity()).setNullToolbar("Home");
    mainService = ((MainActivity) getActivity()).getMainService();
    yourProfileView = view.findViewById(R.id.yourProfile);
    final TextView yourProfileText = yourProfileView.findViewById(R.id.homeListEntryName);
    yourProfileText.setText(loggedInUser.getFirstName());
    yourProfileView.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
              yourProfileView.setBackgroundColor(Color.WHITE);
            } else {
              yourProfileView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            return false;
          }
        });

    yourProfileView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mainService.sendMessageMyGroup();
          }
        });

    ImageButton editProfileButton = yourProfileView.findViewById(R.id.editButton);
    editProfileButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ((MainActivity) getActivity()).setNullToolbar("Profil bearbeiten");
            UserEditFragment nextFrag = new UserEditFragment(loggedInUser, service);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, nextFrag);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        });

    final RecyclerView recyclerView = view.findViewById(R.id.groupSelect);

    homeListAdapter =
        new HomeListAdapter(groupList, recyclerView, this, loggedInUser, service, mainService);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    DividerItemDecoration itemDecorator =
        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
    itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
    recyclerView.addItemDecoration(itemDecorator);

    recyclerView.setAdapter(homeListAdapter);

    View.OnClickListener newGroupOnClick =
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ((MainActivity) getActivity()).setNullToolbar("Bearbeiten");
            GroupEditFragment nextFrag = new GroupEditFragment(loggedInUser, service);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, nextFrag);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        };

    final ImageButton newGroupButton = view.findViewById(R.id.newGroupButton);
    newGroupButton.setOnClickListener(newGroupOnClick);
    final TextView newGroupText = view.findViewById(R.id.newGroupText);
    newGroupText.setOnClickListener(newGroupOnClick);

    newGroupText.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
              newGroupText.setBackgroundColor(Color.WHITE);
            } else {
              newGroupText.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            return false;
          }
        });

    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final Observer<List<Group>> myGroupObserver =
        new Observer<List<Group>>() {
          @Override
          public void onChanged(List<Group> groups) {
            groupList = new ArrayList<>();
            groupList.addAll(groups);
            homeListAdapter.changeGroupList(groupList);
            homeListAdapter.notifyDataSetChanged();
          }
        };
    service.getMyGroups().observe(getViewLifecycleOwner(), myGroupObserver);
    service.sendMessageMyGroup();
  }

  public void setSelectedGroup(int position) {
    this.selectedGroupPosition = position;
  }

  public int getSelectedGroupPosition(){
    return selectedGroupPosition;
  }
}
