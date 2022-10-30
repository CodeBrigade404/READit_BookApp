package com.example.bookapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.PdfListAdminActivity;
import com.example.bookapp.databinding.RowBookBinding;
import com.example.bookapp.databinding.RowCategoryBinding;
import com.example.bookapp.filters.FilterCategory;
import com.example.bookapp.models.ModelBook;
import com.example.bookapp.models.ModelCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.HolderCategory>  {
    private Context context;
    public ArrayList<ModelBook> categoryArrayList;

    //view binding
    private RowBookBinding binding;

    private FirebaseAuth firebaseAuth;
//    //instance of filter class
//    private FilterCategory filter;

    public AdapterBook(Context context, ArrayList<ModelBook> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;

    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind row_category.xml
        binding = RowBookBinding.inflate(LayoutInflater.from(context),parent,false);

        return new HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBook.HolderCategory holder, int position) {
        //get data
        ModelBook model = categoryArrayList.get(position);
        String id = model.getId();
        String book  = model.getBook();
        String uid = model.getUid();
        long timestamp  =model.getTimestamp();

        //set date
        holder.bookTv.setText(book);

        //handle click ,delete category
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //confirm delete dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this category?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //begin delete
                                Toast.makeText(context, "Deleting....", Toast.LENGTH_SHORT).show();
                                deleteCategory(model , holder);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                             dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        //handle item click,goto pdf, also pass pdf category and categoryid
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PdfListAdminActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("book",book);
                context.startActivity(intent);
            }
        });

    }

    private void deleteCategory(ModelBook model, HolderCategory holder) {
        //get id of category to delete
        String id = model.getId();
        //firebase db > categories > categoryId
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(firebaseAuth.getUid()).child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //deleted Successfully
                        Toast.makeText(context, "Successfully Deleted....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to delete
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    //@Override
//    public Filter getFilter() {
//        if(filter == null) {
//            filter = new FilterCategory(filterList, this);
//        }
//        return filter;
//    }

    /*View Holder  class to hold UI views for row_category.xml*/
    class HolderCategory extends RecyclerView.ViewHolder{
        //UI view of row_category.xml
        TextView bookTv;
        ImageButton deleteBtn;
        public HolderCategory(@NonNull View itemView) {
            super(itemView);
            //init UI views

            bookTv = binding.bookTv;
            deleteBtn = binding.deleteBookBtn;
        }
    }
}
