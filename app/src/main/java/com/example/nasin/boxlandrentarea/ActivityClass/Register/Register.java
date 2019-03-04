package com.example.nasin.boxlandrentarea.ActivityClass.Register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private Spinner spingender;

    private ArrayList<String> gender = new ArrayList<String>();
    EditText idcard;
    EditText name;
    EditText phoneNum;
    EditText address;
    EditText username;
    EditText password;
    EditText email;
    Button registerButton;
    FirebaseAuth firebaseAuth;

    Button next;
    GlobalClass globaldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        globaldata = (GlobalClass) getApplicationContext();


        Firebase.setAndroidContext(this);

        idcard = (EditText) findViewById(R.id.idcard);
        name = (EditText) findViewById(R.id.nameTenant);
        address = (EditText) findViewById(R.id.address);
        spingender = (Spinner) findViewById(R.id.gender);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        email = findViewById(R.id.email);


        spingender = (Spinner) findViewById(R.id.gender);
        addgender();

        ArrayAdapter<String> Stringgender = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, gender);
        spingender.setAdapter(Stringgender);





        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInput = email.getText().toString().trim();
                String nameString = name.getText().toString().trim();

                if(!idcard.getText().toString().matches("([0-9]{13})")){
                    Toast.makeText(Register.this, "โปรดใส่เลขบัตรประชาชนให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    Toast.makeText(Register.this, "โปรดใส่อีเมลให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }else if(Pattern.compile("^" +"(?=.*[A-Z][ก-ฮ])"+"(?=.* [ \t] )" + "(?=.*[A-Z][ก-ฮ])"+ "$").matcher(nameString).matches()){
                    Toast.makeText(Register.this, "โปรดใส่ชื่อและนามสกุลให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
                else if(!phoneNum.getText().toString().matches("([0]{1})"+"([6,8,9]{1})"+"[0-9]{8}")) {
                    Toast.makeText(Register.this, "โปรดเบอร์โทรศัพท์ให้ถูกต้อง", Toast.LENGTH_SHORT).show();

                } else if(address.getText().toString().isEmpty()) {
                    Toast.makeText(Register.this, "กรุณากรอกที่อยู่", Toast.LENGTH_SHORT).show();
                }else {
                    Registered();

                }


            }
        });



    }


    public void Registered() {
        EditText email = findViewById(R.id.email);
        String Stringidcard = idcard.getText().toString();
        String Stringaddress= address.getText().toString();
        String Stringspingender = spingender.getSelectedItem().toString();
        String StringsphoneNum = phoneNum.getText().toString();
        String StringEmail = email.getText().toString();


        String Stringname = name.getText().toString();
        String[] FLname = Stringname.split(" ");

        if(FLname.length==2){

            globaldata.getTenant().setIdcardnum(Stringidcard);
            globaldata.getTenant().setFname(FLname[0]);
            globaldata.getTenant().setLname(FLname[1]);
            globaldata.getTenant().setGender(Stringspingender);
            globaldata.getTenant().setAddress(Stringaddress);
            globaldata.getTenant().setPhoneNum(StringsphoneNum);
            globaldata.getTenant().setEmail(StringEmail);

            Intent intent = new Intent(Register.this,Register2.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(Register.this, "ชื่อนามสกุลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();

        }




    }


    private void addgender() {
        gender.add("ชาย");
        gender.add("หญิง");
    }

}