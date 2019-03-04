package com.example.nasin.boxlandrentarea.ActivityClass.ViewAreaDetail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nasin.boxlandrentarea.ActivityClass.EditAreaDetail.EditAreaDetail;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewRentAreaDetail.ViewRentAreaDetailController;
import com.example.nasin.boxlandrentarea.ActivityClass.adminHome;
import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewAreaDetail extends AppCompatActivity {

    TextView areaStatusView,areaPriceView,areaDepositView,areaDetailView,areaSizeView,areaTypeView,areaNameView;
    ImageView areaViewPic;
    RelativeLayout nextPic;
    Button editAreaDetailR,deleteAreaDetail;
     AreaDetail areaDetail = new AreaDetail();
    ProgressDialog progress;

    GlobalClass globaldata;
    int countPic = 0;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_area_detail);
        Firebase.setAndroidContext(this);

        globaldata = (GlobalClass) getApplicationContext();

        areaNameView = findViewById(R.id.areaNameViewDetail);
        areaTypeView  = findViewById(R.id.areaTypeViewDetail);
        areaSizeView = findViewById(R.id.areaSizeViewDetail);
        areaDetailView = findViewById(R.id.areaDetailViewDetail);
        areaDepositView = findViewById(R.id.areaDepositViewDetail);
        areaPriceView = findViewById(R.id.areaPriceViewDetail);
        areaStatusView = findViewById(R.id.areaStatusViewDetail);

        areaViewPic = (ImageView) findViewById(R.id.areaViewPicViewDetail);
        nextPic = (RelativeLayout) findViewById(R.id.nextPicViewDetail);
        editAreaDetailR = (Button) findViewById(R.id.editAreaDetail);
        deleteAreaDetail = (Button) findViewById(R.id.deleteAreaDetail);

        progress = ProgressDialog.show(this, "",
                "กำลังโหลด", true);

        viewRentArea();







    }
    @Override
    public void onResume(){
        super.onResume();


    }

    public void viewRentArea(){

        areaDetail = new ViewAreaDetailController().getAreaDetail(globaldata.getAreadetail().get(0));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                globaldata.getAreadetail().clear();
                globaldata.getAreadetail().add(areaDetail);
                areaNameView.setText(areaDetail.getAreaId());
                areaTypeView.setText(areaDetail.getAreazone().getAreaType());
                areaSizeView.setText(areaDetail.getSize()+" ตรม.");
                areaDetailView.setText(areaDetail.getDetail());
                areaDepositView.setText(areaDetail.getDeposit()+" บาท");
                areaPriceView.setText(areaDetail.getPrice()+" บาท");
                areaStatusView.setText(areaDetail.getStatus());

                Log.e("Pic-Size",""+areaDetail.getAreaPic().size());

                if(areaDetail.getAreaPic().size()>0){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Area").child(areaDetail.getAreaId()).child(areaDetail.getAreaPic().get(0));
                    Log.e("strorage Path",""+storageReference);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                            Log.e("Url",""+uri);
                            countPic = 0;
                            Glide.with(getApplicationContext()).load(uri).into(areaViewPic);

                            areaViewPic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewAreaDetail.this);
                                    final View mView = getLayoutInflater().inflate(R.layout.dialog_zoom_image, null);
                                    final PhotoView photoView  = mView.findViewById(R.id.imageViewCenterCrop);
                                    //Glide.with(getApplicationContext()).load(mapUri[0]).into(photoView);

                                    Picasso.get().load(uri).into(photoView);
                                    mBuilder.setView(mView);
                                    AlertDialog mDialog = mBuilder.create();
                                    mDialog.show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }


                nextPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(countPic ==(areaDetail.getAreaPic().size()-1)){
                            countPic=0;
                        }else {
                            countPic++;

                        }

                        areaViewPic.setImageBitmap(null);
                        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child("Area").child(areaDetail.getAreaId()).child(areaDetail.getAreaPic().get(countPic));
                        Log.e("strorage Path",""+storageReference1);

                        storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                Log.e("Url",""+uri);
                                Glide.with(getApplicationContext()).load(uri).into(areaViewPic);
                                areaViewPic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewAreaDetail.this);
                                        final View mView = getLayoutInflater().inflate(R.layout.dialog_zoom_image, null);
                                        final PhotoView photoView  = mView.findViewById(R.id.imageViewCenterCrop);
                                        //Glide.with(getApplicationContext()).load(mapUri[0]).into(photoView);

                                        Picasso.get().load(uri).into(photoView);
                                        mBuilder.setView(mView);
                                        AlertDialog mDialog = mBuilder.create();
                                        mDialog.show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                });



                // แก้ไข
                editAreaDetailR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ViewAreaDetail.this,EditAreaDetail.class));
                    }
                });

                // ลบพื้นที่
                deleteAreaDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ViewAreaDetail.this, "Size : "+globaldata.getAreadetail().size(), Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(ViewAreaDetail.this);
                        builder.setMessage("ยืนยันการลบพื้นที่");
                        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ViewAreaDetailController viewAreaCon = new ViewAreaDetailController();
                                viewAreaCon.DeleteAreaDetail(areaDetail);
                                globaldata.getAreadetail().clear();
                                areaDetail.getAreaPic().clear();
                                Toast.makeText(ViewAreaDetail.this, "ทำการลบสำเร็จ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ViewAreaDetail.this,adminHome.class));
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
                progress.dismiss();


            }
        },3500);


    }











}

