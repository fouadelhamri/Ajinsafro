package com.example.ajinafro.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajinafro.R;
import com.example.ajinafro.models.Carpool;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCarpoolOtherDetailsActivity extends AppCompatActivity {
    private static final String TAG ="CarpoolOtherDetails" ;
    private static final int CARPOOL_SHARED=880;

    private String userId;
    ConstraintLayout done_btn;
    Carpool carpool;
    SharedPreferences ref;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_carpool_other_details);
        done_btn=findViewById(R.id.newcarpool_donebtn);
        ButterKnife.bind(this);
        ////////////
        db=FirebaseFirestore.getInstance();
        ref=getSharedPreferences("ajinsafro",MODE_PRIVATE);
        userId=ref.getString("userUid","default");
        if (userId.equals("default"))finish();

    }
    @BindView(R.id.newcarpool_description)
    EditText newcarpool_description;
    @BindView(R.id.newcarpool_backbtn2)
    TextView back;

    @OnClick(R.id.newcarpool_backbtn2)
    void back(){
        onBackPressed();
    }

    @BindView(R.id.newcarpool_addplacebtn)
    ImageButton add_place;
    @BindView(R.id.newcarpool_place_number)
    EditText places_nb_field;
    @BindView(R.id.editTextTextPersonName4)
    EditText prix;

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
        Intent intent=getIntent();
        if (prix.getText().toString().isEmpty()){
            Snackbar snackbar=Snackbar.make(newcarpool_description,"Vous êtes sure que cette est offre est gratuit ? mettez un prix à votre covoiturage",BaseTransientBottomBar.LENGTH_LONG);snackbar.show();
        }else{
            if(intent.getExtras()!=null){

                String villedepart= (String) intent.getSerializableExtra("startcity");
                String villearrive= (String) intent.getSerializableExtra("endcity");
                long datecarpool=(long) intent.getSerializableExtra("datecarpool");//khdm biha haka hyt makatchdch b une autre maniere ana 3arfak m3ks atjrb ta ghatjrb o atrj3 lhadi :binomatoka ljamila said
                Integer timecarpoolHour= (int) intent.getSerializableExtra("timecarpoolHour");
                Integer timecarpoolMinute=(int) intent.getSerializableExtra("timecarpoolMinute");
                Date salam=new Date(datecarpool);
                Timestamp  time=new Timestamp(salam);

                String publisher=userId;
                String str= places_nb_field.getText().toString();
                Integer available_places=Integer.parseInt(str);
                String Monprix= prix.getText().toString();
                Integer price=Integer.parseInt(Monprix);
                String Details= newcarpool_description.getText().toString();

                carpool=new Carpool(villedepart,villearrive,publisher,time,timecarpoolHour,timecarpoolMinute,available_places,price,Details);
                Snackbar snackbar=Snackbar.make(done_btn,"Attendez...", Snackbar.LENGTH_SHORT);
                shareCarpool(carpool);
                snackbar.show();
                Log.d(TAG, "share_carpool: lcarpool is  :"+time+"prix"+price);

            }
        }

    }
    void shareCarpool(Carpool carpool){
        db.collection("carpool").add(carpool).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Snackbar snackbar=Snackbar.make(done_btn,"Offre partagée...",Snackbar.LENGTH_LONG);
                snackbar.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            snackbar.dismiss();
                            setResult(CARPOOL_SHARED);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackbar=Snackbar.make(done_btn,"Une erreur est survenue lors du partage...",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

}