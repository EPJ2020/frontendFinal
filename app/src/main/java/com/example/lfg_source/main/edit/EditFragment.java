package com.example.lfg_source.main.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.main.home.HomeFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class EditFragment extends Fragment {
  private TextInputLayout inputEmail;
  private TextInputLayout inputPhone;
  private TextInputLayout inputDescription;
  private Button save;
  private Button cancel;
  private TagContainerLayout mTagContainerLayout;
  private TextInputLayout textTag;
  private Button btnAddTag;
  private Switch active;
  private ImageButton info;

  ArrayList<String> tags = new ArrayList<String>();

  protected void setUpTagContainer() {
    mTagContainerLayout.setTags(tags);
    mTagContainerLayout.setOnTagClickListener(
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
            .setMessage("Das Tag wird gelöscht!")
            .setPositiveButton(
                "Löschen",
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    if (position < mTagContainerLayout.getChildCount()) {
                      mTagContainerLayout.removeTag(position);
                    }
                  }
                })
            .setNegativeButton(
                "Abbrechen",
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
    save.setOnClickListener(
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
            if (textTag.getEditText().getText().toString().isEmpty()) {
              textTag.setError("Geben Sie ein Tag ein und klicken Sie anschliessend auf +");
            } else {
              mTagContainerLayout.addTag(textTag.getEditText().getText().toString());
              textTag.getEditText().setText("");
              textTag.setError("");
            }
          }
        });
    cancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            goToHome(loggedInUserOrGroupAdmin);
          }
        });
    info.setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            AlertDialog dialog =
                new AlertDialog.Builder(getActivity())
                    .setTitle("Tag Info")
                    .setMessage(
                        "Bitte erfassen Sie hier Tag's. Diese Tag's werden von unserem "
                            + "Algorithmus verwendet um Ihre Vorschläge zu berechnen.\n"
                            + "Damit möglichst passende Vorschläge generiert werden können, bitten "
                            + "wir Sie um 3-20 aussagekräftige und passende Tag's")
                    .create();
            dialog.show();
          }
        });
  }

  protected void goToHome(User loggedInUserOrGroupAdmin) {
    Fragment newFragment = new HomeFragment(loggedInUserOrGroupAdmin);
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
      inputEmail.setError("Geben sie Ihre EmailAdresse oder Telefonnummer ein");
      inputPhone.setError("Geben sie Ihre EmailAdresse oder Telefonnummer ein");
      validate = false;
    }
    if (!email.isEmpty() && !email.contains("@")) {
      inputEmail.setError("Geben sie eine gültige EmailAdresse ein");
      validate = false;
    }
    return validate;
  }

  protected boolean validateTags() {
    if (mTagContainerLayout.getTags().size() > 20 || mTagContainerLayout.getTags().size() < 3) {
      textTag.setError("Geben Sie zwischen 3 und 20 Tags ein");
      return false;
    }
    return true;
  }

  protected void getViewElements(View view) {
    inputEmail = view.findViewById(R.id.email);
    inputPhone = view.findViewById(R.id.phoneNumber);
    inputDescription = view.findViewById(R.id.description);
    save = view.findViewById(R.id.save_button);
    cancel = view.findViewById(R.id.cancel_button);
    mTagContainerLayout = (TagContainerLayout) view.findViewById(R.id.tagcontainerLayout);
    btnAddTag = (Button) view.findViewById(R.id.button_tag);
    textTag = view.findViewById(R.id.text_tag);
    active = view.findViewById(R.id.active);
    info = view.findViewById(R.id.button_info);
  }

  protected void setValues(
      String description,
      ArrayList<String> mytags,
      String email,
      String phoneNumber,
      boolean actived) {
    if (description != null) {
      inputDescription.getEditText().setText(description);
    }
    if (mytags != null) {
      for (String tag : mytags) {
        tags.add(tag);
      }
      active.setChecked(actived);
    }
    if (email != null) {
      inputEmail.getEditText().setText(email);
    }
    if (phoneNumber != null) {
      inputPhone.getEditText().setText(phoneNumber);
    }
  }

  protected boolean getActiveState() {
    return active.isChecked();
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
    return (ArrayList<String>) mTagContainerLayout.getTags();
  }
}
