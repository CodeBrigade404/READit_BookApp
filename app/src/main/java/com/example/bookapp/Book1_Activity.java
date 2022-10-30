package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.bookapp.databinding.ActivityBook1Binding;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class Book1_Activity extends AppCompatActivity {
    private ActivityBook1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        File file = new File("C:\\Users\\User\\Desktop\\READit_BookApp\\app\\src\\main\\assets");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "book1/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}