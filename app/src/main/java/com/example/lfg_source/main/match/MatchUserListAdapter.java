package com.example.lfg_source.main.match;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.User;

import java.util.List;

public class MatchUserListAdapter extends RecyclerView.Adapter<MatchUserListAdapter.MyViewHolder> {
  private List<User> memberList;
  private RecyclerView recyclerView;
  private MatchUserFragment context;

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

  public MatchUserListAdapter(
      List<User> list, RecyclerView recyclerView, MatchUserFragment context) {
    this.memberList = list;
    this.recyclerView = recyclerView;
    this.context = context;
  }

  public void changeGroupList(List<User> list) {
    this.memberList = list;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_entry, parent, false);
    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, final int position) {
    final Object object = memberList.get(position);
    if (object instanceof User) {
      final User user = (User) object;
      holder.name.setText(user.getFirstName() + " " + user.getLastName());
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
    return memberList.size();
  }
}
