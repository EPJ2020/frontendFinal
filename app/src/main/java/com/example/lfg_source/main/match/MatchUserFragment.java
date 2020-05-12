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
import com.example.lfg_source.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MatchUserFragment extends Fragment {
  private User loggedInUser;
  private MatchViewModel mViewModel;
  private MatchUserListAdapter matchListAdapter;
  private List<Group> groupAdminList = new ArrayList<>();
  private List<User> memberList = new ArrayList<>();
  private Group actual;
  private String token;

  public MatchUserFragment(User loggedInUser, Group group, String token) {
    this.loggedInUser = loggedInUser;
    this.actual = group;
    this.token = token;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.match_fragment, container, false);
    final RecyclerView recyclerView = view.findViewById(R.id.yourMatchesList);
    matchListAdapter = new MatchUserListAdapter(memberList, recyclerView, this);
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
    mViewModel.setToken(token);
    // groupAdminList is the List of groups where the loggedInUser is admin
    final Observer<List<Group>> groupObserver =
        new Observer<List<Group>>() {
          @Override
          public void onChanged(List<Group> groups) {
            groupAdminList = new ArrayList<>();
            groupAdminList.addAll(groups);
            matchListAdapter.notifyDataSetChanged();
            if (((MainActivity) getActivity()).getSpinner().getVisibility() == View.GONE) {
              ((MainActivity) getActivity())
                  .setupToolbar(groupAdminList, "Match", loggedInUser, true);
            }
          }
        };
    mViewModel.getDataGroupAdmin().observe(getViewLifecycleOwner(), groupObserver);
    mViewModel.sendMessageAdmin(loggedInUser.getId());

    // List of members of the actual group where the loggedInUser is admin.
    final Observer<List<User>> memberObserver =
        new Observer<List<User>>() {
          @Override
          public void onChanged(List<User> members) {
            memberList = new ArrayList<>();
            memberList.addAll(members);
            matchListAdapter.notifyDataSetChanged();
          }
        };
    mViewModel.getDataUser().observe(getViewLifecycleOwner(), memberObserver);
    mViewModel.sendMessage(actual);
  }
}
