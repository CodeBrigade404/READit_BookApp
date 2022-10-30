package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.bookapp.databinding.ActivityCategoryEditBinding;


public class CategoryEditActivity extends AppCompatActivity {
    private ActivityCategoryEditBinding activityCategoryEditBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityCategoryEditBinding = ActivityCategoryEditBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityCategoryEditBinding.getRoot());
        activityCategoryEditBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}