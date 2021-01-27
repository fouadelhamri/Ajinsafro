package com.example.ajinafro.newPost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajinafro.R;
import com.example.ajinafro.models.Adresse;
import com.example.ajinafro.models.Posts;
import com.example.ajinafro.models.UserAccountDetails;
import com.example.ajinafro.utils.LatLon;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

@SuppressWarnings({"unused", "Convert2Lambda"})
public class AddPostDetailsActivity extends AppCompatActivity {
    private static final String TAG = "AddPostDetailsActivity";
    //firebase
    private FirebaseFirestore db;
    private SharedPreferences ref;
    private StorageReference storagePostsImagesReference;
    private StorageTask uploadtask;
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
    //ui var
    Snackbar snackbar;
    ImageButton back_btn;
    ImageView image_post,add_btn;
    TextView add_post_btn;
    EditText nom_post,localisation_post,description_post;
    String categorie_name;
    Adresse post_adresse;
    public LatLng post_location=null;
    private String user_UID;
    private ArrayList<String> post_photos_string=new ArrayList<>();
    private ArrayList<Uri> post_photos=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_details);
        ButterKnife.bind(this);
        ref=getSharedPreferences("ajinsafro",MODE_PRIVATE);
        user_UID=ref.getString("userUid","none");
        //init database firestore
        db=FirebaseFirestore.getInstance();
        storagePostsImagesReference= FirebaseStorage.getInstance().getReference().child("posts");
        //binding
        image_post=(ImageView)findViewById(R.id.image_post);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        add_post_btn=(TextView)findViewById(R.id.add_post_btn);
        nom_post=(EditText)findViewById(R.id.nom_post);
        localisation_post=(EditText)findViewById(R.id.localisation_post);
        description_post=(EditText)findViewById(R.id.description_post);
       /* addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });*/
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //init permission arrays
        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        try{
            categorie_name=getIntent().getExtras().getString("categorie_name");
            Log.d(TAG, "onCreate: categorie received "+categorie_name);
        }
        catch (Exception ex){
            Log.d(TAG, "onCreate: "+ex.getMessage());
        }
        add_post_btn.setVisibility(View.INVISIBLE);
        postdetails_layout.setVisibility(View.INVISIBLE);
        post_photos.clear();
        post_photos_string.clear();
        showImagePickDialog();
    }
    @OnClick(R.id.add_post_btn)
    void add_post(){
        if(nom_post.getText().toString().isEmpty()){
           snackbar=Snackbar.make(postdetails_layout,"Le champs du nom est obligatoire",Snackbar.LENGTH_LONG);
        snackbar.show();
        }else {
            if (localisation_post.getText().toString().isEmpty()) {
                snackbar = Snackbar.make(postdetails_layout, "Veuillez entrer une localisation pour aider les autres voyageurs !", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                post_adresse=new Adresse(post_location.latitude,post_location.longitude,
                getCity(post_location.latitude,post_location.longitude));
                Log.d(TAG, "add_post: "+ post_adresse.toString());
                snackbar = Snackbar.make(postdetails_layout, "Chargement... ", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                uploadPost(post_photos);
            }
        }
    }
    @BindView(R.id.addpost_postdetails)
    ConstraintLayout postdetails_layout;

    void uploadPost(ArrayList<Uri> post_photos){
        for(Uri uri : post_photos){
            uploadPhoto(uri);
        }
    }
    void uploadPhoto(Uri uri){
        if(img_uri!=null){
            final StorageReference fileRef=storagePostsImagesReference
                    .child(String.valueOf((new Timestamp(new Date())).getSeconds())+"_"+user_UID+".jpg");
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
                post_photos_string.add(uri.toString());
                snackbar.setText("Chargement "+post_photos_string.size()+"/"+post_photos.size()+ "");
                if(post_photos_string.size()==post_photos.size()){
                    //derniere photo a étè uploader!
                    String post_desc=description_post.getText().toString();
                    String post_name=nom_post.getText().toString();
                    Posts post_toShare=new Posts(post_name,post_adresse,null,new Timestamp(new Date()),post_desc,null,post_photos_string,user_UID);
                    Log.d(TAG, "added post: "+post_toShare.toString());
                    addPostToDb(post_toShare);
                }
            }
        });
    }
    private void addPostToDb(Posts post_toShare){
    db.collection("posts").add(post_toShare).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            snackbar.setText("terminé");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        snackbar.dismiss();
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    });
}
    private void showImagePickDialog() {
        //option to display in dialog
        String[] options ={"Camera","Galerie"};
        //dialog
        post_photos_string.clear();post_photos.clear();
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
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
    public String getCity(double lat,double lng){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add =obj.getLocality();
            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return " ";
        }
    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);//adresse
            //add = add + "\n name : " + obj.getFeatureName();
            // add = add + "\n" + obj.getCountryName();
            //add = add + "\n" + obj.getCountryCode();
            //add = add + "\n" + obj.getAdminArea();//region
//            add = add + "\n" + obj.getPostalCode();
            //          add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();//ville
            //Log.v("IGA", "Address" + add);
            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return " ";
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4){
            if(resultCode==Activity.RESULT_OK){
                LatLon local= (LatLon) data.getExtras().getSerializable("restaurant_location");
                post_location=new LatLng(local.getLat(),local.getLon());
                TextView location_text=findViewById(R.id.localisation_post);
                location_text.setText(" "+getAddress(local.getLat(),local.getLon()));
            }
        }
        //handle image pick result
        if(resultCode == RESULT_OK ){
            if (requestCode==IMAGE_PICK_GALLERY_CODE)
            {
                postdetails_layout.setVisibility(View.VISIBLE);
                add_post_btn.setVisibility(View.VISIBLE);
                List<Bitmap>  bitmaps= new ArrayList<>();
                ClipData clipData=data.getClipData();
                if (clipData != null){
                    for (int i=0;i<clipData.getItemCount();i++){
                        img_uri=clipData.getItemAt(i).getUri();
                        post_photos.add(img_uri);
                        try {
                            InputStream is=getContentResolver().openInputStream(img_uri);
                            Bitmap bitmap=BitmapFactory.decodeStream(is);
                            bitmaps.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    postdetails_layout.setVisibility(View.VISIBLE);
                    add_btn.setVisibility(View.VISIBLE);
                    img_uri=data.getData();
                    try {
                        InputStream is =getContentResolver().openInputStream(img_uri);
                        Bitmap bitmap=BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {for (int i=0;;i++){
                        for (Bitmap b:bitmaps){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image_post.setImageBitmap(b);
                                }
                            });
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    }
                }).start();
            }
            else if (requestCode ==IMAGE_PICK_CAMERA_CODE)
            {
                img_uri=data.getData();
                image_post.setImageURI(img_uri);
                post_photos.add(img_uri);
            }
        }
    }

    @OnClick(R.id.localisation_post)
    void locationPicker(){
        Log.d(TAG, "locationPicker2: CLICK");
        Intent locationPicker=new Intent(this,MapsPickerActivity.class);
        if(post_location!=null){
            locationPicker.putExtra("location",new LatLon(post_location.latitude,post_location.longitude));
        }
        startActivityForResult(locationPicker,4);
    }

}