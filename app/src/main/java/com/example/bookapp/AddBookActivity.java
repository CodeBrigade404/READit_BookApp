package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityAddBookBinding;
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

public class AddBookActivity extends AppCompatActivity {

    //view binding
    private ActivityAddBookBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
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
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDate();

            }
        });

        //handle click,start category add screen
        binding.ViewBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBookActivity.this, ViewAllBooksActivity.class));
            }
        });
    }

    private String book= "",category = "" ,des = "";
    private void validateDate() {
        /*before creating accounts*/

        //get data
        book = binding.titleEt.getText().toString().trim();
        category = binding.categoryTv.getText().toString().trim();
        des = binding.descriptionEt.getText().toString().trim();


        //validate data
        if (TextUtils.isEmpty(book)){
            Toast.makeText(this,"Enter Book name....",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(category)){
            Toast.makeText(this,"Enter Category name....",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(des)){
            Toast.makeText(this,"Enter Description name....",Toast.LENGTH_SHORT).show();
        }
        else{
            createComment();
        }
    }

    private void createComment() {
        //show progress
        progressDialog.setMessage("Adding Book...");
        progressDialog.show();

        //get timestamp
        long timestamp = System.currentTimeMillis();

        //set up info to add firebase db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", ""+timestamp);
        hashMap.put("book", ""+book);
        hashMap.put("category", ""+category);
        hashMap.put("description", ""+des);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", ""+firebaseAuth.getUid());

        //add firebase db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(firebaseAuth.getUid()).child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //category add
                        progressDialog.dismiss();
                        Toast.makeText(AddBookActivity.this, "Your Book successfully....", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //category failed
                        progressDialog.dismiss();
                        Toast.makeText(AddBookActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}