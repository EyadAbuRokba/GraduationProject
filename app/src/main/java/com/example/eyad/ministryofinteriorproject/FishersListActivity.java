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

import com.example.eyad.ministryofinteriorproject.Adapter.FishersAdapter;
import com.example.eyad.ministryofinteriorproject.Model.Fisher;
import com.example.osama.ministryofinteriorproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FishersListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference table_Fisher;
    ArrayList<Fisher>fisherList;
    FishersAdapter adapter;

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

        setContentView(R.layout.activity_fishers_list);
        showDialog();

        getSupportActionBar().setTitle("بيانات الصيادين");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        fisherList = new ArrayList<>();


        recyclerView = findViewById(R.id.fishers_list);
        database = FirebaseDatabase.getInstance();
        table_Fisher = database.getReference("Fisher");

        table_Fisher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    snapshot.getValue(Fisher.class).setId(String.valueOf(snapshot.getRef().getRoot()));
                    fisherList.add(snapshot.getValue(Fisher.class));
                    fisherList.get(i).setId(snapshot.getKey());
                    i++;
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(FishersListActivity.this));
                adapter = new FishersAdapter(FishersListActivity.this,fisherList);
                recyclerView.setAdapter(adapter);
                hideDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    public void showDialog() {
        progressDialog = new ProgressDialog(FishersListActivity.this);
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

        for(Fisher fisher : fisherList){
            if(fisher.getName().toLowerCase().contains(s.toLowerCase())){
                arrayList.add(fisher);
            }
        }
        adapter = new FishersAdapter(getApplicationContext(),arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        return true;
    }

}
