package ch.lfg.lfg_source.main.edit;

import android.Manifest;
import android.content.Context;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.main.MainActivity;
import ch.lfg.lfg_source.main.MainFacade;

public class GroupEditFragment extends EditFragment {

  private static final int LOCATION_PERMISSION = 42;
  private static final int MAX_GROUPNAME_LENGTH = 10;
  private User groupAdminUser;
  private Group actualGroup;
  private Boolean isNewGroup = false;

  private LocationManager locationManager;
  private Button addLocationButton;
  private TextView showLocation;
  private Location originLocation;

  private MainFacade facade;
  private Button deleteButton;
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
    facade = new MainFacade((MainActivity) getActivity());
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
    setDeleteButton();
    addLocation();
    super.setUpTagContainer();

    return view;
  }

  private void addLocation() {
    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

    addLocationButton.setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            if ((ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
              ActivityCompat.requestPermissions(
                  getActivity(),
                  new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                  LOCATION_PERMISSION);
            } else {
              if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(
                        getActivity(), getString(R.string.gpsDeactivated), Toast.LENGTH_SHORT)
                    .show();
              } else {
                originLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (originLocation == null) {
                  Toast.makeText(getActivity(), getString(R.string.gpsError), Toast.LENGTH_SHORT)
                      .show();
                } else {
                  onLocationChanged(originLocation);
                }
              }
            }
          }
        });
  }

  private void onLocationChanged(Location location) {
    double longitude = location.getLongitude();
    double latitude = location.getLatitude();
    Geocoder geocoder = new Geocoder(getActivity(), Locale.GERMAN);
    List<Address> addresses;
    try {
      addresses = geocoder.getFromLocation(latitude, longitude, 10);
      if (addresses.size() > 0) {
        for (Address adr : addresses) {
          if (adr.getLocality() != null && adr.getLocality().length() > 0) {
            showLocation.setText(adr.getLocality());
            actualGroup.setLocation(adr.getLocality());
            break;
          }
        }
      }
    } catch (IOException e) {
      showLocation.setText(getString(R.string.retry));
    }
    showLocation.setText(getString(R.string.gpsNoLocation));
  }

  private void setDeleteButton() {
    deleteButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            facade.deleteGroup(actualGroup);
            goToHome(groupAdminUser);
          }
        });
  }

  @Override
  protected void update() {
    actualGroup.setDescription(super.getInputDescriptionString());
    actualGroup.setActive(super.getActiveState());
    actualGroup.setName(inputGroupName.getEditText().getText().toString().trim());
    actualGroup.setPhoneNumber(super.getInputPhone());
    actualGroup.setEmail(super.getInputEmail());
    actualGroup.setTags(super.getTags());
    if (isNewGroup) {
      facade.newGroup(actualGroup);
    } else {
      facade.updateGroup(actualGroup);
    }
  }

  private void getGroupViewElements(View view) {
    showLocation = view.findViewById(R.id.location);
    inputGroupName = view.findViewById(R.id.groupName);
    deleteButton = view.findViewById(R.id.delete_button);
    addLocationButton = view.findViewById(R.id.button_location);
    if (isNewGroup) {
      deleteButton.setVisibility(View.GONE);
    }
    inputGroupName.getEditText().setText(actualGroup.getName());
    showLocation.setText(actualGroup.getLocation());
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  private boolean validateGroupName() {
    String firstName = inputGroupName.getEditText().getText().toString().trim();
    if (firstName.isEmpty()) {
      inputGroupName.setError(getString(R.string.inputGroupName));
      return false;
    }
    if (firstName.length() > MAX_GROUPNAME_LENGTH) {
      inputGroupName.setError(
          String.format(getString(R.string.maxCharacter), MAX_GROUPNAME_LENGTH));
      return false;
    }
    return true;
  }

  @Override
  protected boolean allValidate() {
    return validateGroupName();
  }
}
