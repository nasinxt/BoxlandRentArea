package com.example.nasin.boxlandrentarea.ActivityClass.EditTenantDetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.EditProfile.EditProfile;
import com.example.nasin.boxlandrentarea.ActivityClass.ListTenant.ListTenant;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditTenantDetail extends AppCompatActivity {

    EditText nameE, idcardE, addressE, storenameE, phonenumE,emailTenantE,storeDetailETenant;
    Spinner spinstoretypeE, spingenderE;
    private ArrayList<String> storetype = new ArrayList<String>();
    private ArrayList<String> gender = new ArrayList<String>();
    GlobalClass globaldata;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tenant_detail);
        Firebase.setAndroidContext(this);
        globaldata = (GlobalClass) getApplicationContext();
        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        nameE = findViewById(R.id.fullnameTenantE);
        idcardE = findViewById(R.id.idcardTenantE);
        addressE = findViewById(R.id.addressTenantE);
        storenameE = findViewById(R.id.storenameTenantE);
        phonenumE = findViewById(R.id.phoneNumberTenantE);
        storeDetailETenant = findViewById(R.id.storeDetailETenant);
        spingenderE = findViewById(R.id.genderTenantE);
        emailTenantE = findViewById(R.id.emailTenantE);

        findProfileData();


        Button saveTenantBtn = findViewById(R.id.saveTenantBtn);
        saveTenantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(idcardE.getText().toString().isEmpty()){
                    Toast.makeText(EditTenantDetail.this, "เลขบัตรประชาชน", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }
                if(storenameE.getText().toString().isEmpty()){
                    Toast.makeText(EditTenantDetail.this, "กรุณากรอกชื่อร้านค้า", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailTenantE.getText().toString()).matches()) {
                    Toast.makeText(EditTenantDetail.this, "โปรดใส่อีเมลให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }else if(Pattern.compile("^" +"(?=.*[A-Z][ก-ฮ])"+"(?=.* [ \t] )" + "(?=.*[A-Z][ก-ฮ])"+ "$").matcher(nameE.getText().toString()).matches()){
                    progress.dismiss();
                    Toast.makeText(EditTenantDetail.this, "โปรดใส่ชื่อและนามสกุลให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
                else if(!phonenumE.getText().toString().matches("([0]{1})"+"([6,8,9]{1})"+"[0-9]{8}")) {
                    Toast.makeText(EditTenantDetail.this, "โปรดเบอร์โทรศัพท์ให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }else if(storeDetailETenant.getText().toString().isEmpty()){
                    Toast.makeText(EditTenantDetail.this, "กรุณากรอกรายละเอียดร้านค้า", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }else if(addressE.getText().toString().isEmpty()) {
                    Toast.makeText(EditTenantDetail.this, "กรุณากรอกที่อยู่", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }else {
                    EditTenantDetail();

                    Toast.makeText(EditTenantDetail.this, "บันทึกสำเร็จ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EditTenantDetail.this, ListTenant.class));
                    finish();

                }


            }
        });

        Button cancleEditTenantBtn = findViewById(R.id.saveTenantBtn);
        cancleEditTenantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public  void  findProfileData(){
        globaldata = (GlobalClass) getApplicationContext();
        EditTenantController editTenantCon = new EditTenantController();
        final Tenant tenant = editTenantCon.findTenant(globaldata.getTenant());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                globaldata.setTenant(tenant);

                nameE.setText(globaldata.getTenant().getFname()+" "+globaldata.getTenant().getLname());
                idcardE.setText(globaldata.getTenant().getIdcardnum());
                addressE.setText(globaldata.getTenant().getAddress());
                storenameE.setText(globaldata.getTenant().getStorename());
                phonenumE.setText(globaldata.getTenant().getPhoneNum());
                emailTenantE.setText(globaldata.getTenant().getEmail());
                storeDetailETenant.setText(globaldata.getTenant().getStoreDetail());




                gender.add("ชาย");
                gender.add("หญิง");

                ArrayAdapter<String> Stringgender = new ArrayAdapter<String>(EditTenantDetail.this,
                        android.R.layout.simple_dropdown_item_1line, gender);
                spingenderE.setAdapter(Stringgender);


                progress.dismiss();
            }
        },2000);



    }



    public void EditTenantDetail() {

        final Tenant tenant = new Tenant();

        final String[] name = nameE.getText().toString().split(" ");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tenant.setIdcardnum(idcardE.getText().toString());
                tenant.setFname(name[0]);
                tenant.setLname(name[1]);
                tenant.setGender(spingenderE.getSelectedItem().toString());
                tenant.setStorename(storenameE.getText().toString());
                tenant.setPhoneNum(phonenumE.getText().toString());
                tenant.setAddress(addressE.getText().toString());
                tenant.setEmail(emailTenantE.getText().toString());
                tenant.setStoreDetail(storeDetailETenant.getText().toString());
                tenant.setUsername(globaldata.getTenant().getUsername());

                EditTenantController editProCon = new EditTenantController();
                editProCon.editTenantDetail(tenant);

            }
        }, 1500);


        Toast.makeText(this, "บันทึกสำเร็จ", Toast.LENGTH_LONG).show();



    }





}