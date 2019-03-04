package com.example.nasin.boxlandrentarea.ActivityClass.ListRentArea;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.BrowseRentArea.BrowseRentArea;
import com.example.nasin.boxlandrentarea.ActivityClass.BrowseRentArea.BrowseRentAreaController;
import com.example.nasin.boxlandrentarea.ActivityClass.EditAreaDetail.EditAreaDetail;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewAreaDetail.ViewAreaDetail;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewAreaDetail.ViewAreaDetailController;
import com.example.nasin.boxlandrentarea.ActivityClass.adminHome;
import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListRentArea extends AppCompatActivity {

    EditText search;
    GlobalClass globaldata;
    TableLayout tableLayout;
    Spinner typeSinner;
    Button searchBtn;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rent_area);
        globaldata = (GlobalClass) getApplicationContext();

        search = (EditText) findViewById(R.id.searchListTenant);
        typeSinner = (Spinner) findViewById(R.id.typeSinnerListTenant);
        searchBtn = (Button) findViewById(R.id.searchBtnListTenant);
        addStoreType();

        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);



        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(ListRentArea.this, "", "กำลังโหลด", true);

                globaldata.getAreadetail().clear();
                tableLayout = findViewById(R.id.SearchResult);
                tableLayout.removeAllViews();

                AreaDetail areaDetail = new AreaDetail();


                for (AreaZone areaZone : globaldata.getAreaZone()) {
                    if (areaZone.getAreaType().equals(typeSinner.getSelectedItem().toString())) {
                        areaDetail.setAreaId(search.getText().toString());
                        areaDetail.setAreazone(areaZone);
                    }

                }

                if (!search.getText().toString().isEmpty()) {

                    ListRentAreaController listRentArea = new ListRentAreaController();
                    areaDetail.setAreaId(search.getText().toString());
                    Log.e("areaDetail setAreaId ",""+areaDetail.getAreaId());

                    final ArrayList<AreaDetail> areaDetailArray = listRentArea.findAreaByName(areaDetail);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            globaldata.getAreadetail().clear();
                            tableLayout = findViewById(R.id.SearchResult);
                            tableLayout.removeAllViews();
                            Log.e("size :",""+areaDetailArray.size());

                                globaldata.getAreadetail().addAll(areaDetailArray);
                                getArea();


                            progress.dismiss();
                        }
                    }, 2000);

                }else {

                    ListRentAreaController browseRentAreaCon = new ListRentAreaController();
                    final ArrayList<AreaDetail> areaDetailArray = browseRentAreaCon.searchAll(areaDetail);


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            globaldata.getAreadetail().clear();

                                globaldata.getAreadetail().addAll(areaDetailArray);
                                getArea();



                            progress.dismiss();
                        }
                    }, 1500);

                }

            }
        });


    }


    private void getArea() {



        for (final AreaDetail areadetail : globaldata.getAreadetail()) {
            int i = 0;

            tableLayout = findViewById(R.id.SearchResult);
            TableRow tableRow = new TableRow(ListRentArea.this);



            // Create Layout
            LinearLayout picResultLinearText = new LinearLayout(ListRentArea.this);
            LinearLayout areaResultLinearText = new LinearLayout(ListRentArea.this);
            LinearLayout buttonResultLinearText = new LinearLayout(ListRentArea.this);

            LinearLayout.LayoutParams picParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            picParams.setMargins(30, 20, 30, 5);
            picParams.height = 200;
            picParams.width = 200;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(30, 20, 30, 5);


            LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            buttonLayout.setMargins(30, 10, 30, 10);


            // Create Opject
            ImageView imageView = new ImageView(ListRentArea.this);
            final TextView areanameText = new TextView(ListRentArea.this);
            TextView priceText = new TextView(ListRentArea.this);
            final TextView status = new TextView(ListRentArea.this);
            Button editBtn = new Button(ListRentArea.this);
            Button removeBtn = new Button(ListRentArea.this);

            // Set Opject

            imageView.setImageResource(R.drawable.ic_shop);

            areanameText.setText(areadetail.getAreaId());
            areanameText.setTextColor(getApplication().getResources().getColor(R.color.white));

            priceText.setText(" " + areadetail.getPrice() + " บาท");
            priceText.setTextColor(getApplication().getResources().getColor(R.color.white));

            status.setText(areadetail.getStatus());
            status.setTextColor(getApplication().getResources().getColor(R.color.white));


            globaldata.setAreaID(areadetail.getAreaId());


            editBtn.setBackgroundResource(R.drawable.buttonstyle);
            editBtn.setText("แก้ไข");
            editBtn.setTextColor(getApplication().getResources().getColor(R.color.white));

            removeBtn.setBackgroundResource(R.drawable.buttonstyle);
            removeBtn.setTextColor(getApplication().getResources().getColor(R.color.white));
            removeBtn.setText("ลบ");

            // Add Layout

            picResultLinearText.addView(imageView, picParams);
            picResultLinearText.setOrientation(LinearLayout.VERTICAL);


            buttonResultLinearText.addView(editBtn, buttonLayout);
            buttonResultLinearText.addView(removeBtn, buttonLayout);
            buttonResultLinearText.setOrientation(LinearLayout.VERTICAL);


            areaResultLinearText.addView(areanameText, layoutParams);
            areaResultLinearText.addView(priceText, layoutParams);
            areaResultLinearText.addView(status, layoutParams);
            areaResultLinearText.setOrientation(LinearLayout.VERTICAL);


            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        globaldata.getAreadetail().clear();
                        AreaDetail areaDetail = new AreaDetail();
                        for (AreaZone areaZone : globaldata.getAreaZone()) {
                            if (areaZone.getAreaType().equals(typeSinner.getSelectedItem().toString())) {
                                areaDetail.setAreazone(areaZone);
                                areaDetail.setAreaId(areanameText.getText().toString());
                                globaldata.getAreadetail().add(areaDetail);

                                globaldata.setAreaID(areanameText.getText().toString());
                                Log.e("GlobalSize ", "" + globaldata.getAreaID());



                                if(status.getText().toString().equals("ว่าง")){
                                    startActivity(new Intent(ListRentArea.this, EditAreaDetail.class));
                                    finish();
                                }else {
                                    Toast.makeText(ListRentArea.this, "ไม่สามารถแก้ไขได้", Toast.LENGTH_SHORT).show();
                                }
                            }

                    }


                }
            });

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (final AreaDetail areadetail : globaldata.getAreadetail()) {
                        for (AreaZone areaZone : globaldata.getAreaZone()) {
                            if (areaZone.getAreaType().equals(typeSinner.getSelectedItem().toString())) {
                                areadetail.setAreazone(areaZone);
                                globaldata.setAreaID(areanameText.getText().toString());

                                Log.e("GlobalSize ", "" + areadetail.getAreazone().getZoneId()+" "+areadetail.getAreaId());


                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(ListRentArea.this);
                                builder.setMessage("ยืนยันการลบพื้นที่");
                                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ListRentAreaController listRentArea = new ListRentAreaController();
                                        startActivity(new Intent(ListRentArea.this,adminHome.class));

                                        listRentArea.DeleteAreaDetail(areadetail);
                                        Toast.makeText(ListRentArea.this, "ทำการลบสำเร็จ", Toast.LENGTH_SHORT).show();

                                        finish();

                                    }
                                });
                                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(ListRentArea.this,ListRentArea.class));

                                    }
                                });



                                if(status.getText().toString().equals("ว่าง")){
                                    builder.show();
                                }else {
                                    Toast.makeText(ListRentArea.this, "ไม่สามารถลบพื้นที่ได้", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    }

                }


            });

            tableRow.addView(picResultLinearText);
            tableRow.addView(areaResultLinearText);
            tableRow.addView(buttonResultLinearText);


            tableLayout.addView(tableRow);



        }

    }


    private void addStoreType() {
        globaldata.getAreaZone().clear();
        final ArrayList<String> storetype = new ArrayList<String>();
        final ArrayList<AreaZone> areaZone = BrowseRentAreaController.getAreaZone();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("Size", "" + areaZone.size());

                for (AreaZone areaZone : areaZone) {
                    storetype.add(areaZone.getAreaType());
                    Log.e("Size", "" +areaZone.getAreaType());

                    globaldata.getAreaZone().add(areaZone);

                }

                ArrayAdapter<String> StringStoreType = new ArrayAdapter<String>(ListRentArea.this,
                        android.R.layout.simple_dropdown_item_1line, storetype);
                typeSinner.setAdapter(StringStoreType);


                progress.dismiss();
            }
        }, 3500);

    }
}