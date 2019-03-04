package com.example.nasin.boxlandrentarea.ActivityClass.BrowseRentArea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.example.nasin.boxlandrentarea.ActivityClass.BookingArea.BookingArea;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewRentAreaDetail.ViewRentAreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BrowseRentArea extends AppCompatActivity {
    EditText search;
    GlobalClass globaldata;
    TableLayout tableLayout;
    Spinner typeSinner;
    Button searchBtn;
    final Uri[] mapUri = new Uri[1];
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_rent_area);
        globaldata = (GlobalClass) getApplicationContext();


        search = (EditText) findViewById(R.id.search);
        typeSinner = (Spinner) findViewById(R.id.typeSinner);
        searchBtn = (Button)findViewById(R.id.searchBtn);
        addStoreType();
        progress = ProgressDialog.show(this, "",
                "กำลังโหลด", true);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAreaByZone();
            }
        });


        ImageView image = (ImageView) findViewById(R.id.mapImg);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BrowseRentArea.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_zoom_image, null);
                final PhotoView photoView  = mView.findViewById(R.id.imageViewCenterCrop);

                Picasso.get().load(R.drawable.boxlandmap01).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }


    public void findAreaByZone(){
        progress = ProgressDialog.show(BrowseRentArea.this, "",
                "กำลังโหลด", true);
        globaldata.getAreadetail().clear();
        tableLayout = findViewById(R.id.SearchResult);
        tableLayout.removeAllViews();
        ImageView image = (ImageView) findViewById(R.id.mapImg);
        image.setImageDrawable(null);

        AreaDetail areaDetail = new AreaDetail();


        for (AreaZone areaZone :  globaldata.getAreaZone()){
            if(areaZone.getAreaType().equals(typeSinner.getSelectedItem().toString())){
                areaDetail.setAreazone(areaZone);
            }

        }

        if (!search.getText().toString().isEmpty()) {

            BrowseRentAreaController browseRentAreaCon = new BrowseRentAreaController();
            areaDetail.setAreaId(search.getText().toString());
            Log.e("",""+areaDetail.getAreaId().toLowerCase());

            final ArrayList<AreaDetail> areaDetailArray = browseRentAreaCon.findAreaByName(areaDetail);
            final ArrayList<AreaDetail> areaDetailArrayUser = browseRentAreaCon.findAreaByNameUser(areaDetail);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    globaldata.getAreadetail().clear();
                    tableLayout = findViewById(R.id.SearchResult);
                    tableLayout.removeAllViews();
                    Log.e("size areaDetailArray",""+areaDetailArray.size());

                    if (globaldata.getTenantArray().size() == 0 && areaDetailArray.size() > 0) {
                        globaldata.getAreadetail().addAll(areaDetailArray);
                        getArea();


                    } else if (globaldata.getTenantArray().size() != 0 && areaDetailArrayUser.size() > 0) {
                        for (AreaDetail area : areaDetailArrayUser) {
                            globaldata.getAreadetail().add(area);
                        }
                        getArea();

                    }else {
                        progress.dismiss();
                        Toast.makeText(BrowseRentArea.this, "ไม่พบข้อมูล", Toast.LENGTH_SHORT).show();

                    }
                }
            }, 1500);


        } else {

            BrowseRentAreaController browseRentAreaCon = new BrowseRentAreaController();
            final ArrayList<AreaDetail> areaDetailArray = browseRentAreaCon.findAreaByZone(areaDetail);
            final ArrayList<AreaDetail> areaDetailArrayUser = browseRentAreaCon.userSearch(areaDetail);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    globaldata.getAreadetail().clear();

                    if (globaldata.getTenantArray().size() == 0 && areaDetailArray.size() > 0) {
                        globaldata.getAreadetail().addAll(areaDetailArray);
                        getArea();


                    } else if (globaldata.getTenantArray().size() != 0 && areaDetailArrayUser.size() > 0) {
                        for (AreaDetail area : areaDetailArrayUser) {
                            globaldata.getAreadetail().add(area);
                        }
                        getArea();

                    }else {
                        progress.dismiss();
                        Toast.makeText(BrowseRentArea.this, "ไม่พบข้อมูล", Toast.LENGTH_SHORT).show();

                    }
                }
            }, 1500);

        }
    }






    private void getArea() {

        final ImageView image = (ImageView) findViewById(R.id.mapImg);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Zone").child(globaldata.getAreadetail().get(0).getAreazone().getAreaPic());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Url", "" + uri);
                mapUri[0] = uri;


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        final Handler handler = new Handler();






        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // progressDialog.dismiss();
                ImageView image = (ImageView) findViewById(R.id.mapImg);
                Glide.with(getApplicationContext()).load(mapUri[0]).into(image);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BrowseRentArea.this);
                        final View mView = getLayoutInflater().inflate(R.layout.dialog_zoom_image, null);
                        final PhotoView photoView  = mView.findViewById(R.id.imageViewCenterCrop);
                        //Glide.with(getApplicationContext()).load(mapUri[0]).into(photoView);

                        Picasso.get().load(mapUri[0]).into(photoView);
                        mBuilder.setView(mView);
                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    }
                });

            }
        }, 1500);


        for (AreaDetail areadetail : globaldata.getAreadetail()) {
            int i = 0;
            LinearLayout areaResultLinear = findViewById(R.id.areaResultLinear);
            tableLayout = findViewById(R.id.SearchResult);
            TableRow tableRow = new TableRow(BrowseRentArea.this);

            LinearLayout areaResultLinearText = new LinearLayout(BrowseRentArea.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout buttonLinear = new LinearLayout(BrowseRentArea.this);

            layoutParams.setMargins(30, 20, 30, 0);

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(30, 20, 30, 30);

            LinearLayout pictureLinear = new LinearLayout(BrowseRentArea.this);
            LinearLayout.LayoutParams picture = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            picture.setMargins(30, 20, 30, 30);


            ImageView imageView = new ImageView(BrowseRentArea.this);
            final TextView areanameText = new TextView(BrowseRentArea.this);
            final TextView areaStatus = new TextView(BrowseRentArea.this);
            TextView priceText = new TextView(BrowseRentArea.this);
            Button Booking = new Button(BrowseRentArea.this);
            Button view = new Button(BrowseRentArea.this);


            imageView.setImageResource(R.drawable.ic_grocery);
            pictureLinear.addView(imageView,picture);


            areanameText.setText(areadetail.getAreaId());
            areanameText.setTextColor(getApplication().getResources().getColor(R.color.white));

            priceText.setText(" " + areadetail.getPrice() + " บาท");
            priceText.setTextColor(getApplication().getResources().getColor(R.color.white));

            areaStatus.setText(areadetail.getStatus());
            areaStatus.setTextColor(getApplication().getResources().getColor(R.color.white));

            globaldata.setAreaID(areadetail.getAreaId());

            areaResultLinearText.addView(areanameText, layoutParams);
            areaResultLinearText.addView(priceText, layoutParams);
            areaResultLinearText.addView(areaStatus, layoutParams);
            areaResultLinearText.setOrientation(LinearLayout.VERTICAL);

            view.setBackgroundResource(R.drawable.buttonstyle);
            view.setText("ดูรายละเอียด");
            view.setTextColor(getApplication().getResources().getColor(R.color.white));

           /* Booking.setBackgroundResource(R.drawable.buttonstyle);
            Booking.setTextColor(Color.parseColor("#000000"));
            Booking.setText("จองพื้นที่");
            buttonLinear.addView(Booking, layoutParams1);*/

            buttonLinear.addView(view, layoutParams);
            buttonLinear.setOrientation(LinearLayout.VERTICAL);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        globaldata.getAreadetail().clear();
                        AreaDetail areaDetail = new AreaDetail();

                        for (AreaZone areaZone :  globaldata.getAreaZone()){
                            if(areaZone.getAreaType().equals(typeSinner.getSelectedItem().toString())){
                                areaDetail.setAreazone(areaZone);
                                areaDetail.setAreaId(areanameText.getText().toString());
                                globaldata.getAreadetail().add(areaDetail);

                                globaldata.setAreaID(areanameText.getText().toString());
                                Log.e("GlobalSize ",""+ globaldata.getAreaID());
                                if(globaldata.getTenantArray().size()>0){
                                    startActivity(new Intent(BrowseRentArea.this, BookingArea.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(BrowseRentArea.this, ViewRentAreaDetail.class));
                                    finish();
                                }
                            }
                    }
                }
            });

            tableRow.addView(pictureLinear);
            tableRow.addView(areaResultLinearText);
            tableRow.addView(buttonLinear);

            tableLayout.addView(tableRow);
            progress.dismiss();



        }
    }





    private void addStoreType() {
        globaldata.getAreaZone().clear();
        final ArrayList<String> areaType = new ArrayList<String>();
        final ArrayList<AreaZone> areaZone = BrowseRentAreaController.getAreaZone();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("Size",""+areaZone.size());
                for (AreaZone areaZone : areaZone){
                    areaType.add(areaZone.getAreaType());
                    globaldata.getAreaZone().add(areaZone);
                }
                ArrayAdapter<String> StringAreaType = new ArrayAdapter<String>(BrowseRentArea.this,
                        android.R.layout.simple_dropdown_item_1line,areaType);
                typeSinner.setAdapter(StringAreaType);
                progress.dismiss();

            }
        },4000);

    }

}