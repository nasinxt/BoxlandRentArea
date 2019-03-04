package com.example.nasin.boxlandrentarea.ActivityClass.ConfirmPayment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.ListBookingArea.ListBookingArea;
import com.example.nasin.boxlandrentarea.Class.Booking;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Receipt;
import com.example.nasin.boxlandrentarea.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConfirmPayment extends AppCompatActivity {

        TextView areaDepositView, areaNameView;
        TextView bookingIdR, bookingDateR, bookingStatusR, paydateR, phonenumT, tenantNameReceipt;
        GlobalClass globaldata;
        ImageView conPicUpload;
        private Uri filePath;
        private final int PICK_IMAGE_REQUEST = 71;
        Booking bookingData = new Booking();
        ProgressDialog progressDialog;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_confirm_payment);
            progressDialog = ProgressDialog.show(this, "", "กำลังโหลด", true);

            conPicUpload = findViewById(R.id.conPicUpload);
            conPicUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseImage();

                }
            });


            globaldata = (GlobalClass) getApplicationContext();

            areaDepositView = findViewById(R.id.areaDepositReceipt);
            bookingIdR = findViewById(R.id.bookingIdR);
            areaNameView = findViewById(R.id.areanameReceipt);
            bookingDateR = findViewById(R.id.bookingDateR);
            bookingStatusR = findViewById(R.id.bookingStatusR);
            paydateR = findViewById(R.id.paydateR);
            areaDepositView = findViewById(R.id.areaDepositReceipt);
            tenantNameReceipt = findViewById(R.id.tenantNameReceipt);
            phonenumT = findViewById(R.id.tenantPhoneReceipt);

            ConfirmPaymentController conPaymentController = new ConfirmPaymentController();

            Log.e("Booking",""+globaldata.getBookingArray().get(0).getBookingId()+" "+globaldata.getBookingArray().get(0).getTenant().getUsername());

            final Booking booking = conPaymentController.getBookingArea(globaldata.getBookingArray().get(0));


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    //String bookingDate = ""+formatter.format(bookingData.getBookingDateTime());

                    bookingData = booking;
                    bookingIdR.setText(bookingData.getBookingId());
                    areaNameView.setText(bookingData.getAreadetail().getAreaId());
                    bookingDateR.setText(formatter.format(bookingData.getBookingDateTime()));
                    paydateR.setText(formatter.format(bookingData.getPayDepositDate()));
                    bookingStatusR.setText(bookingData.getBookingStatus());
                    areaDepositView.setText("" + bookingData.getAreadetail().getDeposit() + " บาท");
                    phonenumT.setText(bookingData.getTenant().getPhoneNum());
                    tenantNameReceipt.setText(bookingData.getTenant().getFname() + " " + bookingData.getTenant().getLname());
                    progressDialog.dismiss();

                }
            }, 4000);


            Button sendRecipt = findViewById(R.id.sendRecipt);
            sendRecipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog = new ProgressDialog(ConfirmPayment.this);

                    if (filePath != null) {
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();

                        Receipt receipt = new Receipt();


                        Date dateC = new Date();
                        Calendar todayDate = Calendar.getInstance();
                        todayDate.setTime(dateC);
                        receipt.setPaymentDate(dateC);
                        receipt.setPaymentStatus("รอการยืนยันการชำระเงิน");
                        receipt.setPicPayment(filePath.toString());
                        receipt.setBooking(bookingData);
                        globaldata.getReceipts().add(receipt);
                        uploadImage(receipt);


                    } else {
                        Toast.makeText(ConfirmPayment.this, "กรุณาอัพโหลดรูปภาพ", Toast.LENGTH_SHORT).show();
                    }


                }
            });


            Button cancelConfirmBtn = findViewById(R.id.cancelConfirmBtn);
            cancelConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String dateInString = paydateR.getText().toString();
                    Calendar historyDate = Calendar.getInstance();

                    try {
                        Date dateH = formatter.parse(dateInString);
                        historyDate.setTime(dateH);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    Date dateC = new Date();
                    Calendar todayDate = Calendar.getInstance();
                    todayDate.setTime(dateC);

                    progressDialog = new ProgressDialog(ConfirmPayment.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    if (!historyDate.after(todayDate)) {
                        Toast.makeText(ConfirmPayment.this, "ไม่สามารถยกเลิกได้", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        final ConfirmPaymentController conFirm = new ConfirmPaymentController();
                        progressDialog.dismiss();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                conFirm.cancelBooking(bookingData);
                                Toast.makeText(ConfirmPayment.this, "ยกเลิกสำเร็จ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ConfirmPayment.this, ListBookingArea.class));

                            }
                        }, 1000);


                    }


                }
            });


        }

        @Override
        public void onResume() {
            super.onResume();

        }

        private void chooseImage() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    ImageView imageView = findViewById(R.id.imageBefore);
                    // imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        public void uploadImage(final Receipt receipt) {
            final Boolean[] check = {null};
            final StorageReference mStorageRef;
            mStorageRef = FirebaseStorage.getInstance().getReference();
            final Uri uri = null;
            Log.e("Picture", "" + receipt.getPicPayment());

            // Count Payment
            final ArrayList<String> countPayment = new ConfirmPaymentController().countPayment();

            // Count Delayed
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

            if (countPayment.size() < 1) {
                countPayment.add("Receipt-1001");
                Log.e("countPayment", "" + countPayment.get(0));

            } else {
                Log.e("String", "" + countPayment.get(0).toString());
                String receiptId = countPayment.get(countPayment.size() - 1);
                int plusId = 1000+(countPayment.size()+1);
                Log.e("Trans", "" + plusId);
                countPayment.clear();
                countPayment.add("Receipt-" + plusId);

            }
            receipt.setReceiptId(countPayment.get(0));

            StorageReference ref = mStorageRef.child("payment/" + receipt.getReceiptId() + "/" + receipt.getReceiptId());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            receipt.setPicPayment(receipt.getReceiptId());
                            receipt.getBooking().setTenant(globaldata.getTenantArray().get(0));

                            check[0] = true;
                            new ConfirmPaymentController().addRecipt(receipt);
                            Toast.makeText(ConfirmPayment.this, "ทำรายการสำเร็จ", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                            startActivity(new Intent(ConfirmPayment.this,ListBookingArea.class));


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ConfirmPayment.this, "ทำรายการไม่สำเร็จโปรดลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show();

                            check[0] = false;
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });




            }
        }, 2000);


        }



}
