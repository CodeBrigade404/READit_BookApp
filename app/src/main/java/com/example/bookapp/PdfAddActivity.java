package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityPdfAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfAddActivity extends AppCompatActivity {

    //setup view binding
    private ActivityPdfAddBinding binding;

    //progress dialog
    private ProgressDialog progressDialog;

    //fire base auth
    private FirebaseAuth firebaseAuth;

    //arrayList hold pdf categories
    private ArrayList<ModelCategory> categoryArrayList;

    private static final int PDF_PICK_CODE = 1000;
    //tag for debugging
    private static final String TAG = "ADD_PDF_TAG";

    //Uri of picked Uri
    private Uri pdfUri;

    public PdfAddActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfAddBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        loadPdfCategories();

        //handle click ,go to previous activity
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle click ,go to previous activity
         binding.backBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 onBackPressed();
             }
         });

        //handle click ,attach pdf
        binding.attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfPickIntent();
            }
        });

        //handle click ,pick category
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPickDialog();
            }
        });

        //handle click ,upload pdf
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate data
                validateData();
                
            }
        });

    }

    private String title = "" , description = "",category = "";
    private void validateData() {
        Log.d(TAG, "validateData: validating data...");
        //Step 1:validate data

        //get data
        title = binding.titleEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();
        category = binding.categoryTv.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        }else  if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        }else  if (TextUtils.isEmpty(category)){
            Toast.makeText(this, "Pick Category", Toast.LENGTH_SHORT).show();
        }else if(pdfUri == null){
            Toast.makeText(this, "Pick Pdf ", Toast.LENGTH_SHORT).show();
        }else {
            //all data is valid, can upload now
            uploadPdfToStore();
        }
    }

    private void uploadPdfToStore() {
        Log.d(TAG, "uploadPdfToStore: uploading to storage....");

        //show progress
        progressDialog.setMessage("Uploading pdf");
        progressDialog.show();

        //timestamp
        long timestamp = System.currentTimeMillis();
        //path of pdf in firebase storage
        String filepathAndName = "Books/" + timestamp;

        //storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filepathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: PDF uploaded to storage..");
                        Log.d(TAG, "onSuccess: getting pdf uri");

                        //get pdf url
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String upoadedPdfUrl = ""+uriTask.getResult();

                        //upload to firebase db
                        uploadPdfInfoToDb(upoadedPdfUrl,timestamp);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: pdf file upload failed due to " + e.getMessage());
                        Toast.makeText(PdfAddActivity.this, "onFailure: pdf file upload failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPdfInfoToDb(String upoadedPdfUrl, long timestamp) {
        //Step 3: Uploaded pdf into fire base storage
        Log.d(TAG, "uploadPdfIntoDb: PDF uploaded into firebase db");
        progressDialog.setMessage("uploading pdf info..");
        String uid = firebaseAuth.getUid();

        //setup data to upload
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+uid);
        hashMap.put("id",""+timestamp);
        hashMap.put("title",""+title);
        hashMap.put("description",""+description);
        hashMap.put("category",""+category);
        hashMap.put("url",""+upoadedPdfUrl);
        hashMap.put("timestamp",timestamp);

        //db reference :DB >Books
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(""+ timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onSuccess: uploaded Successfully..");
                        Toast.makeText(PdfAddActivity.this, "Successfully uploaded..", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: Fail to upload to db due to  "+e.getMessage());
                        Toast.makeText(PdfAddActivity.this, "Fail to upload to db due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }



    private void loadPdfCategories() {
        Log.d(TAG, "loadPdfCategories: loading Pdf Categories...");
        categoryArrayList = new ArrayList<>();

        //db reference to load categories"
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();//clear before adding
                for (DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    ModelCategory model = ds.getValue(ModelCategory.class);

                    //add to arraylist
                    categoryArrayList.add(model);

                    Log.d(TAG, "onDataChange: "+model.getCategory());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void categoryPickDialog() {
        Log.d(TAG, "categoryPickDialog: showing category Pick dialog");

        //get array of categories from arraylist
        String[] categoriesArray = new String[categoryArrayList.size()];
        for (int i = 0 ; i < categoryArrayList.size(); i++){
             categoriesArray[i] = categoryArrayList.get(i).getCategory();

        }

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item click
                        //get click item from dialog list

                        String category = categoriesArray[which];
                        //set to category textview
                        binding.categoryTv.setText(category);

                        Log.d(TAG, "onClick: selected Category" + category);

                    }
                })
                .show();

    }

    private void pdfPickIntent() {
        Log.d(TAG,"pdfPickIntend: starting pdf pick Intent");
        Intent intent  = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf"),PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK){
            if (requestCode == PDF_PICK_CODE){
                Log.d(TAG,"onActivityResult: PDF Picked");
                 pdfUri = data.getData();
                 Log.d(TAG,"onActivityResult : URI"+pdfUri);

            }
        }else {
            Log.d(TAG,"onActivityResult : Cancelled picking ");
            Toast.makeText(this, "Cancelled picking", Toast.LENGTH_SHORT).show();
        }
    }
}