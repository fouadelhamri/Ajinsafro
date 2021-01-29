package com.example.ajinafro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.example.ajinafro.models.UserAccountDetails;
import com.example.ajinafro.newPost.NewPostFragment;
import com.example.ajinafro.profile.ProfileFragment;
import com.example.ajinafro.search.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
private String filter_city=null;
private ArrayList<String> filter_categories=new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        ref=getSharedPreferences("ajinsafro",MODE_PRIVATE);
        UserUid=ref.getString("userUid"," ");
        Log.d(TAG,"userID =>{"+UserUid+" }");

        db=FirebaseFirestore.getInstance();
        Log.d(TAG,"userID =>{"+UserUid+" }");
        //get user account details;
        getUserAccountDetails(UserUid);
    }
    private static String TAG= "MainActivity : ";
    SharedPreferences ref;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    String UserUid;
    UserAccountDetails userAccountDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupNavigationBottom();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: returned");
        if(requestCode==4 && resultCode== Activity.RESULT_OK){
            filter_city=data.getExtras().getString("selectedCity");
            filter_categories=data.getExtras().getStringArrayList("selectedCategories");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragment_container,
                            new SearchFragment(filter_city,filter_categories))
                    .commit();        }
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
    void setupNavigationBottom(){
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
    void getUserAccountDetails(String userUid){
        String userjson = ref.getString("userAccountDetails","empty");
        if (userjson.equals("empty")){
            getUserAccountDetailsFromDB(UserUid);
        }else {
            Gson gson=new Gson();
            userAccountDetails=gson.fromJson(userjson,UserAccountDetails.class);
            Log.d(TAG,"userDetails =>"+userAccountDetails.toString());
        }
    }
    void getUserAccountDetailsFromDB(String userUid){
        db.collection("users_details").document(userUid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userAccountDetails = documentSnapshot.toObject(UserAccountDetails.class);
                Log.d(TAG,"userDetails =>"+userAccountDetails.toString());
                Gson gson = new Gson();
                String userJson = gson.toJson(userAccountDetails);
                ref.edit().putString("userAccountDetails", userJson).apply();
            }
        });
    }

}
