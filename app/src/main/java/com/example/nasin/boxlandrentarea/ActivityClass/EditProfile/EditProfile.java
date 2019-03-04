package com.example.nasin.boxlandrentarea.ActivityClass.EditProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.home;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {

    EditText nameE, idcardE, addressE, storenameE, phonenumE,emailE,storeDetailE;
    Spinner  spingenderE;
    FirebaseUser user;
    DatabaseReference databaseReference;
    private ArrayList<String> storetype = new ArrayList<String>();
    private ArrayList<String> gender = new ArrayList<String>();
    GlobalClass globaldata;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Firebase.setAndroidContext(this);
        globaldata = (GlobalClass) getApplicationContext();
        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        nameE = findViewById(R.id.fullnameProfileE);
        idcardE = findViewById(R.id.idcardProfileE);
        addressE = findViewById(R.id.addressProfileE);
        storenameE = findViewById(R.id.storenameProfileE);
        storeDetailE = findViewById(R.id.storeDetailE);
        phonenumE = findViewById(R.id.phoneNumberProfireE);
        spingenderE = findViewById(R.id.genderProfileE);
        emailE = findViewById(R.id.emailProfileE);

        getProfileDetail();


        ImageView logoutBtn = findViewById(R.id.logoutBtnE);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globaldata.getTenantArray().clear();
                finish();
                startActivity(new Intent(EditProfile.this, home.class));
            }
        });

        // Save Edit
        Button saveProfileBtn = findViewById(R.id.saveProfileBtn);
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = ProgressDialog.show(EditProfile.this, "", "กำลังโหลด", true);

                if(idcardE.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "เลขบัตรประชาชน", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }
                if(storenameE.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "กรุณากรอกชื่อร้านค้า", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailE.getText().toString()).matches()) {
                    Toast.makeText(EditProfile.this, "โปรดใส่อีเมลให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }else if(Pattern.compile("^" +"(?=.*[A-Z][ก-ฮ])"+"(?=.* [ \t] )" + "(?=.*[A-Z][ก-ฮ])"+ "$").matcher(nameE.getText().toString()).matches()){
                    progress.dismiss();
                    Toast.makeText(EditProfile.this, "โปรดใส่ชื่อและนามสกุลให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
                else if(!phonenumE.getText().toString().matches("([0]{1})"+"([6,8,9]{1})"+"[0-9]{8}")) {
                    Toast.makeText(EditProfile.this, "โปรดเบอร์โทรศัพท์ให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }else if(storeDetailE.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "กรุณากรอกรายละเอียดร้านค้า", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }else if(addressE.getText().toString().isEmpty()) {
                    Toast.makeText(EditProfile.this, "กรุณากรอกที่อยู่", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }else {
                    EditProfile();

                }


            }
        });

        // Cancel Edit
        Button cancel = findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this, home.class));
                finish();
            }
        });


    }


    public void getProfileDetail() {
        globaldata = (GlobalClass) getApplicationContext();
        EditProfileController editProfileCon = new EditProfileController();
        final Tenant tenant = editProfileCon.getProfileDetail(globaldata.getTenantArray().get(0));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                globaldata.setTenant(tenant);

                nameE.setText(globaldata.getTenant().getFname() + " " + globaldata.getTenant().getLname());
                idcardE.setText(globaldata.getTenant().getIdcardnum());
                addressE.setText(globaldata.getTenant().getAddress());
                storenameE.setText(globaldata.getTenant().getStorename());
                phonenumE.setText(globaldata.getTenant().getPhoneNum());
                emailE.setText(globaldata.getTenant().getEmail());
                storeDetailE.setText(globaldata.getTenant().getStoreDetail());




                gender.add("ชาย");
                gender.add("หญิง");

                ArrayAdapter<String> Stringgender = new ArrayAdapter<String>(EditProfile.this,
                        android.R.layout.simple_dropdown_item_1line, gender);
                spingenderE.setAdapter(Stringgender);


                progress.dismiss();
            }
        }, 1500);


    }


    public void EditProfile() {

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
                tenant.setEmail(emailE.getText().toString());
                tenant.setStoreDetail(storeDetailE.getText().toString());
                tenant.setUsername(globaldata.getTenantArray().get(0).getUsername());

                EditProfileController editProCon = new EditProfileController();
                editProCon.editProfile(tenant);


            }
        }, 1500);

        progress.dismiss();

        Toast.makeText(this, "บันทึกสำเร็จ", Toast.LENGTH_LONG).show();

        startActivity(new Intent(EditProfile.this, home.class));
        finish();


    }
}