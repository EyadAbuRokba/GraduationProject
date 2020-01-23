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

import com.example.eyad.ministryofinteriorproject.Adapter.ShipsAdapter;
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

public class ShipsListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    DatabaseReference table_OnSea;
    ShipsAdapter adapter;
    ArrayList<InsideSea> shipsList;

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
        setContentView(R.layout.activity_ships_list);
        showDialog();

        getSupportActionBar().setTitle("بيانات المراكب داخل البحر");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.ships_list);

        shipsList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        table_OnSea = database.getReference("OnSea");


        table_OnSea.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    shipsList.add(snapshot.getValue(InsideSea.class));
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new ShipsAdapter(ShipsListActivity.this,shipsList);
                recyclerView.setAdapter(adapter);
                hideDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void showDialog() {
        progressDialog = new ProgressDialog(ShipsListActivity.this);
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
        ArrayList<InsideSea> arrayList = new ArrayList<>();

        for(InsideSea insideSea : shipsList){
            if(insideSea.getPlateNumberShip().toLowerCase().contains(s.toLowerCase())){
                arrayList.add(insideSea);
            }
        }

        adapter = new ShipsAdapter(getApplicationContext(),arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        return true;
    }
}
