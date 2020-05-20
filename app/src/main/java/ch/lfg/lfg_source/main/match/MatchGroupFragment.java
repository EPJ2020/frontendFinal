package ch.lfg.lfg_source.main.match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.main.MainActivity;
import ch.lfg.lfg_source.main.MainFacade;

public class MatchGroupFragment extends Fragment {
  private MatchListAdapter matchListAdapter;
  private List<Object> groupList = new ArrayList<>();
  private MainFacade facade;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.match_fragment, container, false);
    final RecyclerView recyclerView = view.findViewById(R.id.yourMatchesList);
    facade = new MainFacade((MainActivity) getActivity());
    matchListAdapter = new MatchListAdapter(groupList, this);
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
    facade.getService().getMatchGroups().observe(getViewLifecycleOwner(), userObserver);
    facade.getMatchGroups();
  }
}
