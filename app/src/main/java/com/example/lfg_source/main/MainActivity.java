package com.example.lfg_source.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.login.Login;
import com.example.lfg_source.main.edit.UserEditFragment;
import com.example.lfg_source.main.home.HomeFragment;
import com.example.lfg_source.main.match.MatchFragment;
import com.example.lfg_source.main.swipe.GroupSwipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
  private static final int requestCode = 1;
  private HomeFragment homeFragment = null;
  private Fragment selectedFragment;
  private String token;
  private User loggedInUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().setTitle("LFG Home");

    SharedPreferences preferences = this.getSharedPreferences("LFG", Context.MODE_PRIVATE);
    preferences.edit().putString("USERTOKEN", null).apply();
    token = preferences.getString("USERTOKEN", null);

    if(token == null){
      Intent i = new Intent(getApplicationContext(), Login.class);
      startActivityForResult(i, requestCode);
    }
    else{
      setup();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == this.requestCode) {
      if (resultCode == Activity.RESULT_OK) {
        token = data.getStringExtra("usertoken");
        SharedPreferences preferences = this.getSharedPreferences("LFG", Context.MODE_PRIVATE);
        preferences.edit().putString("USERTOKEN", token).apply();
        setup();
      }
    }
  }

  private void setup() {
    MainViewModel mainViewModel = new MainViewModel();
    mainViewModel.getLoginUser().observe(this, new Observer<User>() {
      @Override
      public void onChanged(User user) {
        loggedInUser = user;
        finishSetup();
      }
    });
    mainViewModel.sendMessageUser(token);
  }

  private void finishSetup(){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    if (loggedInUser == null) {
      UserEditFragment userEditFragment = new UserEditFragment(token);
      fragmentTransaction.add(R.id.fragment_container, userEditFragment);
      fragmentTransaction.commit();
    } else {
      homeFragment = new HomeFragment(token, loggedInUser);
      fragmentTransaction.add(R.id.fragment_container, homeFragment);
      fragmentTransaction.commit();
    }

    BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_swipe:
                // TODO
                selectedFragment = new GroupSwipeFragment(loggedInUser.getId());
                break;
              case R.id.action_Matches:
                getSupportActionBar().setTitle("Match");
                // TODO
                selectedFragment = new MatchFragment(loggedInUser);
                break;
              default:
                getSupportActionBar().setTitle("LFG Home");
                selectedFragment = new HomeFragment(token, loggedInUser);;
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
}
