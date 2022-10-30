package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.bookapp.adapters.AdapterBook;
import com.example.bookapp.adapters.AdapterCategory;
import com.example.bookapp.databinding.ActivityDashboardAdminBinding;
import com.example.bookapp.databinding.ActivityViewAllBooksBinding;
import com.example.bookapp.models.ModelBook;
import com.example.bookapp.models.ModelCategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllBooksActivity extends AppCompatActivity {

    //view binding
    private ActivityViewAllBooksBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //Arraylist to store category
    private ArrayList<ModelBook> categoryArrayList;

    //adapter
    private AdapterBook adapterCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAllBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadCategories();

        //edit text change listener search
//        binding.searchEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                //called as when user type each other
////                try {
////                    adapterCategory.getFilter().filter(s);
////                }
////                catch (Exception e){
////
////                }
////
////            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        //handle click, logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });




        //handle  click, open profile
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewAllBooksActivity.this,ProfileActivity.class));
            }
        });

    }

    private void loadCategories() {
        //init arraylist
        categoryArrayList = new ArrayList<>();
        //get all categories from firebase categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear arraylist before adding date into it
                categoryArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    ModelBook model = ds.getValue(ModelBook.class);

                    //add to arraylist
                    categoryArrayList.add(model);
                }
                //setup adapter
                adapterCategory = new AdapterBook(ViewAllBooksActivity.this
                        ,categoryArrayList);

                //set adapter to recyclerview
                binding.categoriesRv.setAdapter(adapterCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            //not logged in , go to main screen
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else{
            //logged in , get user info
            String email = firebaseUser.getEmail();
            //set in textview of toolbar

        }
    }
}