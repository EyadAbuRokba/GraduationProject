package com.example.eyad.ministryofinteriorproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eyad.ministryofinteriorproject.Model.Fisher;
import com.example.osama.ministryofinteriorproject.R;

import java.util.ArrayList;

public class FishersOnSeaAdapter extends RecyclerView.Adapter<FishersOnSeaAdapter.MyHolder> {

    private Context context;
    private ArrayList<Fisher>fishersList;

    public FishersOnSeaAdapter(Context context, ArrayList<Fisher> fishersList) {
        this.context = context;
        this.fishersList = fishersList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fishers_list,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.ownerId.setText(fishersList.get(position).getId());
        holder.ownerName.setText(fishersList.get(position).getName());
        holder.ownerGovernorate.setText(fishersList.get(position).getGovernorate());
    }

    @Override
    public int getItemCount() {
        return fishersList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView ownerName,ownerId,ownerGovernorate,ship_id,current_date;
        public MyHolder(View itemView) {
            super(itemView);

            ownerName = itemView.findViewById(R.id.ownerName);
            ownerId = itemView.findViewById(R.id.ownerId);
            ownerGovernorate = itemView.findViewById(R.id.ownerGovernorate);
            ship_id = itemView.findViewById(R.id.ship_id);
            current_date = itemView.findViewById(R.id.current_date);
        }
    }
}
