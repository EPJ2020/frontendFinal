package com.example.lfg_source.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.main.home.HomeFragment;
import com.example.lfg_source.main.match.MatchFragment;
import com.example.lfg_source.main.swipe.GroupSwipeFragment;
import com.example.lfg_source.main.swipe.UserSwipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  public HomeFragment homeFragment = null;
  public Fragment selectedFragment;
  public Group selectedGroup = null;
  private User loggedInUser = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().setTitle("LFG Home");

    loggedInUser = new User();
    loggedInUser.setActive(true);
    loggedInUser.setId(1);
    loggedInUser.setLastName("Deller");
    loggedInUser.setFirstName("Kaniel");
    loggedInUser.setEmail("ggg@ggg.ch");
    ArrayList<String> tags = new ArrayList<>();
    tags.add("matchesAll");
    tags.add("laufen");
    tags.add("gaertnern");
    tags.add("c++");
    tags.add("blah");
    loggedInUser.setTags(tags);
    loggedInUser.setPhone("0773736366");

    homeFragment = new HomeFragment(loggedInUser);

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    fragmentTransaction.add(R.id.fragment_container, homeFragment);
    fragmentTransaction.commit();

    BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_swipe:
                if (selectedGroup == null) {
                  getSupportActionBar().setTitle("Swipe User");
                  selectedFragment = new GroupSwipeFragment(loggedInUser.getId());
                } else {
                  getSupportActionBar().setTitle("Swipe Gruppe: " + selectedGroup.getName());
                  selectedFragment = new UserSwipeFragment(selectedGroup);
                }
                break;
              case R.id.action_Matches:
                getSupportActionBar().setTitle("Match");
                selectedFragment = new MatchFragment(loggedInUser);
                break;
              default:
                getSupportActionBar().setTitle("LFG Home");
                selectedFragment = homeFragment;
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
