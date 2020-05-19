package com.example.lfg_source.main.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.main.MainActivity;
import com.example.lfg_source.main.MainFacade;
import com.google.android.material.textfield.TextInputLayout;

public class UserEditFragment extends EditFragment {

  private User actualuser;
  private Boolean isNewUser = false;
  private MainFacade facade;

  private TextInputLayout inputFirstName;
  private TextInputLayout inputLastName;
  private TextInputLayout inputAge;
  private TextInputLayout inputGender;

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
    facade = new MainFacade((MainActivity) getActivity());
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
    if (isNewUser) {
      super.disableCancelButton();
    }
    super.setUpTagContainer();
    return view;
  }

  @Override
  protected void update() {
    actualuser.setDescription(super.getInputDescriptionString());
    actualuser.setActive(super.getActiveState());
    actualuser.setLastName(inputLastName.getEditText().getText().toString().trim());
    actualuser.setFirstName(inputFirstName.getEditText().getText().toString().trim());
    actualuser.setPhone(super.getInputPhone());
    actualuser.setEmail(super.getInputEmail());
    actualuser.setTags(super.getTags());
    actualuser.setGender(inputGender.getEditText().getText().toString().trim());
    actualuser.setAge(inputAge.getEditText().getText().toString().trim());
    if (isNewUser) {
      facade.newUser(actualuser);
    } else {
      facade.updateUser(actualuser);
    }
  }

  private boolean validateFirstName() {
    String firstName = inputFirstName.getEditText().getText().toString().trim();
    if (firstName.isEmpty()) {
      inputFirstName.setError("Geben sie Ihren Vornamen ein");
      return false;
    }
    if (firstName.length() > 10) {
      inputFirstName.setError("Sie können maximal 10 Zeichen eingeben");
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
    if (lastName.length() > 15) {
      inputFirstName.setError("Sie können maximal 15 Zeichen eingeben");
      return false;
    }
    return true;
  }

  private void getUserViewElements(View view) {
    inputFirstName = view.findViewById(R.id.firstName);
    inputLastName = view.findViewById(R.id.lastName);
    inputAge = view.findViewById(R.id.age);
    inputGender = view.findViewById(R.id.gender);

    inputFirstName.getEditText().setText(actualuser.getFirstName());
    inputLastName.getEditText().setText(actualuser.getLastName());
    inputAge.getEditText().setText(actualuser.getAge());
    inputGender.getEditText().setText(actualuser.getGender());
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  protected boolean allValidate() {
    return !(!validateLastName() | !validateFirstName());
  }
}
