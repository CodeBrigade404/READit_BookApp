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
import com.example.bookapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class AddCommentActivity extends AppCompatActivity {

    //view binding
    private ActivityAddCommentBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCommentBinding.inflate(getLayoutInflater());
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
        binding.addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDate();

            }
        });
    }

    private String book = "", comment = "";
    private void validateDate() {
        /*before creating accounts*/

        //get data
        book = binding.bookEt.getText().toString().trim();
        comment = binding.commentEt.getText().toString().trim();



        //validate data
        if (TextUtils.isEmpty(book)){
            Toast.makeText(this,"Enter Book name....",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(comment)){
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
        hashMap.put("book", ""+book);
        hashMap.put("comment", ""+comment);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", ""+firebaseAuth.getUid());

        //add firebase db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("comment");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //category add
                        progressDialog.dismiss();
                        Toast.makeText(AddCommentActivity.this, "Your comment successfully....", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //category failed
                        progressDialog.dismiss();
                        Toast.makeText(AddCommentActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}