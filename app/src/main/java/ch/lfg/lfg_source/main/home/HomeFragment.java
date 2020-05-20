package ch.lfg.lfg_source.main.home;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.main.MainActivity;
import ch.lfg.lfg_source.main.MainFacade;
import ch.lfg.lfg_source.main.edit.GroupEditFragment;
import ch.lfg.lfg_source.main.edit.UserEditFragment;

public class HomeFragment extends Fragment {
  private User loggedInUser;
  private List<Group> groupList = new ArrayList<>();
  private HomeListAdapter homeListAdapter;
  private View yourProfileView = null;
  private MainFacade facade;
  private MainFacade mainFacade;
  private int selectedGroupPosition;

  public HomeFragment(User loggedInUser) {
    this.loggedInUser = loggedInUser;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_fragment, container, false);
    ((MainActivity) getActivity()).setNullToolbar(getString(R.string.home), true);
    mainFacade = ((MainActivity) getActivity()).getMainFacade();
    facade = new MainFacade((MainActivity) getActivity());
    yourProfileView = view.findViewById(R.id.yourProfile);

    final TextView yourProfileText = yourProfileView.findViewById(R.id.homeListEntryName);
    yourProfileText.setText(loggedInUser.getFirstName());

    setProfileViewListener();
    initEditProfileButton();
    initRecyclerView(view);
    initNewGroup(view);

    return view;
  }

  @SuppressLint("ClickableViewAccessibility")
  private void initNewGroup(View view) {
    final ImageButton newGroupButton = view.findViewById(R.id.newGroupButton);
    newGroupButton.setOnClickListener(getNewGroupListener());
    final TextView newGroupText = view.findViewById(R.id.newGroupText);
    newGroupText.setOnClickListener(getNewGroupListener());

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
  }

  private View.OnClickListener getNewGroupListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((MainActivity) getActivity()).setNullToolbar(getString(R.string.newGroup), false);
        GroupEditFragment nextFrag = new GroupEditFragment(loggedInUser);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, nextFrag);
        transaction.addToBackStack(null);
        transaction.commit();
      }
    };
  }

  private void initRecyclerView(View view) {
    final RecyclerView recyclerView = view.findViewById(R.id.groupSelect);
    homeListAdapter = new HomeListAdapter(groupList, recyclerView, this, loggedInUser, mainFacade);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    DividerItemDecoration itemDecorator =
        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
    itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
    recyclerView.addItemDecoration(itemDecorator);
    recyclerView.setAdapter(homeListAdapter);
  }

  private void initEditProfileButton() {
    ImageButton editProfileButton = yourProfileView.findViewById(R.id.editButton);
    editProfileButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ((MainActivity) getActivity()).setNullToolbar(getString(R.string.editProfile), false);
            UserEditFragment nextFrag = new UserEditFragment(loggedInUser);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, nextFrag);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        });
  }

  private void setProfileViewListener() {
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
            mainFacade.getMyGroups();
          }
        });
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
    facade.getService().getMyGroups().observe(getViewLifecycleOwner(), myGroupObserver);
    facade.getMyGroups();
  }

  public void setSelectedGroup(int position) {
    this.selectedGroupPosition = position;
  }

  public int getSelectedGroupPosition() {
    return selectedGroupPosition;
  }
}
