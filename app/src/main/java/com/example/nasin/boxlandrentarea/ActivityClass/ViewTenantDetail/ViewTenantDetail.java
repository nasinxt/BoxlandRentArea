package com.example.nasin.boxlandrentarea.ActivityClass.ViewTenantDetail;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.EditTenantDetail.EditTenantDetail;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewAreaDetail.ViewAreaDetail;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewAreaDetail.ViewAreaDetailController;
import com.example.nasin.boxlandrentarea.ActivityClass.adminHome;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

public class ViewTenantDetail extends AppCompatActivity {
    DatabaseReference databaseReference;
    DatabaseReference tenantData;
    DatabaseReference storeTypeData;
    TextView nameT, idcardT, addressT, storenameT, storeTypeTenant, phonenumT, genderT, emailTenantD;
    GlobalClass globaldata;
    Button delete, editTenantDetail;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tenant_detail);
        Firebase.setAndroidContext(this);
        globaldata = (GlobalClass) getApplicationContext();

        nameT = findViewById(R.id.accountNameTenant);
        idcardT = findViewById(R.id.idcardTenant);
        addressT = findViewById(R.id.addressTenant);
        storenameT = findViewById(R.id.storenameTenant);
        storeTypeTenant = findViewById(R.id.storeTypeTenant);
        phonenumT = findViewById(R.id.phoneNumberTenant);
        genderT = findViewById(R.id.genderTenant);
        emailTenantD = findViewById(R.id.emailTenantD);


        progress = ProgressDialog.show(this, "",
                "กำลังโหลด", true);

        editTenantDetail = findViewById(R.id.editTenantBtn);
        editTenantDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ViewTenantDetail.this, "เเก้ไขข้อมูล", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ViewTenantDetail.this,EditTenantDetail.class));
                finish();

            }
        });
        delete = findViewById(R.id.deleteUserBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ViewTenantDetail.this);
                builder.setMessage("ยืนยันการลบพื้นที่");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(ViewTenantDetail.this, "ลบข้อมูล", Toast.LENGTH_SHORT).show();
                        ViewTenantDetailController viewTenantCon = new ViewTenantDetailController();
                        viewTenantCon.removeTenant(globaldata.getTenantArray().get(0));

                        startActivity(new Intent(ViewTenantDetail.this, adminHome.class));
                        finish();

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


    }

    @Override
    public void onResume() {
        super.onResume();
        FindProfileData();

    }

    public void FindProfileData() {
        globaldata = (GlobalClass) getApplicationContext();

        ViewTenantDetailController viewTenant = new ViewTenantDetailController();

        final Tenant tenant = viewTenant.findTenant(globaldata.getTenant());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                globaldata.getTenantArray().add(tenant);
                Log.e("Size", "" + globaldata.getTenantArray().size());

                nameT.setText(tenant.getFname() + " " + tenant.getLname());
                idcardT.setText(tenant.getIdcardnum());
                addressT.setText(tenant.getAddress());
                storenameT.setText(tenant.getStorename());
                phonenumT.setText(tenant.getPhoneNum());
                genderT.setText(tenant.getGender());
                storeTypeTenant.setText(tenant.getStoreDetail());
                emailTenantD.setText(tenant.getEmail());

                progress.dismiss();
            }
        }, 2500);


    }
}
