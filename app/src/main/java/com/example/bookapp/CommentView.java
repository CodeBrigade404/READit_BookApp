package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bookapp.databinding.ActivityCommentViewBinding;

public class CommentView extends AppCompatActivity {

    private ActivityCommentViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}