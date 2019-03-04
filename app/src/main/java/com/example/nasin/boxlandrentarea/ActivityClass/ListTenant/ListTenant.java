package com.example.nasin.boxlandrentarea.ActivityClass.ListTenant;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.EditTenantDetail.EditTenantDetail;
import com.example.nasin.boxlandrentarea.ActivityClass.ListRentArea.ListRentArea;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewTenantDetail.ViewTenantDetail;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewTenantDetail.ViewTenantDetailController;
import com.example.nasin.boxlandrentarea.ActivityClass.adminHome;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ListTenant extends AppCompatActivity {

    EditText search;
    DatabaseReference databaseReference;
    DatabaseReference tenantData;
    DatabaseReference loginData;
    ProgressDialog progress;

    GlobalClass globaldata;
    TableLayout tableLayout;
    Button searchTeBtn;
    EditText searchTenantBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tenant);
        Firebase.setAndroidContext(this);
        globaldata = (GlobalClass) getApplicationContext();

        searchTenantBar =  findViewById(R.id.searchTenantBar);
        searchTeBtn = findViewById(R.id.searchTeBtn);

        searchTeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(ListTenant.this, "", "กำลังโหลด", true);

                if(tableLayout != null && globaldata.getTenantArray().size()>0) {
                    tableLayout.removeAllViews();
                    globaldata.getTenantArray().clear();

                }

                if(!searchTenantBar.getText().toString().isEmpty()){
                    final ArrayList<Tenant> tenantArrayList = new ListTenantController().findTenant(searchTenantBar.getText().toString());

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            globaldata.getTenantArray().clear();
                            globaldata.getTenantArray().addAll(tenantArrayList);
                            findTenant();

                            progress.dismiss();
                        }
                    },2000);



                }else{
                    tableLayout = findViewById(R.id.SearchTenantResult);


                    final ArrayList<Tenant> tenantArrayList = new ListTenantController().findAllTenant();


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            globaldata.getTenantArray().clear();
                            globaldata.getTenantArray().addAll(tenantArrayList);
                            findTenant();

                            progress.dismiss();
                        }
                    },2000);
                }            }
        });





    }





    public void findTenant(){

        for(final Tenant tenant : globaldata.getTenantArray()){
            int i=0;
            tableLayout = findViewById(R.id.SearchTenantResult);
            TableRow tableRow = new TableRow(ListTenant.this);

            // Create Layout
            LinearLayout picResultLinearText = new LinearLayout(ListTenant.this);
            LinearLayout TenantResultLinearText = new LinearLayout(ListTenant.this);
            LinearLayout buttonResultLinearText = new LinearLayout(ListTenant.this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(30, 20, 30, 0);

            LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            buttonLayout.setMargins(30, 10, 30, 10);

            LinearLayout.LayoutParams picParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            picParams.setMargins(30, 20, 30, 5);
            picParams.height = 250;
            picParams.width = 250;


            // Create Opject

            ImageView imageView = new ImageView(ListTenant.this);
            final TextView tenantT = new TextView(ListTenant.this);
            TextView phoneNumT = new TextView(ListTenant.this);

            imageView.setImageResource(R.drawable.student);
            tenantT.setText(tenant.getFname()+" "+tenant.getLname());
            tenantT.setTextColor(getApplication().getResources().getColor(R.color.white));

            phoneNumT.setText(" "+tenant.getPhoneNum());
            phoneNumT.setTextColor(getApplication().getResources().getColor(R.color.white));

            Button editBtn = new Button(ListTenant.this);
            Button removeBtn = new Button(ListTenant.this);



            picResultLinearText.addView(imageView, picParams);
            picResultLinearText.setOrientation(LinearLayout.VERTICAL);

            globaldata.setTenantId(tenant.getFname());

            TenantResultLinearText.addView(tenantT, layoutParams);
            TenantResultLinearText.addView(phoneNumT, layoutParams);
            TenantResultLinearText.setOrientation(LinearLayout.VERTICAL);

            buttonResultLinearText.addView(editBtn, buttonLayout);
            buttonResultLinearText.addView(removeBtn, buttonLayout);
            buttonResultLinearText.setOrientation(LinearLayout.VERTICAL);


            editBtn.setBackgroundResource(R.drawable.buttonstyle);
            editBtn.setText("แก้ไข");
            editBtn.setTextColor(getApplication().getResources().getColor(R.color.white));

            removeBtn.setBackgroundResource(R.drawable.buttonstyle);
            removeBtn.setTextColor(getApplication().getResources().getColor(R.color.white));
            removeBtn.setText("ลบ");



            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(Tenant tenant1 : globaldata.getTenantArray()){
                        if(tenantT.getText().toString().equals(tenant1.getFname()+" "+tenant.getLname())){
                            Log.e("user-TENANT",""+tenant1.getUsername());
                            globaldata.setTenant(tenant1);
                            startActivity(new Intent(ListTenant.this,EditTenantDetail.class));
                            finish();
                        }

                    }
                    globaldata.getTenantArray().clear();



                }
            });

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(ListTenant.this);
                    builder.setMessage("ยืนยันการลบข้อมูลผู้ใช้");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            for(Tenant tenant1 : globaldata.getTenantArray()){
                                if(tenantT.getText().toString().equals(tenant1.getFname()+" "+tenant.getLname())){

                                    Toast.makeText(ListTenant.this, "ลบข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
                                    ListTenantController remove = new ListTenantController();
                                    startActivity(new Intent(ListTenant.this, adminHome.class));
                                    remove.removeTenant(tenant1);


                                }

                            }
                            globaldata.getTenantArray().clear();

                        }
                    });
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();





                }
            });



            tableRow.addView(picResultLinearText);
            tableRow.addView(TenantResultLinearText);
            tableRow.addView(buttonResultLinearText);
            tableLayout.addView(tableRow);


        }



    }


}
