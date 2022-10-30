package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityCategoryEditBinding;
import com.example.bookapp.models.ModelCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class CategoryEditActivity extends AppCompatActivity {
    private ActivityCategoryEditBinding activityCategoryEditBinding;
    private FirebaseAuth firebaseAuth;
    private String id;
    private ArrayList<ModelCategory> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityCategoryEditBinding = ActivityCategoryEditBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityCategoryEditBinding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        id= getIntent().getStringExtra("id");

        loadCategories();

        activityCategoryEditBinding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });


    }




    private void loadCategories() {
        ModelCategory model = new ModelCategory();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("categories");
        ref.child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String cName = ""+snapshot.child("category").getValue();
                        activityCategoryEditBinding.categoryEdit.setText(cName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }



                });


    }
    private String category="";

    private void validateData() {

        category=activityCategoryEditBinding.categoryEdit.getText().toString().trim();

        if(TextUtils.isEmpty(category)){
            Toast.makeText(this,"Enter Category",Toast.LENGTH_SHORT).show();
        }else{
            updataData();
        }
}

    private void updataData() {
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("category",category);
        hashMap.put("id",id);
        // hashMap.put("uid",uid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("categories");
        ref.child(id)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        onBackPressed();



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onBackPressed();


                    }
                });



    }
    }