package com.example.lfg_source.main.match;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.Group;

import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MyViewHolder> {
  private List<Group> groupList;
  private RecyclerView recyclerView;
  private MatchFragment context;

  public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageButton phoneButton;
    public ImageButton mailButton;
    public View holderView;

    public MyViewHolder(final View view) {
      super(view);
      holderView = view;
      name = view.findViewById(R.id.matchName);
      phoneButton = view.findViewById(R.id.phoneButton);
      mailButton = view.findViewById(R.id.mailButton);
    }
  }

  public MatchListAdapter(List<Group> groupList, RecyclerView recyclerView, MatchFragment context) {
    this.groupList = groupList;
    this.recyclerView = recyclerView;
    this.context = context;
  }

  public void changeGroupList(List<Group> groupList) {
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
    final Group group = groupList.get(position);
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

  @Override
  public int getItemCount() {
    return groupList.size();
  }
}
