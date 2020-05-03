package com.example.lfg_source.main.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.rest.RestClientEditGroupPatch;
import com.example.lfg_source.rest.RestClientNewGroupPost;
import com.google.android.material.textfield.TextInputLayout;

public class GroupEditFragment extends EditFragment {
  User groupAdminUser;
  private GroupEditViewModel mViewModel;
  private Group actualGroup;
  private Boolean isNewGroup = false;

  private TextInputLayout inputGroupName;

  public GroupEditFragment(User groupAdminUser) {
    super();
    this.groupAdminUser = groupAdminUser;
    this.actualGroup = new Group(groupAdminUser.getId());
    isNewGroup = true;
  }

  public GroupEditFragment(Group group, User groupAdminUser) {
    super();
    this.groupAdminUser = groupAdminUser;
    actualGroup = group;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.group_edit_fragment, container, false);

    super.getViewElements(view);
    getGroupViewElements(view);
    super.setValues(
        actualGroup.getDescription(),
        actualGroup.getTags(),
        actualGroup.getEmail(),
        actualGroup.getPhoneNumber(),
        actualGroup.getActive());
    super.setButtons(groupAdminUser);
    super.setUpTagContainer();

    return view;
  }

  @Override
  protected void update() {
    actualGroup.changeAttributes(
        super.getInputDescriptionString(),
        super.getActiveState(),
        inputGroupName.getEditText().getText().toString().trim(),
        super.getInputPhone(),
        super.getInputEmail(),
        super.getTags());
    if (isNewGroup) {
      sendMessageNewGroup();
    } else {
      sendMessageEditGroup();
    }
  }

  private void sendMessageNewGroup() {
    final String url = "http://152.96.56.38:8080/Group";
    RestClientNewGroupPost task = new RestClientNewGroupPost(actualGroup);
    task.execute(url);
  }

  private void sendMessageEditGroup() {
    final String url = "http://152.96.56.38:8080/Group/update";
    RestClientEditGroupPatch task = new RestClientEditGroupPatch(actualGroup);
    task.execute(url);
  }

  private void getGroupViewElements(View view) {
    inputGroupName = view.findViewById(R.id.groupName);

    if (actualGroup != null) {
      inputGroupName.getEditText().setText(actualGroup.getName());
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(GroupEditViewModel.class);
  }

  private boolean validateGroupName() {
    String firstName = inputGroupName.getEditText().getText().toString().trim();
    if (firstName.isEmpty()) {
      inputGroupName.setError("Geben Sie einen Gruppennamen an ein");
      return false;
    }
    return true;
  }

  @Override
  protected boolean allValidate() {
    return !(!validateGroupName());
  }
}
