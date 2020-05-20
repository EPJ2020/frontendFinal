package ch.lfg.lfg_source.main.match;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.User;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MyViewHolder> {
  private List<Object> groupList;
  private Fragment context;

  public class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView firstName;
    private TextView lastName;
    private ImageButton phoneButton;
    private ImageButton mailButton;

    public MyViewHolder(final View view) {
      super(view);
      firstName = view.findViewById(R.id.matchName);
      lastName = view.findViewById(R.id.matchName2);
      phoneButton = view.findViewById(R.id.phoneButton);
      mailButton = view.findViewById(R.id.mailButton);
    }
  }

  public MatchListAdapter(List<Object> groupList, Fragment context) {
    this.groupList = groupList;
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
    if (object instanceof Group) {
      showGroupInformation(holder, object);
    }
    if (object instanceof User) {
      showUserInformation(holder, object);
    }
  }

  private void showUserInformation(MyViewHolder holder, Object object) {
    final User user = (User) object;
    holder.lastName.setVisibility(View.VISIBLE);
    holder.lastName.setText(user.getLastName());
    holder.firstName.setText(user.getFirstName());

    if (user.getPhone() == null) {
      holder.phoneButton.setVisibility(View.GONE);
    } else {
      setPhoneButtonListener(holder, user.getPhone());
    }

    if (user.getPhone() == null) {
      holder.mailButton.setVisibility(View.GONE);
    } else {
      setMailButtonListener(holder, user.getEmail());
    }
  }

  private void showGroupInformation(MyViewHolder holder, Object object) {
    final Group group = (Group) object;
    holder.lastName.setVisibility(View.GONE);
    holder.firstName.setText(group.getName());

    if (group.getPhoneNumber() == null) {
      holder.phoneButton.setVisibility(View.GONE);
    } else {
      setPhoneButtonListener(holder, group.getPhoneNumber());
    }

    if (group.getEmail() == null) {
      holder.mailButton.setVisibility(View.GONE);
    } else {
      setMailButtonListener(holder, group.getEmail());
    }
  }

  private void setPhoneButtonListener(MyViewHolder holder, final String text) {
    holder.phoneButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(context.getActivity(), text, Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void setMailButtonListener(MyViewHolder holder, final String text) {
    holder.phoneButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(context.getActivity(), text, Toast.LENGTH_SHORT).show();
          }
        });
  }

  @Override
  public int getItemCount() {
    return groupList.size();
  }
}
