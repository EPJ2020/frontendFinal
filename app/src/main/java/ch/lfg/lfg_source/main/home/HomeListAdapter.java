package ch.lfg.lfg_source.main.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.main.MainActivity;
import ch.lfg.lfg_source.main.MainFacade;
import ch.lfg.lfg_source.main.edit.GroupEditFragment;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {
  private List<Group> groupList;
  private RecyclerView recyclerView;
  private HomeFragment context;
  private User loggedInUser;
  private MainFacade mainFacade;

  public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private ImageButton editButton;
    private View holderView;

    public MyViewHolder(final View view) {
      super(view);
      holderView = view;
      name = view.findViewById(R.id.homeListEntryName);
      editButton = view.findViewById(R.id.editButton);
    }
  }

  public HomeListAdapter(
      List<Group> groupList,
      RecyclerView recyclerView,
      HomeFragment context,
      User loggedInUser,
      MainFacade mainFacade) {
    this.groupList = groupList;
    this.recyclerView = recyclerView;
    this.context = context;
    this.loggedInUser = loggedInUser;
    this.mainFacade = mainFacade;
  }

  public void changeGroupList(List<Group> groupList) {
    this.groupList = groupList;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_entry, parent, false);
    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, final int position) {
    final Group group = groupList.get(position);
    holder.name.setText(group.getName());

    setHolderEditButtonListener(holder, group);
    setHolderViewListener(holder, position);
  }

  private void setHolderViewListener(MyViewHolder holder, final int position) {
    holder.holderView.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
              recyclerView.getChildAt(position).setBackgroundColor(Color.WHITE);
            } else {
              recyclerView
                  .getChildAt(position)
                  .setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            }
            return false;
          }
        });

    holder.holderView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mainFacade.getMyGroups();
            context.setSelectedGroup(position);
          }
        });
  }

  private void setHolderEditButtonListener(MyViewHolder holder, final Group group) {
    holder.editButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ((MainActivity) context.getActivity())
                .setNullToolbar(group + " " + context.getString(R.string.edit), false);
            GroupEditFragment nextFrag = new GroupEditFragment(group, loggedInUser);
            FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, nextFrag);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        });
  }

  @Override
  public int getItemCount() {
    return groupList.size();
  }
}
