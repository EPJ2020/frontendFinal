package com.example.lfg_source.main.edit;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GroupEditFragment extends EditFragment {

  private static final int LOCATION_PERMISSION = 42;
  User groupAdminUser;
  private GroupEditViewModel mViewModel;
  private Group actualGroup;
  private Boolean isNewGroup = false;
  private Button delete;
  private LocationManager locationManager;
  private Button addLogationButton;
  private String token;

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  public TextView showLocation;

  private Location originLocation;

  private TextInputLayout inputGroupName;

  public GroupEditFragment(User groupAdminUser, String token) {
    super();
    this.groupAdminUser = groupAdminUser;
    this.actualGroup = new Group(groupAdminUser.getId());
    isNewGroup = true;
    this.token = token;
  }

  public GroupEditFragment(Group group, User groupAdminUser, String token) {
    super();
    this.groupAdminUser = groupAdminUser;
    actualGroup = group;
    this.token = token;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.group_edit_fragment, container, false);
    super.setToken(token);
    super.getViewElements(view);
    getGroupViewElements(view);
    super.setValues(
        actualGroup.getDescription(),
        actualGroup.getTags(),
        actualGroup.getEmail(),
        actualGroup.getPhoneNumber(),
        actualGroup.getActive());
    super.setButtons(groupAdminUser);
    setDeleteButton();
    addLocation();
    super.setUpTagContainer();

    return view;
  }

  private void addLocation() {
    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

    addLogationButton.setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            if (!(ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
              ActivityCompat.requestPermissions(
                  getActivity(),
                  new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                  LOCATION_PERMISSION);
            } else {
              if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getActivity(), "GPS ist deaktiviert", Toast.LENGTH_SHORT).show();
              } else {
                originLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (originLocation == null) {
                  Toast.makeText(getActivity(), "GPS Verbindung Fehlgeschlagen", Toast.LENGTH_SHORT)
                      .show();
                } else {
                  onLocationChanged(originLocation);
                }
              }
            }
          }
        });
  }

  public void onLocationChanged(Location location) {
    double longitude = location.getLongitude();
    double latitude = location.getLatitude();
    Geocoder geocoder = new Geocoder(getActivity(), Locale.GERMAN);
    List<Address> adresses;
    try {
      adresses = geocoder.getFromLocation(latitude, longitude, 10);
      if (adresses.size() > 0) {
        for (Address adr : adresses) {
          if (adr.getLocality() != null && adr.getLocality().length() > 0) {
            showLocation.setText(adr.getLocality());
            actualGroup.setLocation(adr.getLocality());
            break;
          }
        }
      }
    } catch (IOException e) {
      showLocation.setText("Versuchen Sie es noch einmal");
    }
    showLocation.setText("Konnte aktuell keine Ortschaft ermitteln");
  }

  private void setDeleteButton() {
    delete.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            final String url = "http://152.96.56.38:8080/Group/" + actualGroup.getGroupId();
            RestClientDeleteGroup task = new RestClientDeleteGroup();
            task.execute(url);
            goToHome(groupAdminUser);
          }
        });
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
    showLocation = view.findViewById(R.id.location);
    inputGroupName = view.findViewById(R.id.groupName);
    delete = view.findViewById(R.id.delete_button);
    addLogationButton = view.findViewById(R.id.button_location);
    if (isNewGroup) {
      delete.setVisibility(View.GONE);
    }
    inputGroupName.getEditText().setText(actualGroup.getName());
    showLocation.setText(actualGroup.getLocation());
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
    if (firstName.length() > 10) {
      inputGroupName.setError("Sie können maximal 10 Zeichen eingeben");
      return false;
    }
    return true;
  }

  @Override
  protected boolean allValidate() {
    return !(!validateGroupName());
  }
}
