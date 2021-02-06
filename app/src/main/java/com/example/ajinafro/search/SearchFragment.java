package com.example.ajinafro.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ajinafro.FiltersPickerActivity;
import com.example.ajinafro.R;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    public SearchFragment() {
    }
    private static final String TAG ="SearchFragment" ;
    public String selectedCity;
    public ArrayList<String> selectedCategories;
    public SearchFragment(String selectedCity, ArrayList<String> selectedCategories) {
        this.selectedCity=selectedCity;
        this.selectedCategories=selectedCategories;
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String selectedCity,ArrayList<String> selectedCategories) {
        SearchFragment fragment = new SearchFragment(selectedCity,selectedCategories);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: fragment selected city "+this.selectedCity);
        if(selectedCategories!=null){
            Log.d(TAG, "onCreate: fragment selected categories "+this.selectedCategories.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        ImageView filterbtn=view.findViewById(R.id.searchFragment_filter_btn);
        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filtreAc=new Intent(getActivity().getApplicationContext(),FiltersPickerActivity.class);
                getActivity().startActivityForResult(filtreAc,4);
            }
        });
        return view;
    }


}