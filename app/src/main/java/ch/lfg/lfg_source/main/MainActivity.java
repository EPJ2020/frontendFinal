package ch.lfg.lfg_source.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.login.Login;
import ch.lfg.lfg_source.main.edit.UserEditFragment;
import ch.lfg.lfg_source.main.home.HomeFragment;
import ch.lfg.lfg_source.main.match.MatchGroupFragment;
import ch.lfg.lfg_source.main.match.MatchUserFragment;
import ch.lfg.lfg_source.main.swipe.GroupSwipeFragment;
import ch.lfg.lfg_source.main.swipe.SwipeFragment;
import ch.lfg.lfg_source.main.swipe.UserSwipeFragment;

public class MainActivity extends AppCompatActivity {
  private Toolbar toolbar;
  private Spinner spinner;
  private Button helpButton;
  private Button logoutButton;
  private TextView homeText;
  private MainFacade facade;
  private ShowcaseView showCase;

  private static final int REQUEST_CODE = 1;
  private Fragment selectedFragment;
  private String token;
  private User loggedInUser;
  private Boolean isMatchFragment = false;
  private List<Object> spinnerList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initGuiElements();

    SharedPreferences preferences =
        this.getSharedPreferences(
            getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
    token = preferences.getString(getResources().getString(R.string.usertoken), null);

    if (token == null) {
      Intent i = new Intent(getApplicationContext(), Login.class);
      startActivityForResult(i, REQUEST_CODE);
    } else {
      initObserver();
    }
  }

  private void initGuiElements() {
    toolbar = findViewById(R.id.toolbar);
    spinner = findViewById(R.id.spinner);
    helpButton = findViewById(R.id.help_button);
    logoutButton = findViewById(R.id.logoutButton);
    homeText = findViewById(R.id.homeText);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == this.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      token = data.getStringExtra(getResources().getString(R.string.usertoken));
      SharedPreferences preferences =
          this.getSharedPreferences(
              getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
      preferences.edit().putString(getResources().getString(R.string.usertoken), token).apply();
      initObserver();
    }
  }

  public void setUser(User user) {
    loggedInUser = user;
    finishSetupMainActivity();
  }

  private void initObserver() {
    facade = new MainFacade(this);
    facade
        .getService()
        .getLoginUser()
        .observe(
            this,
            new Observer<User>() {
              @Override
              public void onChanged(User user) {
                loggedInUser = user;
                finishSetupMainActivity();
              }
            });
    facade
        .getService()
        .getMyGroups()
        .observe(
            this,
            new Observer<List<Group>>() {
              @Override
              public void onChanged(List<Group> myGroups) {
                spinnerList.clear();
                spinnerList.addAll(myGroups);
                setupToolbar();
              }
            });
    facade
        .getService()
        .getLoginFailMessage()
        .observe(
            this,
            new Observer<String>() {
              @Override
              public void onChanged(String s) {
                logout();
              }
            });
    facade.getMyProfile();
  }

  private void finishSetupMainActivity() {
    HomeFragment homeFragment = null;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    setHelpButtonListener();
    setLogoutButtonListener();

    if (loggedInUser == null) {
      setNullToolbar(getString(R.string.createProfile), false);
      UserEditFragment userEditFragment = new UserEditFragment();
      fragmentTransaction.add(R.id.fragment_container, userEditFragment);
      fragmentTransaction.commit();
    } else {
      setupSpinnerListener();
      homeFragment = new HomeFragment(loggedInUser);
      fragmentTransaction.add(R.id.fragment_container, homeFragment);
      fragmentTransaction.commit();

      initBottomNavigation();
    }
  }

  private void setHelpButtonListener() {
    helpButton.setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            showHelp(
                getString(R.string.selectProfileOrGroupHelper), MainActivity.this, R.id.spinner);
          }
        });
  }

  private void setLogoutButtonListener() {
    logoutButton.setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            logout();
          }
        });
  }

  private void logout() {
    SharedPreferences preferences =
        getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
    preferences.edit().putString(getResources().getString(R.string.usertoken), null).apply();
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(intent);
    finish();
    Runtime.getRuntime().exit(0);
  }

  private void initBottomNavigation() {
    BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_swipe:
                facade.getMyGroups();
                isMatchFragment = false;
                selectedFragment = new GroupSwipeFragment(loggedInUser);
                hideLogout();
                break;
              case R.id.action_Matches:
                facade.getMyGroups();
                isMatchFragment = true;
                selectedFragment = new MatchGroupFragment();
                hideLogout();
                break;
              default:
                selectedFragment = new HomeFragment(loggedInUser);
                isMatchFragment = false;
                break;
            }
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
            return true;
          }
        });
  }

  private void hideLogout() {
    logoutButton.setVisibility(View.GONE);
    homeText.setVisibility(View.GONE);
  }

  private void showHelp(String text, Activity activity, int item) {
    // Code Functionality ShowcaseView from https://github.com/amlcurran/ShowcaseView
    showCase =
        new ShowcaseView.Builder(MainActivity.this)
            .setStyle(R.style.CustomShowcaseTheme)
            .setTarget(new ViewTarget(item, activity))
            .setContentTitle(getString(R.string.dropdown))
            .setContentText(text)
            .setOnClickListener(
                new View.OnClickListener() {
                  public void onClick(View v) {
                    Fragment fragment =
                        MainActivity.this
                            .getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_container);
                    if (fragment instanceof SwipeFragment) {
                      showHelperShowcase(
                          getString(R.string.swipeHelper),
                          fragment.getActivity(),
                          R.id.circularProgressbar);

                    } else {
                      showHelperShowcase(
                          getString(R.string.matchHelper),
                          fragment.getActivity(),
                          R.id.yourMatchesList);
                    }
                  }
                })
            .hideOnTouchOutside()
            .withHoloShowcase()
            .build();
  }

  private void showHelperShowcase(String text, Activity activity, int item) {
    // Code Functionality ShowcaseView from https://github.com/amlcurran/ShowcaseView
    showCase.hide();
    new ShowcaseView.Builder(MainActivity.this)
        .setStyle(R.style.CustomShowcaseTheme)
        .setTarget(new ViewTarget(item, activity))
        .setContentTitle(getString(R.string.whatIsThis))
        .setContentText(text)
        .hideOnTouchOutside()
        .withHoloShowcase()
        .build();
  }

  private void setupSpinnerListener() {
    spinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Object item = parent.getItemAtPosition(pos);
            hideLogout();
            if (item instanceof Group && isMatchFragment) {
              final Group group = (Group) item;
              selectedFragment = new MatchUserFragment(group);
            }
            if (item instanceof User && isMatchFragment) {
              selectedFragment = new MatchGroupFragment();
            }
            if (item instanceof Group && !isMatchFragment) {
              final Group group = (Group) item;
              selectedFragment = new UserSwipeFragment(group);
            }
            if (item instanceof User && !isMatchFragment) {
              selectedFragment = new GroupSwipeFragment(loggedInUser);
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, selectedFragment);
            fragmentTransaction.commit();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
  }

  public void setupToolbar() {
    spinnerList.add(loggedInUser);
    ArrayAdapter<Object> adapter =
        new ArrayAdapter<Object>(getApplicationContext(), R.layout.spin_item, spinnerList);
    adapter.setDropDownViewResource(R.layout.spin_dropdown_item);
    spinner.setVisibility(View.VISIBLE);
    helpButton.setVisibility(View.VISIBLE);
    spinner.setAdapter(adapter);
    Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    if (fragment instanceof HomeFragment) {
      spinner.setSelection(((HomeFragment) fragment).getSelectedGroupPosition());
    } else {
      spinner.setSelection(spinnerList.size() - 1);
    }
  }

  public void setNullToolbar(String title, boolean isHomeFragment) {
    spinner.setVisibility(View.GONE);
    helpButton.setVisibility(View.GONE);
    logoutButton.setVisibility(View.GONE);
    homeText.setVisibility(View.GONE);
    toolbar.setTitle(title);
    if (isHomeFragment) {
      logoutButton.setVisibility(View.VISIBLE);
      homeText.setVisibility(View.VISIBLE);
    }
    isMatchFragment = false;
  }

  public MainFacade getMainFacade() {
    return facade;
  }

  @Override
  public void onBackPressed() {
    moveTaskToBack(false);
  }
}
