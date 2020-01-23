package com.example.eyad.ministryofinteriorproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.eyad.ministryofinteriorproject.Common.Common;
import com.example.eyad.ministryofinteriorproject.Model.User;
import com.example.osama.ministryofinteriorproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddUserActivity extends AppCompatActivity {

    EditText edtName,edtUserNum,edtId,edtFinancialNumber,edtPasswordNum;
    Spinner spinnerGovernorate,spinnerRank,spinnerState;
    FButton btnOk,btnExit;

    String flag;

    User newUser;
    DatabaseReference user_table;

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
        setContentView(R.layout.activity_add_user);

        getSupportActionBar().setTitle("منظمة شؤون الصيادين - إضافة موظف");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtName = findViewById(R.id.edtName);
        edtId = findViewById(R.id.edtId);
        edtFinancialNumber = findViewById(R.id.edtFinancialNumber);
        edtUserNum = findViewById(R.id.edtUserNum);
        edtPasswordNum = findViewById(R.id.edtPasswordNum);
        spinnerGovernorate = findViewById(R.id.spinnerGovernorate);
        spinnerRank = findViewById(R.id.spinnerRank);
        spinnerState = findViewById(R.id.spinnerState);

        btnOk = findViewById(R.id.btnOk);
        btnExit = findViewById(R.id.btnExit);

        user_table = FirebaseDatabase.getInstance().getReference("User");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtName.getText().toString().isEmpty() || edtId.getText().toString().equals("") || edtFinancialNumber.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "الرجاء التأكد من تعبئة البيانات", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    final ProgressDialog dialog = new ProgressDialog(AddUserActivity.this);
                    dialog.setMessage("الرجاء الانتظار ...");
                    dialog.show();

                    if (Common.isConnectedToInternet(getBaseContext())) {

                        if(Common.currentUser.getState().equals("true")){

                        user_table.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(edtUserNum.getText().toString()).exists()) {
                                    dialog.dismiss();
                                    Toast.makeText(AddUserActivity.this, "يوجد رقم مستخدم بهذا الرقم", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    if(spinnerState.getSelectedItem().toString().equals("فعال")){
                                        flag = "true";
                                    }else {
                                        flag = "false";
                                    }

                                    newUser = new User();
                                    newUser.setId(edtId.getText().toString());
                                    newUser.setPassword(edtPasswordNum.getText().toString());
                                    newUser.setName(edtName.getText().toString());
                                    newUser.setFinancialNumber(edtFinancialNumber.getText().toString());
                                    newUser.setGovernorate(spinnerGovernorate.getSelectedItem().toString());
                                    newUser.setRank(spinnerRank.getSelectedItem().toString());
                                    newUser.setState(flag);
                                    user_table.child(edtUserNum.getText().toString()).setValue(newUser);

                                    Toast.makeText(AddUserActivity.this, "تم اضافة شفت بإسم :" + newUser.getName(), Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        }else{
                            dialog.dismiss();
                            Toast.makeText(AddUserActivity.this, "ليس لديك الصلاحية !", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        dialog.dismiss();
                        Toast.makeText(AddUserActivity.this, "الرجاء تأكد بأتصال الانترنت", Toast.LENGTH_SHORT).show();
                    }
                
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
