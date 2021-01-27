package com.example.ajinafro.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ajinafro.MainActivity;
import com.example.ajinafro.R;
import com.example.ajinafro.newPost.AddPostDetailsActivity;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    public static String TAG="CategoriesAdapter";
    private ArrayList<String> categories_names;
    private ArrayList<Integer> categories_images;
    private LayoutInflater inflater;
    private Context mcontext;

    public CategoriesAdapter(@NonNull Context context,@NonNull ArrayList<String> categories_names,@NonNull ArrayList<Integer> categories_images) {
        this.categories_names = categories_names;
        this.categories_images = categories_images;
        inflater= LayoutInflater.from(context);
        mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.categorie_single_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.cat_name.setText(categories_names.get(position));
    holder.itemView.setBackgroundResource(categories_images.get(position));
    //holder.cat_image.setImageResource(categories_images.get(position));
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: on"+categories_names.get(position));
            Intent nextactivity=new Intent(mcontext.getApplicationContext(), AddPostDetailsActivity.class);
            nextactivity.putExtra("categorie_name",categories_names.get(position));
            nextactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mcontext.getApplicationContext().startActivity(nextactivity);
        }

    });
    }

    @Override
    public int getItemCount() {
        return categories_images.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cat_image;
        TextView cat_name;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        cat_name=itemView.findViewById(R.id.option_name);
        cat_image=itemView.findViewById(R.id.option_image);
    }
}
}
