package com.example.ajinafro.newPost;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ajinafro.R;
import com.example.ajinafro.adapters.CategoriesAdapter;
import com.example.ajinafro.profile.EditProfile;
import com.example.ajinafro.utils.VerticalSpacingItemDecorator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment {

    TextView option_name;
    private ArrayList<String> optionsList;
    GridView mListView;
    ImageButton addBtn;

    public NewPostFragment() {
        // Required empty public constructor
    }

    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_new_post , container, false);
        RecyclerView categories_recycleview=(RecyclerView) view.findViewById(R.id.option_list);

        ArrayList<String> cat_name = new ArrayList<>();
        cat_name.add("cafe");
        cat_name.add("hotel");
        cat_name.add("restaurant");
        cat_name.add("sushi");
        cat_name.add("monument historique");
        ArrayList<Integer> cat_img=new ArrayList<>();
        cat_img.add(R.drawable.coffe_categorie_image);
        cat_img.add(R.drawable.hotel_categorie_image);
        cat_img.add(R.drawable.restaurant_categorie_image);
        cat_img.add(R.drawable.sushi_categorie_image);
        cat_img.add(R.drawable.monument_categorie_image);

        CategoriesAdapter categoriesAdapter=new CategoriesAdapter(getActivity().getApplicationContext(),cat_name ,cat_img);
        categories_recycleview.setAdapter(categoriesAdapter);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(40);
        categories_recycleview.addItemDecoration(itemDecorator);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        categories_recycleview.setLayoutManager(layoutManager);
        // Inflate the layout for this fragment
        return view;
    }

}