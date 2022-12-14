package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityAddCommentBinding;
import com.example.bookapp.databinding.ActivityProfileEditBinding;
import com.example.bookapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class ProfileEditActivity extends AppCompatActivity {

    //view binding
    private ActivityProfileEditBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //set up progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //handle click, begin register
        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDate();

            }
        });
    }

    private String name = "", userType = "", address = "", email= "";
    private void validateDate() {



        name = binding.nameEt.getText().toString().trim();
        userType = binding.userType.getText().toString().trim();
        address = binding.address.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"Enter Book name....",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userType)){
            Toast.makeText(this,"Enter Your Comment....!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"Enter Your Comment....!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Your Comment....!",Toast.LENGTH_SHORT).show();
        }
        else{
            createComment();
        }
    }

    private void createComment() {
        //show progress
        progressDialog.setMessage("Adding Comment...");
        progressDialog.show();

        //get timestamp
        long timestamp = System.currentTimeMillis();

        //set up info to add firebase db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", ""+timestamp);
        hashMap.put("name", ""+name);
        hashMap.put("UserType", ""+userType);
        hashMap.put("address", ""+address);
        hashMap.put("email", ""+email);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", ""+firebaseAuth.getUid());

        //add firebase db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Edited Users");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //category add
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditActivity.this, " User Updated successfully....", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //category failed
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}