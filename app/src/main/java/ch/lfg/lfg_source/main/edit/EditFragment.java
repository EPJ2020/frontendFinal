package ch.lfg.lfg_source.main.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.main.MainActivity;
import ch.lfg.lfg_source.main.home.HomeFragment;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class EditFragment extends Fragment {
  private TextInputLayout inputEmail;
  private TextInputLayout inputPhone;
  private TextInputLayout inputDescription;
  private TextInputLayout inputTag;
  private Button saveButton;
  private Button cancelButton;
  private Button btnAddTag;
  private Switch activeSwitch;
  private ImageButton infoButton;
  private TagContainerLayout tagContainerLayout;

  private static final int MAX_AMOUNT_TAG = 20;
  private static final int MIN_AMOUNT_TAG = 3;

  ArrayList<String> tags = new ArrayList<>();

  protected void setUpTagContainer() {
    tagContainerLayout.setTags(tags);
    tagContainerLayout.setOnTagClickListener(
        new TagView.OnTagClickListener() {
          @Override
          public void onTagClick(final int position, String text) {
            showDeleteConfirmation(position, text);
          }

          @Override
          public void onTagLongClick(final int position, String text) {
            showDeleteConfirmation(position, text);
          }

          @Override
          public void onSelectedTagDrag(int position, String text) {}

          @Override
          public void onTagCrossClick(int position) {}
        });
  }

  private void showDeleteConfirmation(final int position, String text) {
    AlertDialog dialog =
        new AlertDialog.Builder(getActivity())
            .setTitle(text)
            .setMessage(getString(R.string.deleteTag))
            .setPositiveButton(
                getString(R.string.delete),
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    if (position < tagContainerLayout.getChildCount()) {
                      tagContainerLayout.removeTag(position);
                    }
                  }
                })
            .setNegativeButton(
                getString(R.string.abbrechen),
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                  }
                })
            .create();
    dialog.show();
  }

  protected void setButtons(final User loggedInUserOrGroupAdmin) {
    saveButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (!allValidate() | !validateTags() | !validateContact()) {
              return;
            }
            update();
            goToHome(loggedInUserOrGroupAdmin);
          }
        });
    btnAddTag.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (inputTag.getEditText().getText().toString().isEmpty()) {
              inputTag.setError(getString(R.string.addTagErrorMessage));
            } else {
              tagContainerLayout.addTag(inputTag.getEditText().getText().toString());
              inputTag.getEditText().setText("");
              inputTag.setError("");
            }
          }
        });
    cancelButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            goToHome(loggedInUserOrGroupAdmin);
          }
        });
    infoButton.setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            AlertDialog dialog =
                new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.tagInfoTitle))
                    .setMessage(getString(R.string.tagInfo))
                    .create();
            dialog.show();
          }
        });
  }

  protected void disableCancelButton() {
    cancelButton.setEnabled(false);
  }

  protected void goToHome(User loggedInUser) {
    ((MainActivity) getActivity()).setUser(loggedInUser);
    Fragment newFragment = new HomeFragment(loggedInUser);
    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, newFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  protected boolean allValidate() {
    return false;
  }

  protected void update() {}

  private boolean validateContact() {
    String email = inputEmail.getEditText().getText().toString().trim();
    String phone = inputPhone.getEditText().getText().toString().trim();
    boolean validate = true;
    if (email.isEmpty() && phone.isEmpty()) {
      inputEmail.setError(getString(R.string.inputContactInfoHelper));
      inputPhone.setError(getString(R.string.inputContactInfoHelper));
      validate = false;
    }
    if (!email.isEmpty() && !email.contains("@")) {
      inputEmail.setError(getString(R.string.inputEmailHelper));
      validate = false;
    }
    return validate;
  }

  protected boolean validateTags() {
    if (tagContainerLayout.getTags().size() > MAX_AMOUNT_TAG
        || tagContainerLayout.getTags().size() < MIN_AMOUNT_TAG) {
      inputTag.setError(getString(R.string.inputTagHelper));
      return false;
    }
    return true;
  }

  protected void getViewElements(View view) {
    inputEmail = view.findViewById(R.id.email);
    inputPhone = view.findViewById(R.id.phoneNumber);
    inputDescription = view.findViewById(R.id.description);
    saveButton = view.findViewById(R.id.save_button);
    cancelButton = view.findViewById(R.id.cancel_button);
    tagContainerLayout = view.findViewById(R.id.tagcontainerLayout);
    btnAddTag = view.findViewById(R.id.button_tag);
    inputTag = view.findViewById(R.id.text_tag);
    activeSwitch = view.findViewById(R.id.active);
    infoButton = view.findViewById(R.id.button_info);
  }

  protected void setValues(
      String description,
      ArrayList<String> myTags,
      String email,
      String phoneNumber,
      boolean isActive) {
    if (description != null) {
      inputDescription.getEditText().setText(description);
    }
    if (myTags != null) {
      for (String tag : myTags) {
        tags.add(tag);
      }
      activeSwitch.setChecked(isActive);
    }
    if (email != null) {
      inputEmail.getEditText().setText(email);
    }
    if (phoneNumber != null) {
      inputPhone.getEditText().setText(phoneNumber);
    }
  }

  protected boolean getActiveState() {
    return activeSwitch.isChecked();
  }

  protected String getInputDescriptionString() {
    return inputDescription.getEditText().getText().toString().trim();
  }

  protected String getInputEmail() {
    return inputEmail.getEditText().getText().toString().trim();
  }

  protected String getInputPhone() {
    return inputPhone.getEditText().getText().toString().trim();
  }

  protected ArrayList<String> getTags() {
    return (ArrayList<String>) tagContainerLayout.getTags();
  }
}
