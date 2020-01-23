package com.example.eyad.ministryofinteriorproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eyad.ministryofinteriorproject.Model.InsideSea;
import com.example.osama.ministryofinteriorproject.R;

import java.util.ArrayList;

public class ShipsAdapter extends RecyclerView.Adapter<ShipsAdapter.MyHolder> {

    private Context context;
    private ArrayList<InsideSea>shipsList;

    public ShipsAdapter(Context context, ArrayList<InsideSea> shipsList) {
        this.context = context;
        this.shipsList = shipsList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ships_list,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.ship_number.setText(shipsList.get(position).getPlateNumberShip());
        holder.ship_gov.setText(shipsList.get(position).getGovernorateShip());
        holder.ship_owner.setText(shipsList.get(position).getOwnerShip());

        String names = "";
        for(int i=0;i<shipsList.get(position).getFishers().size();i++){
            names = names + shipsList.get(position).getFishers().get(i).getName() +"\n";
        }


        holder.fishersNames.setText(names);

        holder.user_governorate.setText(shipsList.get(position).getUserGovernorate());
        holder.user_name.setText(shipsList.get(position).getUserName());

        holder.current_date.setText(shipsList.get(position).getDate());
        holder.current_time.setText(shipsList.get(position).getTime());


    }

    @Override
    public int getItemCount() {
        return shipsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView ship_number,ship_gov,ship_owner;
        TextView fishersNames,fishersId,user_name,user_governorate;
        TextView current_date,current_time;

        public MyHolder(View itemView) {
            super(itemView);

            ship_number = itemView.findViewById(R.id.ship_number);
            ship_gov = itemView.findViewById(R.id.ship_gov);
            ship_owner = itemView.findViewById(R.id.ship_owner);
            fishersNames = itemView.findViewById(R.id.fishersNames);
            user_name = itemView.findViewById(R.id.user_name);
            user_governorate = itemView.findViewById(R.id.user_governorate);
            current_date = itemView.findViewById(R.id.current_date);
            current_time = itemView.findViewById(R.id.current_time);

        }
    }
}
