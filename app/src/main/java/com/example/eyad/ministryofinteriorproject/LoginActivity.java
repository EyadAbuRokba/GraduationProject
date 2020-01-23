package com.example.eyad.ministryofinteriorproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText user_num,user_pass;
    FirebaseDatabase database;
    DatabaseReference table_user;

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
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        user_num = findViewById(R.id.user_name);
        user_pass = findViewById(R.id.user_pass);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isConnectedToInternet(getBaseContext())){

                    if(user_num.getText().toString().isEmpty() && user_pass.getText().toString().isEmpty()){
                        //dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "خطأ في تسجيل الدخول", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setMessage("الرجاء الانتظار ....");
                    dialog.setCancelable(false);
                    dialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.child(user_num.getText().toString()).exists()){//100

                                User user = dataSnapshot.child(user_num.getText().toString()).getValue(User.class);
                                user.setUser_number(user_num.getText().toString());
                                if(user.getPassword().equals(user_pass.getText().toString())) {
                                    dialog.dismiss();
                                    Common.currentUser = user;
                                    Intent mainActivityIntent = new Intent(getApplicationContext(),HomeScreenActivity.class);
                                    startActivity(mainActivityIntent);
                                    LoginActivity.this.finish();
                                }else {
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "خطأ في تسجيل الدخول ,الرجاء التأكد من كلمة السر", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "خطأ في تسجيل الدخول , الرجاء تأكد من اسم المستخدم", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });


    }
}
