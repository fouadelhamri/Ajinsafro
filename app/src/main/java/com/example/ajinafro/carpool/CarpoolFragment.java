package com.example.ajinafro.carpool;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ajinafro.R;


public class CarpoolFragment extends Fragment {

    public CarpoolFragment() {
        // Required empty public constructor
    }


    public static CarpoolFragment newInstance(String param1, String param2) {
        CarpoolFragment fragment = new CarpoolFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_carpool, container, false);
        ConstraintLayout addnewcarpoolBtn=view.findViewById(R.id.newcarpool_donebtn);
        addnewcarpoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newcarpool=new Intent(getActivity().getApplicationContext(),NewCarpoolCitiesPickerActivity.class);
                getActivity().startActivity(newcarpool);
            }
        });
        return view;
    }
}