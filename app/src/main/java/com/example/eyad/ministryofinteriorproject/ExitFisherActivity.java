package com.example.eyad.ministryofinteriorproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.eyad.ministryofinteriorproject.Common.Common;
import com.example.eyad.ministryofinteriorproject.Database.MySQLiteHelper;
import com.example.eyad.ministryofinteriorproject.Model.Fisher;
import com.example.eyad.ministryofinteriorproject.Model.InsideSea;
import com.example.osama.ministryofinteriorproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.eyad.ministryofinteriorproject.Common.Common.fisherArrayList;
import static com.example.eyad.ministryofinteriorproject.Common.Common.fisherArrayListForDelete;

public class ExitFisherActivity extends AppCompatActivity {

    FButton btnOk,btnExit;
    EditText edtPlateNum;

    FirebaseDatabase database;
    DatabaseReference table_OnSea;
    DatabaseReference table_Fisher;
    DatabaseReference table_Fishers;

    List<InsideSea> fishersList;

    MaterialEditText edtFisherId;

    MySQLiteHelper mySQLiteHelper;


    TextView tvOwnerName,tvOwnerId,tvGovernorate,tvFishersName,tvFishersId;

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
        setContentView(R.layout.activity_exit_fisher);


        getSupportActionBar().setTitle("منظمة شؤون الصيادين - خروج صياد");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        database = FirebaseDatabase.getInstance();
        table_OnSea = database.getReference("OnSea");
        table_Fisher = database.getReference("Fisher");
        table_Fishers = database.getReference("Fishers");

        edtPlateNum = findViewById(R.id.edtShipId);
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
                else if(Common.currentUser.getState().equals("true")){

                    table_OnSea.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.child(edtPlateNum.getText().toString()).exists()){

                                InsideSea insideSea = dataSnapshot.child(edtPlateNum.getText().toString()).getValue(InsideSea.class);
                                insideSea.setPlateNumberShip(edtPlateNum.getText().toString());

                                if(Common.currentUser.getGovernorate().equals("شمال غزة") && insideSea.getGovernorateShip().equals("شمال غزة") ||
                                        Common.currentUser.getGovernorate().equals("غرب غزة") && insideSea.getGovernorateShip().equals("غرب غزة") ||
                                        Common.currentUser.getGovernorate().equals("جنوب غزة") && insideSea.getGovernorateShip().equals("جنوب غزة") ||
                                        Common.currentUser.getGovernorate().equals("غزة") && insideSea.getGovernorateShip().equals("غزة")) {

                                    ShowDialog(insideSea);
                                }else {
                                    Toast.makeText(getApplicationContext(), "لا تمتلك الصلاحية , خارج محافظتك !", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                }else {
                                Toast.makeText(ExitFisherActivity.this, "المركب لم يُسجل دخول لخروج صياد !", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(), "ليس لديك الصلاحية ,الحساب غير فعال !", Toast.LENGTH_SHORT).show();

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
    private void ShowDialog(final InsideSea insideSea){

        mySQLiteHelper = new MySQLiteHelper(getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(ExitFisherActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_fisher_layout,null);


        fishersList = new ArrayList<>();


        tvOwnerName = view.findViewById(R.id.ownerName);
        tvOwnerId = view.findViewById(R.id.ownerId);
        tvGovernorate = view.findViewById(R.id.governorate);
        tvFishersName = view.findViewById(R.id.fishersName);
        tvFishersId = view.findViewById(R.id.fishersId);

        edtFisherId = view.findViewById(R.id.edtFisherId);

        tvOwnerName.setText(insideSea.getOwnerShip());
        tvOwnerId.setText(insideSea.getIdShip());
        tvGovernorate.setText(insideSea.getPlateNumberShip());

        String name = "";
        String id = "";

        for(int i=0;i<insideSea.getFishers().size();i++){
            name = name + insideSea.getFishers().get(i).getName()+"\n";
        }
        tvFishersName.setText(name);

        for(int i=0;i<insideSea.getFishers().size();i++){
            id = id + insideSea.getFishers().get(i).getId()+"\n";
        }
        tvFishersId.setText(id);

        builder.setView(view);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(edtFisherId.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "الرجاء التأكد من تعبئة البيانات", Toast.LENGTH_SHORT).show();
                    return;
                }

                table_Fisher.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtFisherId.getText().toString()).exists()){


                            for(int i=0;i<fisherArrayList.size();i++){

                                if(edtFisherId.getText().toString().equals(fisherArrayList.get(i).getId())){

                                    table_Fisher.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final Fisher fisher1 = dataSnapshot.child(edtFisherId.getText().toString()).getValue(Fisher.class);
                                            fisher1.setId(edtFisherId.getText().toString());

                                            table_Fishers.child(edtFisherId.getText().toString()).removeValue();

                                            table_OnSea.child(insideSea.getPlateNumberShip()).child("fishers").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                                    fisherArrayListForDelete = new ArrayList<>();
                                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                        fisherArrayListForDelete.add(snapshot.getValue(Fisher.class));
                                                    }
                                                    for (int i = 0; i<fisherArrayListForDelete.size();i++){
                                                        if(edtFisherId.getText().toString().equals(fisherArrayListForDelete.get(i).getId())){
                                                            fisherArrayListForDelete.remove(i);
                                                        }
                                                    }
                                                    dataSnapshot.getRef().setValue(fisherArrayListForDelete);
                                                    Toast.makeText(ExitFisherActivity.this,"تم خروج صياد: " +fisher1.getName(), Toast.LENGTH_LONG).show();
                                                    ExitFisherActivity.this.finish();
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                        else {
                            Toast.makeText(ExitFisherActivity.this, "رقم الهوية غير مسجل !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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



}
