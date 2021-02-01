package com.example.ajinafro.carpool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ajinafro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCarpoolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_carpool);
        ButterKnife.bind(this);
    }

    @BindView(R.id.searchcarpool_startcity)
    EditText startcity_field;
    @BindView(R.id.searchcarpool_endcity)
    EditText endcity_field;
    @BindView(R.id.searchcarpool_switchbtn)
    ImageButton switchbtn;
}