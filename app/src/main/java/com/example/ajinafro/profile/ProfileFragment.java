package com.example.ajinafro.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ajinafro.R;
import com.example.ajinafro.models.City;
import com.example.ajinafro.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    private static final String TAG ="PROFILE Fragment : " ;
    private FirebaseFirestore db;
    private SharedPreferences ref;
    private String UserUid;
    private QuerySnapshot result;

    public ProfileFragment() {
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        db=FirebaseFirestore.getInstance();
        ref=getActivity().getSharedPreferences("ajinsafro",MODE_PRIVATE);
        UserUid=ref.getString("userUid"," ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        Button edit_button=(Button) view.findViewById(R.id.fragment_profile_editprofile_btn);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_profil_clicked();
            }
        });
        return view;
    }

    void edit_profil_clicked(){
        Intent nextpage= new Intent(getActivity().getApplicationContext(),EditProfile.class);
        getActivity().startActivity(nextpage);
    }




}