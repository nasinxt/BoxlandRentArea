package com.example.nasin.boxlandrentarea.ActivityClass.Login;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.Register.Register;
import com.example.nasin.boxlandrentarea.ActivityClass.adminHome;
import com.example.nasin.boxlandrentarea.ActivityClass.home;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.example.nasin.boxlandrentarea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    DatabaseReference databaseReference;


    private TextView reg ;
    private Button login;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private ProgressDialog progressDialog;

    private String usernameE;
    private String passwordE;
    GlobalClass globaldata;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();


        globaldata = (GlobalClass) getApplicationContext();

        progressDialog = new ProgressDialog(this);

        reg = (TextView) findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                finish();
            }
        });



        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globaldata.getTenantArray().clear();
                progress = ProgressDialog.show(Login.this, "",
                        "กำลังโหลด", true);

                usernameEditText = findViewById(R.id.username);
                usernameE = usernameEditText.getText().toString();

                passwordEditText = findViewById(R.id.password);
                passwordE = passwordEditText.getText().toString();



                if (TextUtils.isEmpty(usernameE)) {
                    progress.dismiss();

                    Toast.makeText(getApplicationContext(), "กรุณากรอกชื่อผู้ใช้", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordE)) {
                    progress.dismiss();

                    Toast.makeText(getApplicationContext(), "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Tenant login = new Tenant();
                login.setUsername(usernameE.toLowerCase());
                login.setPassword(passwordE);

                final ArrayList<Tenant> tenantArray = new LoginController().getLogin(login);



                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            globaldata.getTenantArray().addAll(tenantArray);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "เชื่อมต่อล้มเหลวโปรดลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                        }

                        if( globaldata.getTenantArray().size()<1){
                            Toast.makeText(Login.this, "โปรดลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                            globaldata.getTenantArray().clear();
                            progress.dismiss();
                        }else {
                            if(globaldata.getTenantArray().get(0).getUsername().equals(login.getUsername()) && globaldata.getTenantArray().get(0).getPassword().equals(passwordE)){
                                if(globaldata.getTenantArray().get(0).getUsername().equals("admin")){
                                    startActivity(new Intent(Login.this,adminHome.class));
                                    progress.dismiss();

                                }else {
                                    startActivity(new Intent(Login.this,home.class));
                                    progress.dismiss();

                                }

                            }else {
                                progress.dismiss();
                                Toast.makeText(Login.this, "ชื่อผู้ใช้เเละรหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show();
                            }

                        }

                        // progressDialog.dismiss();
                    }
                },2500);




            }
        });




    }













}
