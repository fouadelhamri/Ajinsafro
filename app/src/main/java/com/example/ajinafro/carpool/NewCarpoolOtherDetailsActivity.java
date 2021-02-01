package com.example.ajinafro.carpool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.ajinafro.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCarpoolOtherDetailsActivity extends AppCompatActivity {
    private static final String TAG ="CarpoolOtherDetails" ;
    private Integer nb_places;
    private Double price;
    private String description;
    ConstraintLayout done_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_carpool_other_details);
        done_btn=findViewById(R.id.newcarpool_donebtn);
        ButterKnife.bind(this);
    }


    @BindView(R.id.newcarpool_place_number)
    EditText places_nb_field;

    @OnClick(R.id.newcarpool_addplacebtn)
    void increase_places(){
    Integer nb_place= Integer.parseInt(places_nb_field.getText().toString());
    if(nb_place==4){
        places_nb_field.setText(String.valueOf(0));
    }else {
        places_nb_field.setText(String.valueOf(nb_place+1));
    }
    }

    @OnClick(R.id.newcarpool_decreaseplacebtn)
    void decrease_places(){
        Integer nb_place= Integer.parseInt(places_nb_field.getText().toString());
        if(nb_place==0){

        }else {
            places_nb_field.setText(String.valueOf(nb_place-1));
        }
    }

    @OnClick(R.id.newcarpool_donebtn)
    void share_carpool(){
        Snackbar snackbar=Snackbar.make(done_btn,"Attendez...", Snackbar.LENGTH_LONG);
        snackbar.show();
        Log.d(TAG, "share_carpool: details nombre de places est "+nb_places+"\n le prix est "+price);
    }
}