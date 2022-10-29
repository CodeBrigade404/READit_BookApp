package com.example.bookapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.example.bookapp.databinding.ActivityPdfEditBinding;
import com.example.bookapp.databinding.ActivityProfileBinding;
import com.example.bookapp.databinding.ActivityProfileEditBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileEditActivity extends AppCompatActivity {

    private ActivityProfileEditBinding binding;

    private FirebaseAuth firebaseAuth;

    private static final String TAG = "PROFILE_EDIT_TAG";

    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        firebaseAuth  = FirebaseAuth.getInstance();
        loadUserInfo();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.profileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageAttachMenu();
            }
        });

    }

    private void showImageAttachMenu() {
        PopupMenu popupMenu = new PopupMenu(this
        ,binding.profileTv);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Camara");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Gallary");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int which = menuItem.getItemId();
                if (which == 0){

                }else  if(which == 1){
                    pickImageGallary();
                }
                return false;
            }


        });
    }

    private void pickImageGallary() {
        ContentValues values =new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New pick");
        values.put(MediaStore.Images.Media.DESCRIPTION,"sample image description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        camaraActivityResultLauncher.launch(intent);

    }




   private ActivityResultLauncher<Intent> camaraActivityResultLauncher = registerForActivityResult(
           new ActivityResultContracts.StartActivityForResult(),
           new ActivityResultCallback<ActivityResult>() {
               @Override
               public void onActivityResult(ActivityResult result) {
                     if (result.getResultCode() == Activity.RESULT_OK){
                         Log.d(TAG, "onActivityResult: Picked from Camara");
                         Intent data  = result.getData();
                         binding.profileTv.setImageURI(imageUri);
                     }
               }
           }
   );

    private ActivityResultLauncher<Intent> gallaryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data  = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: Picked from Gallary");
                    }
                }
            }
    );


    private void loadUserInfo() {
        Log.d(TAG, "loadUserInfo: Loarding user info...."+firebaseAuth.getUid());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String email = ""+snapshot.child("email").getValue();
                        String name = ""+snapshot.child("name").getValue();
                        String profileImage = ""+snapshot.child("profileImage").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String uid = ""+snapshot.child("uid").getValue();
                        String userType = ""+snapshot.child("userType").getValue();


                        binding.nameEt.setText("email");


                        Glide.with(ProfileEditActivity.this).load(profileImage)
                                .placeholder(R.drawable.ic_person_gray)
                                .into(binding.profileTv);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}