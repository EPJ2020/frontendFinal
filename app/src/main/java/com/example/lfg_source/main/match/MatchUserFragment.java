package com.example.lfg_source.main.match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.service.MyService;

import java.util.ArrayList;
import java.util.List;

public class MatchUserFragment extends Fragment {
  private User loggedInUser;
  private MatchListAdapter matchListAdapter;
  private List<Group> groupAdminList = new ArrayList<>();
  private List<Object> memberList = new ArrayList<>();
  private Group actual;
  private MyService service;

  public MatchUserFragment(User loggedInUser, Group group, MyService service) {
    this.loggedInUser = loggedInUser;
    this.actual = group;
    this.service = service;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.match_fragment, container, false);
    final RecyclerView recyclerView = view.findViewById(R.id.yourMatchesList);
    matchListAdapter = new MatchListAdapter(memberList, recyclerView, this);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    DividerItemDecoration itemDecorator =
        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
    itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
    recyclerView.addItemDecoration(itemDecorator);
    recyclerView.setAdapter(matchListAdapter);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // List of members of the actual group where the loggedInUser is admin.
    final Observer<List<User>> memberObserver =
        new Observer<List<User>>() {
          @Override
          public void onChanged(List<User> members) {
            memberList = new ArrayList<>();
            memberList.addAll(members);
            matchListAdapter.changeGroupList(memberList);
            matchListAdapter.notifyDataSetChanged();
          }
        };
    service.getMatchUsers().observe(getViewLifecycleOwner(), memberObserver);
    service.sendMessageMatchUsers(actual);
  }
}
