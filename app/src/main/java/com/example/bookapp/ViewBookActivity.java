package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.bookapp.adapters.AdapterBook;
import com.example.bookapp.databinding.ActivityViewBookBinding;
import com.example.bookapp.models.ModelBook;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBookActivity extends AppCompatActivity {

    private ActivityViewBookBinding binding;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelBook> categoryArrayList;
    private AdapterBook adapaterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewBookBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        loadCategories();

        binding.ctr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewBookActivity.this,ViewBookActivity.class));

            }
        });


    }

    private void loadCategories() {

        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categoryArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){

                            ModelBook model = ds.getValue(ModelBook.class);
                            categoryArrayList.add(model);

                        }
                        adapaterCategory = new AdapterBook(ViewBookActivity.this,categoryArrayList);
                        binding.ctagory.setAdapter(adapaterCategory);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}