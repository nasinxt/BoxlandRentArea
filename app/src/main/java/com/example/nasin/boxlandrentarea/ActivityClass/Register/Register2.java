package com.example.nasin.boxlandrentarea.ActivityClass.Register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Register2 extends AppCompatActivity {

    Button next;
    private Spinner spinstoretype;
    private ArrayList<String> storetype = new ArrayList<String>();
    EditText storename,storeDetail;
    GlobalClass globaldata;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Firebase.setAndroidContext(this);

        globaldata = (GlobalClass) getApplicationContext();
        storename = (EditText) findViewById(R.id.storename);
        storeDetail = (EditText) findViewById(R.id.storeDetail);



        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storename.getText().toString().isEmpty()){
                    Toast.makeText(Register2.this, "กรุณากรอกชื่อร้านค้า", Toast.LENGTH_SHORT).show();
                }else {
                    Registered();
                }


            }
        });





    }

    public void Registered() {
        String Stringstorename = storename.getText().toString();
        String StringstoreDetail = storeDetail.getText().toString();

        globaldata.getTenant().setStorename(Stringstorename);
        globaldata.getTenant().setStoreDetail(StringstoreDetail);


        Intent intent = new Intent(Register2.this, Register3.class);
        startActivity(intent);
        finish();

    }







}
