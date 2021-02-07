package com.example.ajinafro.carpool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ajinafro.R;
import com.example.ajinafro.adapters.CarpoolsAdapter;
import com.example.ajinafro.models.Carpool;
import com.example.ajinafro.utils.CityPickerActivity;
import com.example.ajinafro.utils.VerticalSpacingItemDecorator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchCarpoolActivity extends AppCompatActivity {
    private static final int CITY_PICKER =888 ;
    private static final int CITY_PICKER2 =881 ;
    private FirebaseFirestore db;
    private ArrayList<Carpool> carpoolArrayList=new ArrayList<>();
    private static final String TAG = "SearchCarpoolActivity";
    private static final Date now= new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_carpool);
        ButterKnife.bind(this);
        db=FirebaseFirestore.getInstance();
        result_search.setVisibility(View.INVISIBLE);
        initRecycleview();
    }

    @BindView(R.id.carpool_offers_recycleview)
    RecyclerView carpools_recycleview;

    @OnClick(R.id.newcarpool_donebtn)
    void alert(){
        Snackbar snackbar=Snackbar.make(result_search,"Prochainement...", Snackbar.LENGTH_LONG);snackbar.show();
    }

    @BindView(R.id.searchcarpool_result_layout)
    ConstraintLayout result_search;

    @BindView(R.id.searchcarpool_startcity)
    EditText startcity_field;

    @BindView(R.id.recherche_btn)
    Button recherche;
    @OnClick(R.id.recherche_btn)
    void result(){
        result_carpool_search();
        //result_search.setVisibility(View.VISIBLE);
    }

    @BindView(R.id.searchcarpool_endcity)
    EditText endcity_field;
    @BindView(R.id.searchcarpool_switchbtn)
    ImageButton switchbtn;
    @OnClick(R.id.searchcarpool_switchbtn)
    void switchIt(){
      String a=startcity_field.getText().toString();
      String b=endcity_field.getText().toString();
      startcity_field.setText(b);
      endcity_field.setText(a);
    }
    @OnClick(R.id.searchcarpool_endcity)
    void cityPicker2(){
        Intent intent=new Intent(SearchCarpoolActivity.this,CityPickerActivity.class);
        startActivityForResult(intent,CITY_PICKER2);
    }


    @OnClick(R.id.searchcarpool_startcity)
    void cityPicker(){
    Intent intent=new Intent(SearchCarpoolActivity.this,CityPickerActivity.class);
    startActivityForResult(intent,CITY_PICKER);
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CITY_PICKER){

            if(resultCode == Activity.RESULT_OK){
                startcity_field.setText(data.getExtras().getString("selectedCity") );
            }
        }
        if(requestCode==CITY_PICKER2){
            if(resultCode == Activity.RESULT_OK){
                endcity_field.setText(data.getExtras().getString("selectedCity") );
            }
        }

    }

    void result_carpool_search(){
        String start_city=startcity_field.getText().toString();
        String end_city=endcity_field.getText().toString();
        if(!start_city.isEmpty() && !end_city.isEmpty()){
            searcByStartAndEnd(start_city,end_city);
        }
        else {
            if(!start_city.isEmpty() && end_city.isEmpty()){
                searchByStartCity(start_city);
            }else{
                if(start_city.isEmpty() && !end_city.isEmpty()){
                    searchByEndCity(end_city);
                }
            }
        }
    }

    private void searchByEndCity(String end_city) {
        result_search.setVisibility(View.INVISIBLE);
        db.collection("carpool")
                .whereEqualTo("end_city",end_city)
                .whereGreaterThanOrEqualTo("start_date",now )
                .orderBy("start_date")
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            carpoolArrayList=new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                if (documentSnapshot.exists()){
                                    carpoolArrayList.add(documentSnapshot.toObject(Carpool.class));
                                    Log.d(TAG, "onSuccess: Result "+carpoolArrayList.size());
                                }
                            }
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);

                        }else{
                            carpoolArrayList=new ArrayList<>();
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);
                            result_search.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onSuccess: noResultFound");
                        }
                    }
                });
    }

    private void searchByStartCity(String start_city) {
        result_search.setVisibility(View.INVISIBLE);
        db.collection("carpool")
                .whereEqualTo("start_city",start_city)
                .whereGreaterThanOrEqualTo("start_date",now )
                .orderBy("start_date")
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            carpoolArrayList=new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                if (documentSnapshot.exists()){
                                    carpoolArrayList.add(documentSnapshot.toObject(Carpool.class));
                                    Log.d(TAG, "onSuccess: Result "+carpoolArrayList.size());
                                }
                            }
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);

                        }else{
                            result_search.setVisibility(View.VISIBLE);
                            carpoolArrayList=new ArrayList<>();
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);
                            Log.d(TAG, "onSuccess: noResultFound");
                        }
                    }
                });
    }

    private void searcByStartAndEnd(String start_city, String end_city) {
        result_search.setVisibility(View.INVISIBLE);
        db.collection("carpool")
                .whereEqualTo("end_city",end_city)
                .whereEqualTo("start_city",start_city)
                .whereGreaterThanOrEqualTo("start_date",now )
                .orderBy("start_date")
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            carpoolArrayList=new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                if (documentSnapshot.exists()){
                                    carpoolArrayList.add(documentSnapshot.toObject(Carpool.class));
                                    Log.d(TAG, "onSuccess: Result "+carpoolArrayList.size());
                                }
                            }
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);

                        }else{
                            result_search.setVisibility(View.VISIBLE);
                            carpoolArrayList=new ArrayList<>();
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);
                            Log.d(TAG, "onSuccess: noResultFound");
                        }
                    }
                });
    }
    void initRecycleview(){
        result_search.setVisibility(View.INVISIBLE);
        db.collection("carpool").whereGreaterThanOrEqualTo("start_date",now )
                .orderBy("start_date").limit(20).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                if (documentSnapshot.exists()){
                                    carpoolArrayList.add(documentSnapshot.toObject(Carpool.class));
                                    Log.d(TAG, "onSuccess: Result "+carpoolArrayList.size());
                                }
                            }
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);
                        }else{
                            Log.d(TAG, "onSuccess: noResultFound");
                            result_search.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

}