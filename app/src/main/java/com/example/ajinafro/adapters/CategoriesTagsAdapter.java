package com.example.ajinafro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ajinafro.FiltersPickerActivity;
import com.example.ajinafro.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class CategoriesTagsAdapter extends RecyclerView.Adapter<CategoriesTagsAdapter.ViewHolder> {
    public static String TAG="CategoriesTagsAdapter";
    private ArrayList<String> categories_names;
    private LayoutInflater inflater;
    private ArrayList<String> selected_categories=new ArrayList<>();
    private FiltersPickerActivity filtersPickerActivity;
    private Context mcontext;
    public CategoriesTagsAdapter(@NonNull Context context, @NonNull ArrayList<String> categories_names, FiltersPickerActivity cityPickerActivity) {
        this.categories_names = categories_names;
        inflater= LayoutInflater.from(context);
        this.filtersPickerActivity=cityPickerActivity;
        mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.category_tag_single_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cat_name.setText(categories_names.get(position));
        holder.itemView.setSelected(selected_categories.contains(categories_names.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_categories.contains( categories_names.get(position) ) ){
                    selected_categories.remove(categories_names.get(position));
                    notifyDataSetChanged();
                }
                else{
                    if(selected_categories.size()<10){
                        selected_categories.add(categories_names.get(position));
                        notifyDataSetChanged();
                    }else{
                        Snackbar snackbar=Snackbar.make(holder.itemView,"Vous pouvez maximun choisir 10 catégories", BaseTransientBottomBar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                Log.d(TAG, "onClick: categories_selected "+selected_categories.toString());
                filtersPickerActivity.setSelectedCategories(selected_categories);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cat_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_name=itemView.findViewById(R.id.tag_category_name);
        }
    }

}
