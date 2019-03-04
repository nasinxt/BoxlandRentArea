package com.example.nasin.boxlandrentarea.ActivityClass.EditAreaDetail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.BookingArea.BookingArea;
import com.example.nasin.boxlandrentarea.ActivityClass.EditProfile.EditProfile;
import com.example.nasin.boxlandrentarea.ActivityClass.ListRentArea.ListRentArea;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewAreaDetail.ViewAreaDetail;
import com.example.nasin.boxlandrentarea.ActivityClass.adminHome;
import com.example.nasin.boxlandrentarea.ActivityClass.home;
import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditAreaDetail extends AppCompatActivity {
    TextView EditAreaPic;
    EditText areaIdEdit,areaDepositEdit,areaPriceEdit,areaSizeEdit,areaDetailEdit;
    Spinner areaTypeSpinner,areaStatusSpinner;
    GlobalClass globaldata;
    Button cancleEditAreaBtn,saveAreaBtn;
    ArrayList<AreaZone> areaZone = new ArrayList<AreaZone>();
    ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_area_detail);
        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        globaldata = (GlobalClass) getApplicationContext();


        EditAreaPic = findViewById(R.id.EditAreaPic);


        areaIdEdit = findViewById(R.id.areaIdEdit);
        areaDepositEdit = findViewById(R.id.areaDepositEdit);
        areaPriceEdit = findViewById(R.id.areaPriceEdit);
        areaSizeEdit = findViewById(R.id.areaSizeEdit);
        areaDetailEdit = findViewById(R.id.areaDetailEdit);

        areaTypeSpinner = findViewById(R.id.areaTypeSpinner);
        areaStatusSpinner = findViewById(R.id.areaStatusSpinner);

        cancleEditAreaBtn = findViewById(R.id.cancleEditAreaBtn);
        saveAreaBtn = findViewById(R.id.saveAreaBtn);
        addStoreType();
        addStatus();

        viewRentArea();




    }



    private void addStoreType() {
        globaldata.getAreaZone().clear();
        final ArrayList<String> storetype = new ArrayList<String>();
        areaZone = EditAreaDetailController.getAreaZone();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("Size",""+areaZone.size());


                for (AreaZone areaZone : areaZone){
                    storetype.add(areaZone.getAreaType());
                    Log.e("Size",""+areaZone.getAreaType());

                    globaldata.getAreaZone().add(areaZone);

                }

                ArrayAdapter<String> StringStoreType = new ArrayAdapter<String>(EditAreaDetail.this,
                        android.R.layout.simple_dropdown_item_1line,storetype);
                areaTypeSpinner.setAdapter(StringStoreType);


                progress.dismiss();

            }
        },2500);

    }

    private void addStatus() {
        globaldata.getAreaZone().clear();
        final ArrayList<String> status = new ArrayList<String>();

                status.add("ว่าง");
                status.add("ถูกจองเเล้ว");

                ArrayAdapter<String> StringStoreType = new ArrayAdapter<String>(EditAreaDetail.this,
                        android.R.layout.simple_dropdown_item_1line,status);
                areaStatusSpinner.setAdapter(StringStoreType);


    }


    public void viewRentArea(){

        final AreaDetail areaDetail = new EditAreaDetailController().getAreaDetail(globaldata.getAreadetail().get(0));
        globaldata.setAreaDetailClass(areaDetail);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                globaldata.getAreadetail().clear();
                globaldata.getAreadetail().add(globaldata.getAreaDetailClass());

                areaIdEdit.setText(areaDetail.getAreaId());
                areaSizeEdit.setText(areaDetail.getSize());
                areaDetailEdit.setText(areaDetail.getDetail());
                areaDepositEdit.setText(""+areaDetail.getDeposit());
                areaPriceEdit.setText(""+areaDetail.getPrice());

                EditAreaPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(EditAreaDetail.this,EditPicArea.class));
                    }
                });
                // Cancel
                cancleEditAreaBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(EditAreaDetail.this,ListRentArea.class));
                    }
                });
                // Save
                saveAreaBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress = ProgressDialog.show(EditAreaDetail.this, "", "กำลังโหลด", true);

                        if(areaIdEdit.getText().toString().isEmpty()){
                            Toast.makeText(EditAreaDetail.this, "กรุณากรอกหมายเลขพื้นที่", Toast.LENGTH_SHORT).show();
                            progress.dismiss();

                        }else if(areaSizeEdit.getText().toString().isEmpty()){
                            Toast.makeText(EditAreaDetail.this, "กรุณากรอกขนาด", Toast.LENGTH_SHORT).show();
                            progress.dismiss();

                        } else if (areaDetailEdit.getText().toString().isEmpty()) {
                            Toast.makeText(EditAreaDetail.this, "กรุณากรอกรายละเอียด", Toast.LENGTH_SHORT).show();
                            progress.dismiss();

                        }else if(areaDepositEdit.getText().toString().isEmpty()){
                            progress.dismiss();
                            Toast.makeText(EditAreaDetail.this, "กรุณากรอกราคามัดจำ", Toast.LENGTH_SHORT).show();
                        }
                        else if(areaPriceEdit.getText().toString().isEmpty()) {
                            Toast.makeText(EditAreaDetail.this, "กรุณากรอกราคาเช่า", Toast.LENGTH_SHORT).show();
                            progress.dismiss();

                        }else {
                            final AreaDetail areaDetail1 = new AreaDetail();
                            areaDetail1.setAreaId(areaIdEdit.getText().toString());
                            areaDetail1.setSize(areaSizeEdit.getText().toString());
                            areaDetail1.setDetail(areaDetailEdit.getText().toString());
                            areaDetail1.setDeposit(Double.parseDouble(areaDepositEdit.getText().toString()));
                            areaDetail1.setPrice(Double.parseDouble(areaPriceEdit.getText().toString()));
                            areaDetail1.setStatus(areaStatusSpinner.getSelectedItem().toString());
                            for (AreaZone areaZone : areaZone){
                                Log.e("asdasd",""+areaTypeSpinner.getSelectedItem().toString());
                                if(areaZone.getAreaType().equals(areaTypeSpinner.getSelectedItem().toString())){
                                    areaDetail1.setAreazone(areaZone);
                                    Log.e("asdasd",""+areaDetail1.getAreazone().getAreaType());

                                }

                            }

                            final EditAreaDetailController editAreaDetailController = new EditAreaDetailController();

                       

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(EditAreaDetail.this);
                            builder.setMessage("คุณเเน่ใจว่าจะแก้ไขหรือไม่");
                            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    editAreaDetailController.saveEditArea(areaDetail1);
                                    Toast.makeText(EditAreaDetail.this, "แก้ไขสำเร็จ", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();

                                }
                            });
                            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                            builder.show();


                        }




                    }
                });

                Log.e("Pic-Size",""+areaDetail.getAreaPic().size());

                progress.dismiss();
            }
        },3500);


    }



}
