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

import com.example.ajinafro.R;
import com.example.ajinafro.models.City;
import com.example.ajinafro.utils.CityPickerActivity;

import java.util.ArrayList;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {
    public CitiesAdapter(@NonNull Context context, @NonNull ArrayList<City> cities, CityPickerActivity cityPickerActivity) {
        this.mcontext=context;
        this.cities=cities;
        this.cityPickerActivity=cityPickerActivity;
        this.inflater= LayoutInflater.from(context);
    }
    public CityPickerActivity cityPickerActivity;
    private int currentSelection=-1;
    static String TAG="CitiesAdapter";
    private Context mcontext;
    private ArrayList<City> cities;
    public LayoutInflater inflater;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.citypicker_single_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cityname.setText(cities.get(position).getName());
        holder.itemView.setSelected(position==currentSelection);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSelection!=position){
                    currentSelection=position;
                    notifyDataSetChanged();
                }
                ((EditText)(cityPickerActivity.findViewById(R.id.citypicker_cityname))).setHint(cities.get(position).getName());
                cityPickerActivity.setSelectedCity(cities.get(position).getName());
                Log.d(TAG, "onClick: on "+cities.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView cityname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityname=itemView.findViewById(R.id.citypicker_name);
        }
    }

}
