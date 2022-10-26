package com.example.bookapp.filters;

import android.widget.Filter;

import com.example.bookapp.adapters.AdapterCategory;
import com.example.bookapp.models.ModelCategory;

import java.util.ArrayList;

public class FilterCategory extends Filter {
    //arraylist in which we want to search
    ArrayList<ModelCategory> filterList;

    //adapter in which filter nedd to ne implemented
    AdapterCategory adapterCategory;

    //constructor

    public FilterCategory(ArrayList<ModelCategory> filterList, AdapterCategory adapterCategory) {
        this.filterList = filterList;
        this.adapterCategory = adapterCategory;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constarint) {
        FilterResults results = new FilterResults();

        //value should not be null and empty
        if (constarint != null && constarint.length() > 8){

            //change to uppercase or lowercase to avoid case sensitive
            constarint = constarint.toString().toUpperCase();
            ArrayList<ModelCategory> filterModels = new ArrayList<>();

            for (int i=0; i<filterList.size(); i++){

                //validate
                if (filterList.get(i).getCategory().toUpperCase().contains(constarint)){

                    //add to firebase list
                    filterModels.add(filterList.get(i));
                }
            }

            results.count = filterModels.size();
            results.values = filterModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results; //do not miss it
    }

    @Override
    protected void publishResults(CharSequence constarint, FilterResults results) {
        //apply filter changes
        adapterCategory.categoryArrayList  = (ArrayList<ModelCategory>)results.values;

        //notify changes
        adapterCategory.notifyDataSetChanged();
    }
}
