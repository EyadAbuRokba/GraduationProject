package com.example.eyad.ministryofinteriorproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ExitShipActivity extends AppCompatActivity {

    EditText edtPlateNum;
    FButton btnOk,btnExit;

    FirebaseDatabase database;
    DatabaseReference table_OnSea;
    DatabaseReference table_fishers;
    MySQLiteHelper mySQLiteHelper;

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
        setContentView(R.layout.activity_exit_ship);

        getSupportActionBar().setTitle("منظمة شؤون الصيادين - خروج مركبة");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mySQLiteHelper = new MySQLiteHelper(getApplicationContext());

        database = FirebaseDatabase.getInstance();
        table_OnSea = database.getReference("OnSea");
        table_fishers = database.getReference("fishers");

        edtPlateNum = findViewById(R.id.edtPlateNum);
        btnOk = findViewById(R.id.btnOk);
        btnExit = findViewById(R.id.btnExit);

        BottomNavigationView bottomNavigationView =  findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Fishers:
                        startActivity(new Intent(getApplicationContext(), FishersListActivity.class));
                        break;
                    case R.id.action_ships:
                        startActivity(new Intent(getApplicationContext(), ShipsListActivity.class));
                        break;
                    case R.id.action_FisherOnSea:
                        startActivity(new Intent(getApplicationContext(), FishersOnSeaActivity.class));
                        break;
                }
                return true;
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtPlateNum.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "الرجاء التأكد من تعبئة البيانات", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (Common.currentUser.getState().equals("true")) {

                        table_OnSea.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(edtPlateNum.getText().toString()).exists()) {//35-0

                                    final InsideSea insideSea = dataSnapshot.child(edtPlateNum.getText().toString()).getValue(InsideSea.class);
                                    insideSea.setPlateNumberShip(edtPlateNum.getText().toString());



                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && insideSea.getGovernorateShip().equals("شمال غزة") ||
                                            Common.currentUser.getGovernorate().equals("غرب غزة") && insideSea.getGovernorateShip().equals("غرب غزة") ||
                                            Common.currentUser.getGovernorate().equals("جنوب غزة") && insideSea.getGovernorateShip().equals("جنوب غزة") ||
                                            Common.currentUser.getGovernorate().equals("غزة") && insideSea.getGovernorateShip().equals("غزة")) {


                                        AlertDialog.Builder builder = new AlertDialog.Builder(ExitShipActivity.this);
                                        builder.setTitle("تأكيد !");
                                        builder.setMessage("هل انت متأكد من تسجيل خروج المركب " + edtPlateNum.getText().toString());

                                        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {

                                                dataSnapshot.child(edtPlateNum.getText().toString()).getRef().removeValue();

                                                Toast.makeText(ExitShipActivity.this,"تم تسجيل خروج المركب مع الصيادين  " +insideSea.getPlateNumberShip(), Toast.LENGTH_SHORT).show();
                                                ExitShipActivity.this.finish();
                                            }
                                        });

                                        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                        AlertDialog alert = builder.create();
                                        alert.show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "لا تمتلك الصلاحية , خارج محافظتك !", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(ExitShipActivity.this, "المركب ليس داخل البحر !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "ليس لديك الصلاحية ,الحساب غير فعال !", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "الرجاء تأكد بأتصال الانترنت", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
