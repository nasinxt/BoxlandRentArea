package com.example.nasin.boxlandrentarea.ActivityClass.ApprovePayment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nasin.boxlandrentarea.ActivityClass.ListNewPayment.ListNewPayment;
import com.example.nasin.boxlandrentarea.ActivityClass.SendReceipt.SendReceipt;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Receipt;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class ApprovePayment extends AppCompatActivity {

    TextView ApprovereceiptId,tenantNameReceipt,tenantPhoneReceipt,areanameReceipt,bookingDateR,paydateR,areaDepositReceipt,bookingStatusR;
    ImageView paymentImg;
    Button cancelConfirmBtn,approvePaymentBtn;
    GlobalClass globaldata;
    ProgressDialog progress;
    Receipt receipt = new Receipt();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_payment);
        globaldata = (GlobalClass) getApplicationContext();
        Firebase.setAndroidContext(this);
        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        ApprovereceiptId = (TextView)findViewById(R.id.ApprovereceiptId);
        tenantNameReceipt = (TextView)findViewById(R.id.tenantNameReceipt);
        tenantPhoneReceipt = (TextView)findViewById(R.id.tenantPhoneReceipt);
        areanameReceipt = (TextView)findViewById(R.id.areanameReceipt);
        bookingDateR = (TextView)findViewById(R.id.bookingDateR);
        paydateR = (TextView)findViewById(R.id.paydateR);
        areaDepositReceipt = (TextView)findViewById(R.id.areaDepositReceipt);
        bookingStatusR = (TextView)findViewById(R.id.bookingStatusR);

        cancelConfirmBtn = (Button)findViewById(R.id.cancelConfirmBtn);
        approvePaymentBtn = (Button)findViewById(R.id.approvePaymentBtn);


        paymentImg = (ImageView) findViewById(R.id.paymentImg);



        getAprovePayment();





    }

    public void getAprovePayment(){
        final ApprovePaymentController approvePayment = new ApprovePaymentController();
        receipt = approvePayment.getReceipt(globaldata.getReceipts().get(0));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("payment").child(receipt.getReceiptId()).child(receipt.getReceiptId());
                Log.e("storageReference",""+storageReference);
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        Log.e("Url",""+uri);
                        Glide.with(getApplicationContext()).load(uri).into(paymentImg);
                        paymentImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ApprovePayment.this);
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
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                ApprovereceiptId.setText(receipt.getReceiptId());
                tenantNameReceipt.setText(receipt.getBooking().getTenant().getFname()+" "+receipt.getBooking().getTenant().getLname());
                tenantPhoneReceipt.setText(receipt.getBooking().getTenant().getPhoneNum());
                areanameReceipt.setText(receipt.getBooking().getAreadetail().getAreaId());
                bookingDateR.setText(""+formatter.format(receipt.getBooking().getBookingDateTime()));
                paydateR.setText(""+formatter.format(receipt.getBooking().getPayDepositDate()));
                areaDepositReceipt.setText(""+receipt.getBooking().getAreadetail().getDeposit()+" บาท");
                bookingStatusR.setText(receipt.getPaymentStatus());

                cancelConfirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!receipt.getPaymentStatus().equals("ชำระเงินสำเร็จ")){
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(ApprovePayment.this);
                            builder.setMessage("คุณเเน่ใจว่าจะยกเลิกหรือไม่");
                            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ApprovePaymentController approveCon = new ApprovePaymentController();
                                    approveCon.cancelPayment(receipt);

                                    startActivity(new Intent(ApprovePayment.this,ListNewPayment.class));

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
                approvePaymentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!receipt.getPaymentStatus().equals("ชำระเงินสำเร็จ")) {

                            ApprovePaymentController approveCon = new ApprovePaymentController();
                            approveCon.approvePayment(receipt);
                            globaldata.getReceipts().clear();
                            globaldata.getReceipts().add(receipt);
                            final Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(ApprovePayment.this, SendReceipt.class));
                                    Toast.makeText(ApprovePayment.this, "ยืนยันสำเร็จ", Toast.LENGTH_SHORT).show();
                                    Log.e("User : ",globaldata.getReceipts().get(0).getBooking().getBookingId()+" "+globaldata.getReceipts().get(0).getBooking().getTenant().getUsername());                                }
                            }, 1500);

                        }
                    }
                });

                progress.dismiss();

            }
        }, 10000);

    }

}