package com.example.ajinafro.authentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ajinafro.MainActivity;
import com.example.ajinafro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG ="SignInActivity : " ;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        ref = getSharedPreferences("ajinsafro", MODE_PRIVATE);
    }

    private SharedPreferences ref;
    @OnClick(R.id.register_now)
    void redirectToSignUpActivity(){
        Intent signup=new Intent(this,SignUpActivity.class);
        startActivity(signup);
    }
    private Snackbar snackbar;
    private FirebaseAuth mAuth;
    @BindView(R.id.signin_main_layout)
    RelativeLayout main_Layout;

    @BindView(R.id.signin_email)
    EditText emailField;

    @BindView(R.id.signin_password)
    EditText passwordField;

    @OnClick(R.id.signin_connexion_btn)
    void SignIn(){
        Log.d(TAG,"START LOGIN : ");
        String pass=passwordField.getText().toString().trim();
        String email =emailField.getText().toString().trim();
        if(pass.equals("") || email.equals("")){
            snackbar=Snackbar.make(main_Layout,"Veuillez saisir un email et password valide",Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            if (pass.length()>8){
                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    ref.edit().putBoolean("isLogged",true).apply();
                                    ref.edit().putString("UserUid",user.getUid()).apply();
                                    Intent nextpage=new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(nextpage);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    snackbar=Snackbar.make(main_Layout,"Connexion échouée.",Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        });
            }else{
                snackbar=Snackbar.make(main_Layout,"Ce mot de passe est trés court \n entrez des informations valides",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}

