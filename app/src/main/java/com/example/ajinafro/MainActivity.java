package com.example.ajinafro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ajinafro.carpool.CarpoolFragment;
import com.example.ajinafro.home.HomeFragment;
import com.example.ajinafro.newPost.NewPostFragment;
import com.example.ajinafro.profile.ProfileFragment;
import com.example.ajinafro.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        ref=getSharedPreferences("ajinsafro",MODE_PRIVATE);
        UserUid=ref.getString("userUid"," ");
        Log.d(TAG,"userID =>{"+UserUid+" }");
    }
private static String TAG= "MainActivity : ";
    SharedPreferences ref;
    FirebaseUser currentUser;
    String UserUid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setItemIconTintList(null);
        bottomNav.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container,
                        new HomeFragment())
                .commit();
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener
            navListener
            = item -> {
                // By using switch we can easily get
                // the selected fragment
                // by using there id.
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;

                    case R.id.covoiturage:
                        selectedFragment = new CarpoolFragment();
                        break;

                    case R.id.newpost:
                        //Intent newPost =new Intent(this,NewPostActivity.class);
                        selectedFragment = new NewPostFragment();
                        break;

                    case R.id.search:
                        selectedFragment = new SearchFragment();
                        break;

                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                // It will help to replace the one fragment to other.
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragment_container,
                                selectedFragment)
                        .commit();
                return true;
            };
}
