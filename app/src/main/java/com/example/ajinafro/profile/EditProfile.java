package com.example.ajinafro.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajinafro.R;

public class EditProfile extends AppCompatActivity {
    private ImageButton backBtn,btn_edit;
    private ImageView profile_img;
    private EditText Edit_UserName,Edit_Bio,Edit_phone,Edit_city;
    //private Button btn_edit;
    private TextView edit_profile_txt,edit_mdp;

    //permission constants
    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=300;
    //img pick constant
    private static final int IMAGE_PICK_GALLERY_CODE =400;
    private static final int IMAGE_PICK_CAMERA_CODE =500;

    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermissions;
    //progress dialog
    private ProgressDialog progressDialog;
    //image uri
    private Uri img_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        //init permission arrays

        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //setupp progress dialog

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Attendez s'il vous plait ");
        progressDialog.setCanceledOnTouchOutside(false);
        backBtn=findViewById(R.id.back_btn);
        profile_img=findViewById(R.id.profile_img);
        Edit_UserName=findViewById(R.id.Edit_UserName);
        Edit_Bio=findViewById(R.id.Edit_Bio);
        Edit_phone=findViewById(R.id.Edit_phone);
        Edit_city=findViewById(R.id.Edit_city);
        btn_edit=findViewById(R.id.Edit_profile_btn_edit);
        edit_profile_txt=findViewById(R.id.edit_profile_txt);
        edit_mdp=findViewById(R.id.edit_mdp);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on change dans firebase les anciens donnees
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edit_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changer mot de pass
            }
        });
        edit_profile_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick img
                showImagePickDialog();
            }
        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick img
                showImagePickDialog();

            }
        });}
    private void showImagePickDialog() {
        //option to display in dialog
        String[] options ={"Camera","Galerie"};
        //dialog
        Context context;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("choisissez une image :")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            //camera clicked
                            if (checkCameraPermission()){
                                pickFromCamera();
                            }
                            else {
                                requestCameraPermission();
                            }

                        }
                        else {
                            //gallery clicked
                            if (checkStoragePermission()){
                                //allowed ,open gallery
                                pickFromGallery();
                            }
                            else {
                                //not allowed
                                requestStoragePermission();
                            }

                        }
                    }
                })
                .show();


    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermissions,STORAGE_REQUEST_CODE);
    }
    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera() {
        //intent to pick image from Camera
        ContentValues contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"titre de l'image");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Description de l'image");
        img_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,img_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);

    }
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted =grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this,"la permission de la camera est necessaire..",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this,"la permission dU stockage est necessaire..",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //handle image pick result
        if (resultCode == RESULT_OK){
            if (requestCode==IMAGE_PICK_CAMERA_CODE)
            {
                //PICKED FROM GALLERY
                img_uri=data.getData();
                //set to imageview
                profile_img.setImageURI(img_uri);
            }
            else if (requestCode ==IMAGE_PICK_GALLERY_CODE)
            {
                profile_img.setImageURI(img_uri);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}