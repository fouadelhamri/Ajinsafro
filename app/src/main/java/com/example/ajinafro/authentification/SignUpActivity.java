package com.example.ajinafro.authentification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ajinafro.MainActivity;
import com.example.ajinafro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.sql.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        ref=getSharedPreferences("ajinsafro", MODE_PRIVATE);
    }
    private SharedPreferences ref;
    private FirebaseFirestore db;
    private static String TAG ="SIGNUPACTIVITY : ";
    private FirebaseAuth mAuth;
    @BindView(R.id.signup_username_field)
    EditText usernamefield;
    @BindView(R.id.signup_mail_field)
    EditText emailfield;
    @BindView(R.id.signup_password_field)
    EditText passwordfield;
    @BindView(R.id.signup_password_confirmation_field)
    EditText password_confirmationfield;
    @OnClick(R.id.button)
    void backToSignin(){
        Intent nextpage=new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(nextpage);
        finish();
    }
@BindView(R.id.signin_main_layout)
    RelativeLayout main_layout;
    Snackbar snackbar;
    
    @OnClick(R.id.signup_register_btn)
    void register(){
        String username=usernamefield.getText().toString().trim().toLowerCase();
        String email=emailfield.getText().toString().trim();
        String password=passwordfield.getText().toString().trim();
        String password_confirmation=password_confirmationfield.getText().toString().trim();
        if(password.equals("") || password_confirmation.equals("") || username.equals("") || email.equals("") || !password.equals(password_confirmation)){
            snackbar= Snackbar.make(main_layout,"tout les champs sont obligatoire",Snackbar.LENGTH_LONG);
            snackbar.show();
            Log.d(TAG,"tout les champs sont obligatoire");
        }else{
           db.collection("users").whereEqualTo("username",username)
                   .get()
                   .addOnCompleteListener(task -> {
                           if (task.isSuccessful()){
                               List<DocumentSnapshot> doc = task.getResult().getDocuments();
                               boolean username_already_exist=false;
                               if (doc.size()!=0)username_already_exist=true;
                               if(username_already_exist){
                                   snackbar= Snackbar.make(main_layout,  "Ce pseudo est déja utilisé \n veuillez choisir un autre",Snackbar.LENGTH_LONG);
                                   snackbar.show();
                               }else {
                                   createNewUser(email,password,username,db);
                               }
                           }
                       }
           );
        }
    }

    void createNewUser(String email,String password,String username,FirebaseFirestore db){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserInfo(user.getUid(),username,email,db);
                        } else {
                            // If sign in fails, display a message to the user.
                            snackbar= Snackbar.make(main_layout,  task.getException().getMessage()+" ",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });

    }
    void saveUserInfo(String uid,String username,String email ,FirebaseFirestore db){
        Map<String, Object> user = new HashMap<>();
        user.put("username",username );
        user.put("email",email );
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> saveUserAccountDetails(uid,username,email,db))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

    }
    void saveUserAccountDetails(String uid,String username,String email,FirebaseFirestore db){
        Log.d(TAG, "DocumentSnapshot successfully written!");
        Map<String, Object> userdetails = new HashMap<>();
        userdetails.put("username",username );
        userdetails.put("email",email );
        userdetails.put("bio","Salut ,j'utilise Ajinsafro ");
        userdetails.put("fullname",username );
        userdetails.put("phone","none");
        userdetails.put("city","none");
        userdetails.put("profile_image","https://firebasestorage.googleapis.com/v0/b/ajinsafro-db.appspot.com/o/avatar.jpg?alt=media&token=610b96ee-5252-48ee-a36e-5df7a4dccfca");
        userdetails.put("posts", Arrays.asList());
        userdetails.put("saved_posts",Arrays.asList());
        db.collection("users_details").document(uid)
                .set(userdetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        //ref.edit().putBoolean("isLogged",true).apply();
                        ref.edit().putString("userUid",uid).apply();
                        Intent nextpage=new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(nextpage);
                        finish();
                    }
                });
    }
    }
