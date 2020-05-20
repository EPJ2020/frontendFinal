package ch.lfg.lfg_source.main.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.main.MainActivity;
import ch.lfg.lfg_source.main.MainFacade;

public class UserEditFragment extends EditFragment {

  private static final int MAX_FIRSTNAME_LENGTH = 10;
  private static final int MAX_LASTNAME_LENGTH = 15;

  private User actualUser;
  private Boolean isNewUser = false;
  private MainFacade facade;

  private TextInputLayout inputFirstName;
  private TextInputLayout inputLastName;
  private TextInputLayout inputAge;
  private TextInputLayout inputGender;

  public UserEditFragment() {
    super();
    this.actualUser = new User();
    isNewUser = true;
  }

  public UserEditFragment(User loggedInUser) {
    super();
    actualUser = loggedInUser;
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
        actualUser.getDescription(),
        actualUser.getTags(),
        actualUser.getEmail(),
        actualUser.getPhone(),
        actualUser.getActive());
    if (isNewUser) {
      super.disableCancelButton();
    }
    super.setButtons(actualUser);
    super.setUpTagContainer();
    return view;
  }

  @Override
  protected void update() {
    actualUser.setDescription(super.getInputDescriptionString());
    actualUser.setActive(super.getActiveState());
    actualUser.setLastName(inputLastName.getEditText().getText().toString().trim());
    actualUser.setFirstName(inputFirstName.getEditText().getText().toString().trim());
    actualUser.setPhone(super.getInputPhone());
    actualUser.setEmail(super.getInputEmail());
    actualUser.setTags(super.getTags());
    actualUser.setGender(inputGender.getEditText().getText().toString().trim());
    actualUser.setAge(inputAge.getEditText().getText().toString().trim());
    if (isNewUser) {
      facade.newUser(actualUser);
    } else {
      facade.updateUser(actualUser);
    }
  }

  private boolean validateFirstName() {
    String firstName = inputFirstName.getEditText().getText().toString().trim();
    if (firstName.isEmpty()) {
      inputFirstName.setError(getString(R.string.inputFirstname));
      return false;
    }
    if (firstName.length() > MAX_FIRSTNAME_LENGTH) {
      inputFirstName.setError(
          String.format(getString(R.string.maxCharacter), MAX_FIRSTNAME_LENGTH));
      return false;
    }
    return true;
  }

  private boolean validateLastName() {
    String lastName = inputLastName.getEditText().getText().toString().trim();
    if (lastName.isEmpty()) {
      inputLastName.setError(getString(R.string.inputLastname));
      return false;
    }
    if (lastName.length() > MAX_LASTNAME_LENGTH) {
      inputLastName.setError(String.format(getString(R.string.maxCharacter), MAX_LASTNAME_LENGTH));
      return false;
    }
    return true;
  }

  private void getUserViewElements(View view) {
    inputFirstName = view.findViewById(R.id.firstName);
    inputLastName = view.findViewById(R.id.lastName);
    inputAge = view.findViewById(R.id.age);
    inputGender = view.findViewById(R.id.gender);

    inputFirstName.getEditText().setText(actualUser.getFirstName());
    inputLastName.getEditText().setText(actualUser.getLastName());
    inputAge.getEditText().setText(actualUser.getAge());
    inputGender.getEditText().setText(actualUser.getGender());
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
