package com.example.ajinafro.carpool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ajinafro.R;
import com.example.ajinafro.adapters.CarpoolsAdapter;
import com.example.ajinafro.models.Carpool;
import com.example.ajinafro.utils.CityPickerActivity;
import com.example.ajinafro.utils.VerticalSpacingItemDecorator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchCarpoolActivity extends AppCompatActivity {
    private static final int CITY_PICKER =888 ;
    private static final int CITY_PICKER2 =881 ;
    private FirebaseFirestore db;
    private ArrayList<Carpool> carpoolArrayList=new ArrayList<>();
    private static final String TAG = "SearchCarpoolActivity";
    private static final Date now= new Date();
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    SearchCarpoolActivity self;
    private String lastUserPhone_selected=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_carpool);
        ButterKnife.bind(this);
        db=FirebaseFirestore.getInstance();
        result_search.setVisibility(View.INVISIBLE);
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);
        hide_bottomsheet();
        initRecycleview();
        self = this;

    }
@BindView(R.id.bottomSheet)
ConstraintLayout bottomSheet;
    @BindView(R.id.carpool_offers_recycleview)
    RecyclerView carpools_recycleview;

    @OnClick(R.id.newcarpool_donebtn)
    void alert(){
        Snackbar snackbar=Snackbar.make(result_search,"Prochainement...", Snackbar.LENGTH_LONG);snackbar.show();
    }

    @BindView(R.id.searchcarpool_result_layout)
    ConstraintLayout result_search;

    @OnClick(R.id.call_btn)
    void call(){
        call_someone(lastUserPhone_selected);
    }

    @BindView(R.id.searchcarpool_startcity)
    EditText startcity_field;

    @BindView(R.id.recherche_btn)
    Button recherche;

    @BindView(R.id.description)
    TextView descripton_area;
    @BindView(R.id.carpool_detail_username)
    TextView username_area;
    @BindView(R.id.carpool_detail_avatar_picture)
    CircleImageView circleImageView;

    public void hide_bottomsheet(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
    public void show_bottomsheet(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    public void change_bottomsheet_state(String description, String username, Uri pdp,String phone){
        descripton_area.setText(""+description);
        username_area.setText(username);
        Glide.with(this).load(pdp).into(circleImageView);
        lastUserPhone_selected=phone;
        show_bottomsheet();
    }

void call_someone(String number){
        if(number!=null && !number.isEmpty()){
            Intent calling = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            startActivity(calling);
        }else{
            Snackbar snackbar=Snackbar.make(bottomSheet,"Cet utilisateur n'a pas encore enregistré un numero de telephone",Snackbar.LENGTH_SHORT);
                snackbar.show();
        }

}

    @OnClick(R.id.recherche_btn)
    void result(){
        hide_bottomsheet();
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
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList,self);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);

                        }else{
                            carpoolArrayList=new ArrayList<>();
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList,self);
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
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList,self);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);

                        }else{
                            result_search.setVisibility(View.VISIBLE);
                            carpoolArrayList=new ArrayList<>();
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList,self);
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
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList,self);
                            carpools_recycleview.setAdapter(carpoolsAdapter);
                            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
                            carpools_recycleview.addItemDecoration(itemDecorator);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            carpools_recycleview.setLayoutManager(layoutManager);

                        }else{
                            result_search.setVisibility(View.VISIBLE);
                            carpoolArrayList=new ArrayList<>();
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList,self);
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
                            CarpoolsAdapter carpoolsAdapter=new CarpoolsAdapter(getApplicationContext(),carpoolArrayList,self);
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