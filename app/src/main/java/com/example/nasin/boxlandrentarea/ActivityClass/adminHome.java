package com.example.nasin.boxlandrentarea.ActivityClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.nasin.boxlandrentarea.ActivityClass.AddArea.AddRentArea;
import com.example.nasin.boxlandrentarea.ActivityClass.ListNewPayment.ListNewPayment;
import com.example.nasin.boxlandrentarea.ActivityClass.ListRentArea.ListRentArea;
import com.example.nasin.boxlandrentarea.ActivityClass.ListTenant.ListTenant;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;

public class adminHome extends AppCompatActivity {

    GlobalClass globaldata;
    LinearLayout addAreaBtn,list_area,list_TenantBtn,list_paymentBtn,logout_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        globaldata = (GlobalClass) getApplicationContext();


        addAreaBtn = findViewById(R.id.addAreaBtn);
        addAreaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminHome.this,AddRentArea.class));
            }
        });

        list_area = findViewById(R.id.list_area);
        list_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminHome.this,ListRentArea.class));

            }
        });

        list_TenantBtn = findViewById(R.id.list_TenantBtn);
        list_TenantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminHome.this,ListTenant.class));
            }
        });

        list_paymentBtn = findViewById(R.id.list_paymentBtn);
        list_paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(adminHome.this,ListNewPayment.class));

            }
        });
        logout_Btn = findViewById(R.id.logout_Btn);
        logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globaldata.getTenantArray().clear();
                startActivity(new Intent(adminHome.this,home.class));

            }
        });


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

}

