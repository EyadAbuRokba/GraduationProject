package com.example.eyad.ministryofinteriorproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;

import com.example.eyad.ministryofinteriorproject.Adapter.FishersOnSeaAdapter;
import com.example.eyad.ministryofinteriorproject.Common.Common;
import com.example.eyad.ministryofinteriorproject.Model.Fisher;
import com.example.eyad.ministryofinteriorproject.Model.InsideSea;
import com.example.osama.ministryofinteriorproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FishersOnSeaActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    FishersOnSeaAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference table_fishers;
    DatabaseReference table_OnSea;
    ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Monadi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_fishers_on_sea);

        getSupportActionBar().setTitle("بيانات الصيادين داخل البحر");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showDialog();
        database = FirebaseDatabase.getInstance();
        table_fishers = database.getReference("Fishers");
        table_OnSea = database.getReference("OnSea");
        recyclerView = findViewById(R.id.fishers_list);

        final ArrayList<InsideSea> seaArrayList = new ArrayList<>();
        table_OnSea.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    seaArrayList.add(snapshot.getValue(InsideSea.class));
                }
                Common.fisherArrayList.clear();
                for(int i=0; i<seaArrayList.size();i++){
                    Common.fisherArrayList.addAll(seaArrayList.get(i).getFishers());
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(FishersOnSeaActivity.this));
                adapter = new FishersOnSeaAdapter(FishersOnSeaActivity.this, Common.fisherArrayList);
                recyclerView.setAdapter(adapter);
                hideDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void showDialog() {
        progressDialog = new ProgressDialog(FishersOnSeaActivity.this);
        progressDialog.setMessage("تحميل ....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("البحث");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Monadi.ttf")
                .build());
        ArrayList<Fisher> arrayList = new ArrayList<>();

        for(Fisher fisher : Common.fisherArrayList){
            if(fisher.getName().toLowerCase().contains(s.toLowerCase())){
                arrayList.add(fisher);
            }
        }

        adapter = new FishersOnSeaAdapter(getApplicationContext(),arrayList);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        return true;
    }


}
