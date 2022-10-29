package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bookapp.databinding.ActivityBook1Binding;
import com.github.barteksc.pdfviewer.PDFView;

public class Book1_Activity extends AppCompatActivity {
    private ActivityBook1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        PDFView pdfView = findViewById(R.id.pdf_view);

        pdfView.fromAsset("book1.pdf").load();
    }
}