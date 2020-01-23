package com.example.eyad.ministryofinteriorproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.eyad.ministryofinteriorproject.Common.Common;
import com.example.eyad.ministryofinteriorproject.Database.MySQLiteHelper;
import com.example.eyad.ministryofinteriorproject.Model.InsideSea;
import com.example.osama.ministryofinteriorproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeScreenActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeLayout;
    TextView admin ,TvCountFisher,TvcountFisherOnSea,TvcountShips;
    FButton btnExit,btnAdd;
    FirebaseDatabase database;
    DatabaseReference table_fisher;
    DatabaseReference table_OnSea;
    DatabaseReference table_fishers;
    int fisherCount;
    MySQLiteHelper mySQLiteHelper;

    int fishers,ships;
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
        setContentView(R.layout.activity_home_screen);

        getSupportActionBar().setTitle("منظمة شؤون الصيادين - لوحة التحكم");

        mySQLiteHelper = new MySQLiteHelper(getApplicationContext());


        swipeLayout = findViewById(R.id.swipeRefresh);
        admin = findViewById(R.id.admin);
        TvCountFisher = findViewById(R.id.TvCountFisher);
        TvcountFisherOnSea = findViewById(R.id.TvcountFisherOnSea);
        TvcountShips = findViewById(R.id.TvcountShips);
        btnExit = findViewById(R.id.exit);
        btnAdd = findViewById(R.id.add);

        database = FirebaseDatabase.getInstance();
        table_fisher = database.getReference("Fisher");
        table_OnSea = database.getReference("OnSea");
        table_fishers = database.getReference("Fishers");

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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        table_fisher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    TvCountFisher.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        table_OnSea.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ships = (int) dataSnapshot.getChildrenCount();

                    TvcountShips.setText(dataSnapshot.getChildrenCount()+ "");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        table_OnSea.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    fisherCount += childSnapshot.child("fishers").getChildrenCount();
                }

                fishers = fisherCount;
                TvcountFisherOnSea.setText(fisherCount+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TvCountFisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FishersListActivity.class);
                startActivity(intent);
            }
        });

        TvcountShips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ships == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
                    builder.setTitle("غير مسموح !");
                    builder.setMessage("لا يوجد مراكب داخل البحر");


                    builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else{
                    Intent intent = new Intent(getApplicationContext(),ShipsListActivity.class);
                    startActivity(intent);
                }
            }
        });
        TvcountFisherOnSea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fishers == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
                    builder.setTitle("غير مسموح !");
                    builder.setMessage("لا يوجد صيادين داخل البحر");


                    builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Intent intent = new Intent(getApplicationContext(),FishersOnSeaActivity.class);
                    startActivity(intent);
                }
            }
        });


        swipeLayout.setColorSchemeResources(R.color.colorAccent);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
                swipeLayout.setRefreshing(false);
            }
        });

        GridLayout mainGrid;
        mainGrid =  findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);

        admin.setText(Common.currentUser.getName());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddUserActivity.class));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
                builder.setTitle("نهاية الشفت");
                builder.setMessage("هل تريد بالتأكيد تسجيل خروج الشفت ؟");
                builder.setIcon(R.drawable.ic_exit_to_app_white_24dp);

                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        Intent Exit = new Intent(HomeScreenActivity.this, LoginActivity.class);
                        Exit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(Exit);
                    }
                });
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void setSingleEvent(GridLayout mainGrid){
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    switch (finalI){

                        case 0 :
                            startActivity(new Intent(getApplicationContext(),AddShipActivity.class));
                            break;
                        case 1 :
                            startActivity(new Intent(getApplicationContext(),ExitShipActivity.class));
                            break;
                        case 2 :
                            startActivity(new Intent(getApplicationContext(),AddFisherActivity.class));
                            break;
                        case 3 :
                            startActivity(new Intent(getApplicationContext(),ExitFisherActivity.class));
                            break;

                    }


                }
            });
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "للخروج اضغط مرة اخرى", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
