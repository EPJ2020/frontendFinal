package com.example.lfg_source.main.match;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;

import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MyViewHolder> {
  private List<Object> groupList;
  private RecyclerView recyclerView;
  private Fragment context;

  public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView name2;
    public ImageButton phoneButton;
    public ImageButton mailButton;
    public View holderView;

    public MyViewHolder(final View view) {
      super(view);
      holderView = view;
      name = view.findViewById(R.id.matchName);
      name2 = view.findViewById(R.id.matchName2);
      phoneButton = view.findViewById(R.id.phoneButton);
      mailButton = view.findViewById(R.id.mailButton);
    }
  }

  public MatchListAdapter(List<Object> groupList, RecyclerView recyclerView, Fragment context) {
    this.groupList = groupList;
    this.recyclerView = recyclerView;
    this.context = context;
  }

  public void changeGroupList(List<Object> groupList) {
    this.groupList = groupList;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_entry, parent, false);
    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, final int position) {
    final Object object = groupList.get(position);
    if (object != null && object instanceof Group) {
      final Group group = (Group) object;
      holder.name2.setVisibility(View.GONE);
      holder.name.setText(group.getName());
      holder.phoneButton.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(context.getActivity(), group.getPhoneNumber(), Toast.LENGTH_SHORT)
                  .show();
            }
          });
      if (group.getPhoneNumber() == null) {
        holder.phoneButton.setVisibility(View.GONE);
      }

      holder.mailButton.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(context.getActivity(), group.getEmail(), Toast.LENGTH_SHORT).show();
            }
          });
      if (group.getPhoneNumber() == null) {
        holder.mailButton.setVisibility(View.GONE);
      }
    }
    if (object != null && object instanceof User) {
      final User user = (User) object;
      holder.name2.setVisibility(View.VISIBLE);
      holder.name2.setText(user.getLastName());
      holder.name.setText(user.getFirstName());
      holder.phoneButton.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(context.getActivity(), user.getPhone(), Toast.LENGTH_SHORT).show();
            }
          });
      if (user.getPhone() == null) {
        holder.phoneButton.setVisibility(View.GONE);
      }

      holder.mailButton.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(context.getActivity(), user.getEmail(), Toast.LENGTH_SHORT).show();
            }
          });
      if (user.getPhone() == null) {
        holder.mailButton.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public int getItemCount() {
    return groupList.size();
  }
}
