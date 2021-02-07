package com.example.ajinafro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ajinafro.R;
import com.example.ajinafro.models.Carpool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CarpoolsAdapter extends RecyclerView.Adapter<CarpoolsAdapter.ViewHolder> {
    ArrayList<Carpool> carpoolArrayList;
    Context mcontext;
    LayoutInflater inflater;
    static String TAG="CarpoolsAdapter";

    public CarpoolsAdapter(Context context,ArrayList<Carpool> carpoolArrayList) {
        mcontext=context;
        this.carpoolArrayList=carpoolArrayList;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=inflater.inflate(R.layout.carpool_single_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Carpool item=carpoolArrayList.get(position);
        Locale locale = new Locale("fr", "FR");
        String pattern = "E dd/MM";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern,locale);
        String date_formated = simpleDateFormat.format(item.getStart_date().toDate());
        /////////////
        holder.date.setText(date_formated);

        String minutes=String.valueOf(item.getStart_minutes());
        if(minutes.length()==1){
                minutes="0"+minutes;
        }
        String hours=String.valueOf(item.getStart_hours());
        if(hours.length()==1){
            hours="0"+hours;
        }

        holder.time.setText(hours+"h "+minutes);
        holder.price.setText(item.getPrice()+" Dhs");
        holder.places.setText(" "+item.getAvailable_places());
        holder.startC.setText(item.getStart_city());
        holder.endC.setText(item.getEnd_city());
    }

    @Override
    public int getItemCount() {
        return carpoolArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView startC,endC;
        TextView price,places,time,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startC=itemView.findViewById(R.id.ville_depart);
            endC=itemView.findViewById(R.id.ville_d_arrivee);
            price=itemView.findViewById(R.id.price_case);
            places=itemView.findViewById(R.id.place_case);
            time=itemView.findViewById(R.id.heure_case);
            date=itemView.findViewById(R.id.date_case);
        }
    }
}
