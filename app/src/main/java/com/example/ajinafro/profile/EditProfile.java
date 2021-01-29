package com.example.ajinafro.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.example.ajinafro.R;
import com.example.ajinafro.models.City;
import com.example.ajinafro.models.UserAccountDetails;
import com.example.ajinafro.utils.CityPickerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile<layout_city> extends AppCompatActivity {
    private static final String TAG = "EditProfile";
    private static final int CITY_PICKER =888 ;
    private static final int CITY_CHANGED =999 ;
    //user details from Shared;
    private UserAccountDetails userAccountDetails;
    private String userUID;
    private SharedPreferences ref;
    private boolean userInfochanged=false;
    //firebase
    private FirebaseFirestore db;
    private StorageReference storageUsersImagesReference;
    private StorageTask uploadtask;
    ////Ui var
    private ImageButton backBtn,btn_edit;
    private CircleImageView profile_img;
    private EditText Edit_UserName,Edit_Bio,Edit_phone,Edit_city;
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
    private ProgressDialog progressDialog,progressLoadImageDialog;
    //image uri
    private Uri img_uri;
    private String myuri;
    @OnClick(R.id.layout_city)
    void cityPicker(){
        Intent intent=new Intent(this, CityPickerActivity.class);
        if(!Edit_city.getText().toString().isEmpty())
        intent.putExtra("initialCity",Edit_city.getText().toString());
        startActivityForResult(intent,CITY_PICKER);
    }

    @OnFocusChange(R.id.Edit_city)
    void cityPicker1(){
        Intent intent=new Intent(this, CityPickerActivity.class);
        startActivityForResult(intent,CITY_PICKER);
    }

    @OnClick(R.id.imageView4)
    void cityPicker2(){
        Intent intent=new Intent(this, CityPickerActivity.class);
        startActivityForResult(intent,CITY_PICKER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        db= FirebaseFirestore.getInstance();
        storageUsersImagesReference= FirebaseStorage.getInstance().getReference().child("users_images");
        getUserDetailsFromSharedPreferences();
        //init permission arrays
        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ///UI binding
        uiBinding();
    }

    private  void getUserDetailsFromSharedPreferences(){
        ref=getSharedPreferences("ajinsafro",MODE_PRIVATE);
        userUID=ref.getString("UserUid","none");
        if(userUID.equals("none")){
            userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        String userjson = ref.getString("userAccountDetails","empty");
        Gson gson=new Gson();
        userAccountDetails=gson.fromJson(userjson,UserAccountDetails.class);
        Log.d(TAG, "getUserDetailsFromSharedPreferences: "+        userAccountDetails.toString());
    }
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
        super.onActivityResult(requestCode, resultCode, data);
        //handle image pick result
        if(requestCode==CITY_PICKER){
            Log.d(TAG, "onActivityResult: city picker");
            if(resultCode == Activity.RESULT_OK){
                Edit_city.setText(data.getExtras().getString("selectedCity") );
            }else {
            }
        }
        else if(resultCode == RESULT_OK ){
            if (requestCode==IMAGE_PICK_CAMERA_CODE)
            {
                img_uri=data.getData();
                profile_img.setImageURI(img_uri);
                uploadProfileImageToCloudStorage(img_uri);
            }
            else if (requestCode ==IMAGE_PICK_GALLERY_CODE)
            {
                //PICKED FROM GALLERY
                img_uri=data.getData();
                profile_img.setImageURI(img_uri);
                uploadProfileImageToCloudStorage(img_uri);
            }
        }
    }
    @OnTextChanged(R.id.Edit_Bio)
    void userInfoChanged1(){
        userInfoChanged();
    }
    @OnTextChanged(R.id.Edit_phone)
    void userInfoChanged2(){
        userInfoChanged();
    }
    @OnTextChanged(R.id.Edit_city)
    void userInfoChanged3(){
        userInfoChanged();
    }
    @OnTextChanged(R.id.citypicker_cityname)
    void userInfoChanged4(){
        userInfoChanged();
    }
    @BindView(R.id.edit_profile_main_layout)
    RelativeLayout main_layout;
    @BindView(R.id.Edit_profile_btn_edit)
    ImageButton  editBtn;
    void uiBinding(){
        backBtn=findViewById(R.id.back_btn);
        profile_img=findViewById(R.id.profile_img);
        Edit_UserName=findViewById(R.id.citypicker_cityname);
        Edit_Bio=findViewById(R.id.Edit_Bio);
        Edit_phone=findViewById(R.id.Edit_phone);
        Edit_city=findViewById(R.id.Edit_city);
        btn_edit=findViewById(R.id.Edit_profile_btn_edit);
        edit_profile_txt=findViewById(R.id.edit_profile_txt);
        edit_mdp=findViewById(R.id.edit_mdp);
        //Field hint
        Edit_UserName.getText().append(userAccountDetails.getFullname());
        Edit_Bio.getText().append(userAccountDetails.getBio());
        Edit_phone.getText().append(userAccountDetails.getPhone());
        Edit_city.getText().append(userAccountDetails.getCity());
        btn_edit.setVisibility(View.INVISIBLE);
        Glide.with(this)
                .load(Uri.parse(userAccountDetails.getProfile_image()))
                .centerCrop()
                .placeholder(R.drawable.default_profile)
                .into(profile_img);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtnClicked();
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
                Snackbar snackbar=Snackbar.make(v,"Cette option n'est pas encore développer",Snackbar.LENGTH_SHORT);
                snackbar.show();
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
        });
    }
    void setupProgressDialog(String title){
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
    void userInfoChanged(){
        userInfochanged=true;
        editBtn.setVisibility(View.VISIBLE);
    }
    void editBtnClicked(){
        if (Edit_Bio.equals(userAccountDetails.getBio())
                && Edit_city.equals(userAccountDetails.getCity())
                && Edit_phone.equals(userAccountDetails.getPhone())
                && Edit_UserName.equals(userAccountDetails.getUsername())
        && !userInfochanged)
        {
            finish();
        }
        else{
            userAccountDetails.setBio(Edit_Bio.getText().toString());
            userAccountDetails.setCity(Edit_city.getText().toString());
            userAccountDetails.setPhone(Edit_phone.getText().toString());
            userAccountDetails.setFullname(Edit_UserName.getText().toString());
            Gson gson = new Gson();
            String userJson = gson.toJson(userAccountDetails);
            ref.edit().remove("userAccountDetails");
            ref.edit().putString("userAccountDetails", userJson).apply();
            updateUserDetailsDB(userAccountDetails);
        }
    }
    void updateUserDetailsDB(UserAccountDetails userAccountDetails){
        setupProgressDialog("chargement...");
        db.collection("users_details")
                    .document(userUID)
                    .set(userAccountDetails, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>(){
                        @Override
                        public void onSuccess(Void aVoid) {
                           progressDialog.dismiss();
                           finish();
                        }
                  }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "updateUserDetailsDB =>onFailure: "+e.getMessage());
                            progressDialog.dismiss();
                            finish();
                        }
                });

    }
    void uploadProfileImageToCloudStorage(Uri img_uri){
        progressLoadImageDialog=new ProgressDialog(this);
        progressLoadImageDialog.setTitle("Chargement d'image... ");
        progressLoadImageDialog.setCanceledOnTouchOutside(false);
        progressLoadImageDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressLoadImageDialog.show();
        if(img_uri!=null){
            final StorageReference fileRef=storageUsersImagesReference
                    .child(userUID)
                    .child("pdp_"+userAccountDetails.getUsername()+".jpg");
            uploadtask=fileRef.putFile(img_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getDownloadUrl(fileRef);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressLoadImageDialog.dismiss();
                    Log.e(TAG, "onFailure: uploadProfileImageToCloudStorage =>",e.getCause() );
                }
            });

                }
        else{
            Log.d(TAG, "uploadProfileImageToCloudStorage: "+"pas d'image d entree");
        }
    }
    void getDownloadUrl(StorageReference ref){
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess: Url = "+uri.toString());
                setUserProfilImage(uri);
            }
        });
    }
    void setUserProfilImage(Uri uri){
        myuri=uri.toString();
        this.userAccountDetails.setProfile_image(myuri);
        db.collection("users_details").document(userUID)
                .set(this.userAccountDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Gson json =new Gson();
                    String userjson = json.toJson(userAccountDetails);
                    ref.edit().remove("userAccountDetails");
                    ref.edit().putString("userAccountDetails",userjson).apply();
                    Log.d(TAG, "onComplete: USER Profile update!");
                    progressLoadImageDialog.dismiss();
                }
            }
        });
    }
}