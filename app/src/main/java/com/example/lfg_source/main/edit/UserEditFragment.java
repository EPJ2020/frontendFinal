package com.example.lfg_source.main.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.rest.RestClientEditProfilePatch;
import com.google.android.material.textfield.TextInputLayout;

public class UserEditFragment extends EditFragment {

  private UserEditViewModel mViewModel;
  private User actualuser;
  private Boolean isNewUser = false;

  private TextInputLayout inputFirstName;
  private TextInputLayout inputLastName;

  public UserEditFragment() {
    super();
    this.actualuser = new User();
    isNewUser = true;
  }

  public UserEditFragment(User loggedInUser) {
    super();
    actualuser = loggedInUser;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.user_edit_fragment, container, false);
    super.getViewElements(view);
    getUserViewElements(view);
    super.setValues(
        actualuser.getDescription(),
        actualuser.getTags(),
        actualuser.getEmail(),
        actualuser.getPhone(),
        actualuser.getActive());
    super.setButtons(actualuser);
    super.setUpTagContainer();
    return view;
  }

  @Override
  protected void update() {
    actualuser.changeAttributes(
        super.getInputDescriptionString(),
        super.getActiveState(),
        inputFirstName.getEditText().getText().toString().trim(),
        super.getInputPhone(),
        inputLastName.getEditText().getText().toString().trim(),
        super.getInputEmail(),
        super.getTags());
    if (isNewUser) {
      sendMessageNewUser();
    } else {
      sendMessageEditUser();
    }
  }

  private void sendMessageNewUser() {
    final String url = "http://152.96.56.38:8080/User";
  }

  private void sendMessageEditUser() {
    final String url = "http://152.96.56.38:8080/User/update";
    RestClientEditProfilePatch task = new RestClientEditProfilePatch(actualuser);
    task.execute(url);
  }

  private boolean validateFirstName() {
    String firstName = inputFirstName.getEditText().getText().toString().trim();
    if (firstName.isEmpty()) {
      inputFirstName.setError("Geben sie Ihren Vornamen ein");
      return false;
    }
    return true;
  }

  private boolean validateLastName() {
    String lastName = inputLastName.getEditText().getText().toString().trim();
    if (lastName.isEmpty()) {
      inputLastName.setError("Geben sie Ihren Nachnamen ein");
      return false;
    }
    return true;
  }

  private void getUserViewElements(View view) {
    inputFirstName = view.findViewById(R.id.firstName);
    inputLastName = view.findViewById(R.id.lastName);

    inputFirstName.getEditText().setText(actualuser.getFirstName());
    inputLastName.getEditText().setText(actualuser.getLastName());
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(UserEditViewModel.class);
  }

  @Override
  protected boolean allValidate() {
    return !(!validateLastName() | !validateFirstName());
  }
}
