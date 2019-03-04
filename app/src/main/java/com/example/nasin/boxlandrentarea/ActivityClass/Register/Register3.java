package com.example.nasin.boxlandrentarea.ActivityClass.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.home;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class Register3 extends AppCompatActivity {

        Button registerButton;
        EditText username;
        EditText password;
        EditText passwordConfirm;

        GlobalClass globaldata;
        String Stringusername;
        String Stringpassword;
        String StringpasswordConfirm;

        ProgressBar progressBar;
        ProgressDialog progress;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register3);
            Firebase.setAndroidContext(this);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);



            password = (EditText) findViewById(R.id.password);
            passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);

            username = (EditText) findViewById(R.id.username);


            registerButton = (Button)findViewById(R.id.registerBTN);






            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progress = ProgressDialog.show(Register3.this, "", "กำลังโหลด", true);

                    Stringusername = username.getText().toString();
                    Stringpassword = password.getText().toString();
                    StringpasswordConfirm = passwordConfirm.getText().toString();
                    Log.e("GET",""+Stringusername+" "+Stringpassword+" "+StringpasswordConfirm);

                    if (TextUtils.isEmpty(Stringusername)) {
                        Toast.makeText(getApplicationContext(), "กรุณากรอกชื่อผู้ใช้", Toast.LENGTH_SHORT).show();
                        progress.dismiss();

                    } else if (TextUtils.isEmpty(Stringpassword)) {
                        Toast.makeText(getApplicationContext(), "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
                        progress.dismiss();

                    }else if (Stringpassword.length() < 6) {
                        Toast.makeText(getApplicationContext(), "รหัสผ่านสั้นเกินไป", Toast.LENGTH_SHORT).show();
                        progress.dismiss();

                    }else if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "รหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show();
                        progress.dismiss();

                    }
                    else {

                        Registered();



                    }
                }
            });



        }

        public void Registered() {

            username = (EditText) findViewById(R.id.username);
            password = (EditText) findViewById(R.id.password);
            Stringusername = username.getText().toString();
            Stringpassword = password.getText().toString();
            globaldata = (GlobalClass) getApplicationContext();

            globaldata.getTenant().setUsername(Stringusername);
            globaldata.getTenant().setPassword(Stringpassword);
            final RegisterController registerCon = new RegisterController();
            final ArrayList<String> count = registerCon.countTenant(globaldata.getTenant());


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("Key",""+ count.size());

                    if(count.size() == 0){
                        registerCon.addTenant(globaldata.getTenant());

                        progress.dismiss();
                        Toast.makeText(Register3.this, "สมัครสมาชิกสำเร็จ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Register3.this, home.class);
                        startActivity(intent);
                        finish();



                        progressBar.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(Register3.this, "ชื่อผู้ใช้ถูกใช้งานเเล้ว", Toast.LENGTH_SHORT).show();
                        progress.dismiss();


                    }

                    // progressDialog.dismiss();
                }
            },2500);






        }


        @Override
        protected void onResume() {
            super.onResume();
            progressBar.setVisibility(View.GONE);
        }

    }

