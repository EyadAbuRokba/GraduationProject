package com.example.eyad.ministryofinteriorproject.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.eyad.ministryofinteriorproject.FishersListActivity;
import com.example.eyad.ministryofinteriorproject.ShipsListActivity;
import com.example.eyad.ministryofinteriorproject.Common.Common;
import com.example.eyad.ministryofinteriorproject.Database.MySQLiteHelper;
import com.example.eyad.ministryofinteriorproject.FishersOnSeaActivity;
import com.example.eyad.ministryofinteriorproject.Model.Fisher;
import com.example.eyad.ministryofinteriorproject.Model.InsideSea;
import com.example.eyad.ministryofinteriorproject.Model.Ship;
import com.example.osama.ministryofinteriorproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.eyad.ministryofinteriorproject.Common.Common.fisherArrayList;

public class ActivityOne extends AppCompatActivity {

    EditText edtPlateNum, edtFishersNum;
    MaterialEditText edtIdOne, edtIdTwo, edtIdThree, edtIdFour, edtIdFive;
    FButton btnOk, btnExit;
    Button btnInsert, btnBack;
    TextView ownerName, ownerId, governorate, items;
    MySQLiteHelper mySQLiteHelper;

    List<Fisher> fishers;
    String shipType;

    DatabaseReference ship_table;
    DatabaseReference Fisher_table;
    DatabaseReference InsideSea_table;
    DatabaseReference Fishers_table;

    Fisher fisher1, fisher2, fisher3, fisher4, fisher5;

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
        setContentView(R.layout.activity_one);


        getSupportActionBar().setTitle("إضافة بيانات المركبة");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ship_table = FirebaseDatabase.getInstance().getReference("Ship");
        Fisher_table = FirebaseDatabase.getInstance().getReference("Fisher");
        InsideSea_table = FirebaseDatabase.getInstance().getReference("OnSea");
        Fishers_table = FirebaseDatabase.getInstance().getReference("Fishers");


        mySQLiteHelper = new MySQLiteHelper(getApplicationContext());

        edtPlateNum = findViewById(R.id.edtPlateNum);
        edtFishersNum = findViewById(R.id.edtFishersNum);

        items = findViewById(R.id.items);

        btnOk = findViewById(R.id.btnOk);
        btnExit = findViewById(R.id.btnExit);

        if (getIntent() != null) {
            shipType = getIntent().getStringExtra("shipType");
            items.setText(shipType);
        }

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

                if (edtPlateNum.getText().toString().isEmpty() || edtFishersNum.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "الرجاء التأكد من تعبئة البيانات", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (Common.currentUser.getState().equals("true")) {

                        ship_table.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(edtPlateNum.getText().toString()).exists()) {//35-0


                                    final Ship ship = dataSnapshot.child(edtPlateNum.getText().toString()).getValue(Ship.class);
                                    ship.setPlateNumber(edtPlateNum.getText().toString());

                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && ship.getGovernorate().equals("شمال غزة") ||
                                            Common.currentUser.getGovernorate().equals("غرب غزة") && ship.getGovernorate().equals("غرب غزة") ||
                                            Common.currentUser.getGovernorate().equals("جنوب غزة") && ship.getGovernorate().equals("جنوب غزة") ||
                                            Common.currentUser.getGovernorate().equals("شرق غزة") && ship.getGovernorate().equals("شرق غزة")) {


                                        InsideSea_table.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.child(edtPlateNum.getText().toString()).exists()) {
                                                    Toast.makeText(ActivityOne.this, "المركبة داخل البحر !", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    switch (Integer.parseInt(edtFishersNum.getText().toString())) {
                                                        case 0:
                                                            Toast.makeText(ActivityOne.this, "يجب أن يكون عدد الصيادين على المركب 2 فأكثر", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        case 1:
                                                            Toast.makeText(ActivityOne.this, "يجب أن يكون عدد الصيادين على المركب 2 فأكثر", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        case 2:
                                                            ShowDialog(2, ship);
                                                            break;
                                                        case 3:
                                                            ShowDialog(3, ship);
                                                            break;
                                                        case 4:
                                                            ShowDialog(4, ship);
                                                            break;
                                                        case 5:
                                                            ShowDialog(5, ship);
                                                            break;
                                                        default:
                                                            Toast.makeText(ActivityOne.this, "يجب أن يكون عدد الصيادين عالأكتر 5", Toast.LENGTH_SHORT).show();
                                                            break;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }else {
                                        Toast.makeText(ActivityOne.this, "لا تمتلك الصلاحية,المركب خارج محافظتك !", Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(ActivityOne.this, "لا يوجد رقم مركبة بهذا الرقم !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(ActivityOne.this, "ليس لديك الصلاحية,الحساب غير فعال !", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "الرجاء تأكد بأتصال الأنترنت", Toast.LENGTH_SHORT).show();
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

    private void ShowDialog(final int i, final Ship ship) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOne.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_ship_layout, null);

        edtIdOne = view.findViewById(R.id.edtIdOne);
        edtIdTwo = view.findViewById(R.id.edtIdTwo);
        edtIdThree = view.findViewById(R.id.edtIdThree);
        edtIdFour = view.findViewById(R.id.edtIdFour);
        edtIdFive = view.findViewById(R.id.edtIdFive);

        if (i == 3) {
            edtIdThree.setVisibility(View.VISIBLE);
        } else if (i == 4) {
            edtIdThree.setVisibility(View.VISIBLE);
            edtIdFour.setVisibility(View.VISIBLE);
        } else if (i == 5) {
            edtIdThree.setVisibility(View.VISIBLE);
            edtIdFour.setVisibility(View.VISIBLE);
            edtIdFive.setVisibility(View.VISIBLE);
        }

        ownerName = view.findViewById(R.id.ownerName);
        ownerId = view.findViewById(R.id.ownerId);
        governorate = view.findViewById(R.id.governorate);

        ownerName.setText(ship.getOwner());
        ownerId.setText(ship.getId());
        governorate.setText(ship.getGovernorate());

        btnInsert = view.findViewById(R.id.btnInsert);
        btnBack = view.findViewById(R.id.btnBack);

        builder.setView(view);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (i) {
                    case 2:
                        fishers = new ArrayList<>();

                        if (edtIdOne.getText().toString().isEmpty() || edtIdTwo.getText().toString().isEmpty()) {
                            Toast.makeText(getBaseContext(), "الرجاء ادخال هويات الصيادين !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (edtIdOne.getText().toString().equals(edtIdTwo.getText().toString())) {
                            Toast.makeText(getBaseContext(), "الرجاء عدم تكرار رقم الهوية !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (final Fisher fisher : fisherArrayList) {
                            if (edtIdOne.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الاولى مسجله دخول في البحر!" + edtIdOne.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdTwo.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الثانيه مسجله دخول في البحر!" + edtIdTwo.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Fisher_table.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                 if(!dataSnapshot.child(edtIdOne.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الاول غير مسجل !", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if(!dataSnapshot.child(edtIdTwo.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الثاني غير مسجل !", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (dataSnapshot.child(edtIdOne.getText().toString()).exists()) {
                                    fisher1 = dataSnapshot.child(edtIdOne.getText().toString()).getValue(Fisher.class);
                                    fisher1.setId(edtIdOne.getText().toString());
                                    fishers.add(fisher1);
                                }

                                if (dataSnapshot.child(edtIdTwo.getText().toString()).exists()) {
                                    fisher2 = dataSnapshot.child(edtIdTwo.getText().toString()).getValue(Fisher.class);
                                    fisher2.setId(edtIdTwo.getText().toString());
                                    fishers.add(fisher2);

                                    Calendar calendar = Calendar.getInstance();

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat sdd = new SimpleDateFormat("h:mm a");
                                    String strDate = sdf.format(calendar.getTime());
                                    String strTime = sdd.format(calendar.getTime());

                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher1.getGovernorate().equals("شمال غزة") ||
                                            Common.currentUser.getGovernorate().equals("غرب غزة") && fisher1.getGovernorate().equals("غرب غزة") ||
                                            Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher1.getGovernorate().equals("جنوب غزة") ||
                                            Common.currentUser.getGovernorate().equals("غزة") && fisher1.getGovernorate().equals("غزة")) {

                                        if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher2.getGovernorate().equals("شمال غزة") ||
                                                Common.currentUser.getGovernorate().equals("غرب غزة") && fisher2.getGovernorate().equals("غرب غزة") ||
                                                Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher2.getGovernorate().equals("جنوب غزة") ||
                                                Common.currentUser.getGovernorate().equals("غزة") && fisher2.getGovernorate().equals("غزة")) {

                                            InsideSea insideSea = new InsideSea(Common.currentUser.getName(), Common.currentUser.getGovernorate(),
                                                    Common.currentUser.getRank(), ship.getPlateNumber(), ship.getId(), ship.getOwner(), ship.getGovernorate(), fishers, strDate, strTime);

                                            InsideSea_table.child(ship.getPlateNumber()).setValue(insideSea);

                                        } else {
                                            Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher2.getId(), Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher1.getId(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;

                    case 3:
                        edtIdThree.setVisibility(View.VISIBLE);
                        fishers = new ArrayList<>();

                        if (edtIdOne.getText().toString().isEmpty() || edtIdTwo.getText().toString().isEmpty() || edtIdThree.getText().toString().isEmpty()) {
                            Toast.makeText(getBaseContext(), "الرجاء ادخال هويات الصيادين !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (edtIdOne.getText().toString().equals(edtIdTwo.getText().toString()) || edtIdOne.getText().toString().equals(edtIdThree.getText().toString()) || edtIdTwo.getText().toString().equals(edtIdThree.getText().toString())) {
                            Toast.makeText(getBaseContext(), "الرجاء عدم تكرار رقم الهوية !", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (final Fisher fisher : fisherArrayList) {
                            if (edtIdOne.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الاولى مسجله دخول في البحر!" + edtIdOne.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdTwo.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الثانيه مسجله دخول في البحر!" + edtIdTwo.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdThree.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الثالثه مسجله دخول في البحر!" + edtIdThree.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Fisher_table.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(!dataSnapshot.child(edtIdOne.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الاول غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdTwo.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الثاني غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdThree.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الثالث غير مسجل !", Toast.LENGTH_LONG).show();
                                }

                                if (dataSnapshot.child(edtIdOne.getText().toString()).exists()) {
                                    fisher1 = dataSnapshot.child(edtIdOne.getText().toString()).getValue(Fisher.class);
                                    fisher1.setId(edtIdOne.getText().toString());
                                    fishers.add(fisher1);
                                }
                                if (dataSnapshot.child(edtIdTwo.getText().toString()).exists()) {
                                    fisher2 = dataSnapshot.child(edtIdTwo.getText().toString()).getValue(Fisher.class);
                                    fisher2.setId(edtIdTwo.getText().toString());
                                    fishers.add(fisher2);
                                }
                                if (dataSnapshot.child(edtIdThree.getText().toString()).exists()) {
                                    fisher3 = dataSnapshot.child(edtIdThree.getText().toString()).getValue(Fisher.class);
                                    fisher3.setId(edtIdThree.getText().toString());
                                    fishers.add(fisher3);

                                    Calendar calendar = Calendar.getInstance();

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat sdd = new SimpleDateFormat("h:mm a");
                                    String strDate = sdf.format(calendar.getTime());
                                    String strTime = sdd.format(calendar.getTime());

                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher1.getGovernorate().equals("شمال غزة") ||
                                            Common.currentUser.getGovernorate().equals("غرب غزة") && fisher1.getGovernorate().equals("غرب غزة") ||
                                            Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher1.getGovernorate().equals("جنوب غزة") ||
                                            Common.currentUser.getGovernorate().equals("غزة") && fisher1.getGovernorate().equals("غزة")) {

                                        if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher2.getGovernorate().equals("شمال غزة") ||
                                                Common.currentUser.getGovernorate().equals("غرب غزة") && fisher2.getGovernorate().equals("غرب غزة") ||
                                                Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher2.getGovernorate().equals("جنوب غزة") ||
                                                Common.currentUser.getGovernorate().equals("غزة") && fisher2.getGovernorate().equals("غزة")) {

                                            if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher3.getGovernorate().equals("شمال غزة") ||
                                                    Common.currentUser.getGovernorate().equals("غرب غزة") && fisher3.getGovernorate().equals("غرب غزة") ||
                                                    Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher3.getGovernorate().equals("جنوب غزة") ||
                                                    Common.currentUser.getGovernorate().equals("غزة") && fisher3.getGovernorate().equals("غزة")) {


                                                InsideSea insideSea = new InsideSea(Common.currentUser.getName(), Common.currentUser.getGovernorate(),
                                                        Common.currentUser.getRank(), ship.getPlateNumber(), ship.getId(), ship.getOwner(), ship.getGovernorate(), fishers, strDate, strTime);

                                                InsideSea_table.child(ship.getPlateNumber()).setValue(insideSea);

                                            } else {
                                                Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher3.getId(), Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher2.getId(), Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher1.getId(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;
                    case 4:
                        edtIdThree.setVisibility(View.VISIBLE);
                        edtIdFour.setVisibility(View.VISIBLE);
                        fishers = new ArrayList<>();

                        if (edtIdOne.getText().toString().isEmpty() || edtIdTwo.getText().toString().isEmpty() || edtIdThree.getText().toString().isEmpty() || edtIdFour.getText().toString().isEmpty()) {
                            Toast.makeText(getBaseContext(), "الرجاء ادخال هويات الصيادين !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (edtIdOne.getText().toString().equals(edtIdTwo.getText().toString())
                                || edtIdOne.getText().toString().equals(edtIdThree.getText().toString())
                                || edtIdOne.getText().toString().equals(edtIdFour.getText().toString())
                                || edtIdTwo.getText().toString().equals(edtIdThree.getText().toString())
                                || edtIdTwo.getText().toString().equals(edtIdFour.getText().toString())
                                || edtIdThree.getText().toString().equals(edtIdFour.getText().toString())) {

                            Toast.makeText(getBaseContext(), "الرجاء عدم تكرار رقم الهوية !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (final Fisher fisher : fisherArrayList) {
                            if (edtIdOne.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الاولى مسجله دخول في البحر!" + edtIdOne.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdTwo.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الثانيه مسجله دخول في البحر!" + edtIdTwo.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdThree.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الثالثه مسجله دخول في البحر!" + edtIdThree.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdFour.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الرابعه مسجله دخول في البحر!" + edtIdFour.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Fisher_table.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(!dataSnapshot.child(edtIdOne.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الاول غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdTwo.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الثاني غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdThree.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الثالث غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdFour.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الرابع غير مسجل !", Toast.LENGTH_LONG).show();
                                }

                                if (dataSnapshot.child(edtIdOne.getText().toString()).exists()) {
                                    fisher1 = dataSnapshot.child(edtIdOne.getText().toString()).getValue(Fisher.class);
                                    fisher1.setId(edtIdOne.getText().toString());
                                    fishers.add(fisher1);
                                }
                                if (dataSnapshot.child(edtIdTwo.getText().toString()).exists()) {
                                    fisher2 = dataSnapshot.child(edtIdTwo.getText().toString()).getValue(Fisher.class);
                                    fisher2.setId(edtIdTwo.getText().toString());
                                    fishers.add(fisher2);
                                }
                                if (dataSnapshot.child(edtIdThree.getText().toString()).exists()) {
                                    fisher3 = dataSnapshot.child(edtIdThree.getText().toString()).getValue(Fisher.class);
                                    fisher3.setId(edtIdThree.getText().toString());
                                    fishers.add(fisher3);
                                }
                                if (dataSnapshot.child(edtIdFour.getText().toString()).exists()) {
                                    fisher4 = dataSnapshot.child(edtIdFour.getText().toString()).getValue(Fisher.class);
                                    fisher4.setId(edtIdFour.getText().toString());
                                    fishers.add(fisher4);

                                    Calendar calendar = Calendar.getInstance();

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat sdd = new SimpleDateFormat("h:mm a");
                                    String strDate = sdf.format(calendar.getTime());
                                    String strTime = sdd.format(calendar.getTime());

                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher1.getGovernorate().equals("شمال غزة") ||
                                            Common.currentUser.getGovernorate().equals("غرب غزة") && fisher1.getGovernorate().equals("غرب غزة") ||
                                            Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher1.getGovernorate().equals("جنوب غزة") ||
                                            Common.currentUser.getGovernorate().equals("غزة") && fisher1.getGovernorate().equals("غزة")) {

                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher2.getGovernorate().equals("شمال غزة") ||
                                             Common.currentUser.getGovernorate().equals("غرب غزة") && fisher2.getGovernorate().equals("غرب غزة") ||
                                             Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher2.getGovernorate().equals("جنوب غزة") ||
                                             Common.currentUser.getGovernorate().equals("غزة") && fisher2.getGovernorate().equals("غزة")) {

                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher3.getGovernorate().equals("شمال غزة") ||
                                              Common.currentUser.getGovernorate().equals("غرب غزة") && fisher3.getGovernorate().equals("غرب غزة") ||
                                              Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher3.getGovernorate().equals("جنوب غزة") ||
                                              Common.currentUser.getGovernorate().equals("غزة") && fisher3.getGovernorate().equals("غزة")) {

                                     if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher4.getGovernorate().equals("شمال غزة") ||
                                               Common.currentUser.getGovernorate().equals("غرب غزة") && fisher4.getGovernorate().equals("غرب غزة") ||
                                               Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher4.getGovernorate().equals("جنوب غزة") ||
                                               Common.currentUser.getGovernorate().equals("غزة") && fisher4.getGovernorate().equals("غزة")) {

                                                    InsideSea insideSea = new InsideSea(Common.currentUser.getName(), Common.currentUser.getGovernorate(),
                                                            Common.currentUser.getRank(), ship.getPlateNumber(), ship.getId(), ship.getOwner(), ship.getGovernorate(), fishers, strDate, strTime);

                                                    InsideSea_table.child(ship.getPlateNumber()).setValue(insideSea);

                                                } else {
                                                    Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher4.getId(), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher3.getId(), Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher2.getId(), Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher1.getId(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        break;
                    case 5:
                        edtIdThree.setVisibility(View.VISIBLE);
                        edtIdFour.setVisibility(View.VISIBLE);
                        edtIdFive.setVisibility(View.VISIBLE);
                        fishers = new ArrayList<>();

                        if (edtIdOne.getText().toString().isEmpty() || edtIdTwo.getText().toString().isEmpty() || edtIdThree.getText().toString().isEmpty()
                                || edtIdFour.getText().toString().isEmpty() || edtIdFive.getText().toString().isEmpty()) {
                            Toast.makeText(getBaseContext(), "الرجاء ادخال هويات الصيادين !", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (edtIdOne.getText().toString().equals(edtIdTwo.getText().toString())
                                || edtIdOne.getText().toString().equals(edtIdThree.getText().toString())
                                || edtIdOne.getText().toString().equals(edtIdFour.getText().toString())
                                || edtIdOne.getText().toString().equals(edtIdFive.getText().toString())
                                || edtIdTwo.getText().toString().equals(edtIdThree.getText().toString())
                                || edtIdTwo.getText().toString().equals(edtIdFour.getText().toString())
                                || edtIdTwo.getText().toString().equals(edtIdFive.getText().toString())
                                || edtIdThree.getText().toString().equals(edtIdFour.getText().toString())
                                || edtIdFour.getText().toString().equals(edtIdFive.getText().toString())) {

                            Toast.makeText(getBaseContext(), "الرجاء عدم تكرار رقم الهوية !", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (final Fisher fisher : fisherArrayList) {
                            if (edtIdOne.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الاولى مسجله دخول في البحر!" + edtIdOne.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdTwo.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الثانيه مسجله دخول في البحر!" + edtIdTwo.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdThree.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الثالثه مسجله دخول في البحر!" + edtIdThree.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdFour.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الرابعه مسجله دخول في البحر!" + edtIdFour.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            } else if (edtIdFive.getText().toString().equals(fisher.getId())) {
                                Toast.makeText(ActivityOne.this, "رقم الهويه المدخلة الخامسه مسجله دخول في البحر!" + edtIdFive.getText().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Fisher_table.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.child(edtIdOne.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الاول غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdTwo.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الثاني غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdThree.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الثالث غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdFour.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الرابع غير مسجل !", Toast.LENGTH_LONG).show();
                                }
                                if(!dataSnapshot.child(edtIdFive.getText().toString()).exists()) {
                                    Toast.makeText(ActivityOne.this, "رقم هوية الصياد الخامس غير مسجل !", Toast.LENGTH_LONG).show();
                                }

                                if (dataSnapshot.child(edtIdOne.getText().toString()).exists()) {
                                    fisher1 = dataSnapshot.child(edtIdOne.getText().toString()).getValue(Fisher.class);
                                    fisher1.setId(edtIdOne.getText().toString());
                                    fishers.add(fisher1);
                                }
                                if (dataSnapshot.child(edtIdTwo.getText().toString()).exists()) {
                                    fisher2 = dataSnapshot.child(edtIdTwo.getText().toString()).getValue(Fisher.class);
                                    fisher2.setId(edtIdTwo.getText().toString());
                                    fishers.add(fisher2);
                                }
                                if (dataSnapshot.child(edtIdThree.getText().toString()).exists()) {
                                    fisher3 = dataSnapshot.child(edtIdThree.getText().toString()).getValue(Fisher.class);
                                    fisher3.setId(edtIdThree.getText().toString());
                                    fishers.add(fisher3);
                                }
                                if (dataSnapshot.child(edtIdFour.getText().toString()).exists()) {
                                    fisher4 = dataSnapshot.child(edtIdFour.getText().toString()).getValue(Fisher.class);
                                    fisher4.setId(edtIdFour.getText().toString());
                                    fishers.add(fisher4);
                                }
                                if (dataSnapshot.child(edtIdFive.getText().toString()).exists()) {
                                    fisher5 = dataSnapshot.child(edtIdFour.getText().toString()).getValue(Fisher.class);
                                    fisher5.setId(edtIdFive.getText().toString());
                                    fishers.add(fisher5);


                                    Calendar calendar = Calendar.getInstance();

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat sdd = new SimpleDateFormat("h:mm a");
                                    String strDate = sdf.format(calendar.getTime());
                                    String strTime = sdd.format(calendar.getTime());

                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher1.getGovernorate().equals("شمال غزة") ||
                                            Common.currentUser.getGovernorate().equals("غرب غزة") && fisher1.getGovernorate().equals("غرب غزة") ||
                                            Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher1.getGovernorate().equals("جنوب غزة") ||
                                            Common.currentUser.getGovernorate().equals("غزة") && fisher1.getGovernorate().equals("غزة")) {

                                        if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher2.getGovernorate().equals("شمال غزة") ||
                                                Common.currentUser.getGovernorate().equals("غرب غزة") && fisher2.getGovernorate().equals("غرب غزة") ||
                                                Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher2.getGovernorate().equals("جنوب غزة") ||
                                                Common.currentUser.getGovernorate().equals("غزة") && fisher2.getGovernorate().equals("غزة")) {

                                            if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher3.getGovernorate().equals("شمال غزة") ||
                                                    Common.currentUser.getGovernorate().equals("غرب غزة") && fisher3.getGovernorate().equals("غرب غزة") ||
                                                    Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher3.getGovernorate().equals("جنوب غزة") ||
                                                    Common.currentUser.getGovernorate().equals("غزة") && fisher3.getGovernorate().equals("غزة")) {

                                                if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher4.getGovernorate().equals("شمال غزة") ||
                                                        Common.currentUser.getGovernorate().equals("غرب غزة") && fisher4.getGovernorate().equals("غرب غزة") ||
                                                        Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher4.getGovernorate().equals("جنوب غزة") ||
                                                        Common.currentUser.getGovernorate().equals("غزة") && fisher4.getGovernorate().equals("غزة")) {

                                                    if (Common.currentUser.getGovernorate().equals("شمال غزة") && fisher5.getGovernorate().equals("شمال غزة") ||
                                                            Common.currentUser.getGovernorate().equals("غرب غزة") && fisher5.getGovernorate().equals("غرب غزة") ||
                                                            Common.currentUser.getGovernorate().equals("جنوب غزة") && fisher5.getGovernorate().equals("جنوب غزة") ||
                                                            Common.currentUser.getGovernorate().equals("غزة") && fisher5.getGovernorate().equals("غزة")) {


                                                        InsideSea insideSea = new InsideSea(Common.currentUser.getName(), Common.currentUser.getGovernorate(),
                                                                Common.currentUser.getRank(), ship.getPlateNumber(), ship.getId(), ship.getOwner(), ship.getGovernorate(), fishers, strDate, strTime);

                                                        InsideSea_table.child(ship.getPlateNumber()).setValue(insideSea);

                                                    } else {
                                                        Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher5.getId(), Toast.LENGTH_SHORT).show();

                                                    }
                                                } else {
                                                    Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher4.getId(), Toast.LENGTH_SHORT).show();

                                                }
                                            } else {
                                                Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher3.getId(), Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher2.getId(), Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(ActivityOne.this, "الصياد خارج محافظتك " + fisher1.getId(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                }
                ActivityOne.this.finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOne.this.finish();
            }
        });

        builder.show();
    }

    @Override
    protected void onStop() {
        ActivityOne.this.finish();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ArrayList<InsideSea> seaArrayList = new ArrayList<>();
        InsideSea_table.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    seaArrayList.add(snapshot.getValue(InsideSea.class));
                }
                fisherArrayList.clear();

                for(int i=0; i<seaArrayList.size();i++){
                    fisherArrayList.addAll(seaArrayList.get(i).getFishers());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
