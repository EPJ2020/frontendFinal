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

import java.util.ArrayList;
import java.util.List;

public class MatchFragment extends Fragment {
  private User loggedInUser;
  private MatchViewModel mViewModel;
  private MatchListAdapter matchListAdapter;
  private List<Group> groupList = new ArrayList<>();
  private List<Group> groupAdminList = new ArrayList<>();

  public MatchFragment(User loggedInUser) {
    this.loggedInUser = loggedInUser;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.match_fragment, container, false);

    final RecyclerView recyclerView = view.findViewById(R.id.yourMatchesList);
    matchListAdapter = new MatchListAdapter(groupList, recyclerView, this);
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
    mViewModel = ViewModelProviders.of(this).get(MatchViewModel.class);
    final Observer<List<Group>> userObserver =
        new Observer<List<Group>>() {
          @Override
          public void onChanged(List<Group> groups) {
            groupList = new ArrayList<>();
            groupList.addAll(groups);
            matchListAdapter.changeGroupList(groupList);
            matchListAdapter.notifyDataSetChanged();
          }
        };
    mViewModel.getDataGroup().observe(getViewLifecycleOwner(), userObserver);
    mViewModel.sendMessage(loggedInUser.getId());

    // groupAdminList is the List of groups where the loggedInUser is admin
    final Observer<List<Group>> groupObserver =
        new Observer<List<Group>>() {
          @Override
          public void onChanged(List<Group> groups) {
            groupAdminList = new ArrayList<>();
            groupAdminList.addAll(groups);
            matchListAdapter.changeGroupList(groupAdminList);
            matchListAdapter.notifyDataSetChanged();
          }
        };
    mViewModel.getDataGroupAdmin().observe(getViewLifecycleOwner(), groupObserver);
    mViewModel.sendMessageAdmin(loggedInUser.getId());
  }
}
