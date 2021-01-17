package com.example.ajinafro.carpool;

import android.os.Bundle;

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
        return inflater.inflate(R.layout.fragment_carpool, container, false);
    }
}