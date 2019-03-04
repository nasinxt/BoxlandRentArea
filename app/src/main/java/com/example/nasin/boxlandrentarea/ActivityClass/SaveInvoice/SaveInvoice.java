package com.example.nasin.boxlandrentarea.ActivityClass.SaveInvoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nasin.boxlandrentarea.ActivityClass.home;
import com.example.nasin.boxlandrentarea.BuildConfig;
import com.example.nasin.boxlandrentarea.Class.Booking;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.text.SimpleDateFormat;

public class SaveInvoice extends AppCompatActivity {

    TextView areaPriceView,areaDepositView,areaDetailView,areaSizeView,areaTypeView,bookingID;
    TextView tenantNameInv,tenantPhoneInv,tenantEmailInv,payDateInv;
    DatabaseReference databaseReference;
    GlobalClass globaldata;
    Button sendInvoice,backBtn;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_invoice);
        globaldata = (GlobalClass) getApplicationContext();


        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);


        sendInvoice = findViewById(R.id.InvBtn);
        bookingID = findViewById(R.id.areaNameInv);
        areaTypeView = findViewById(R.id.areaTypeInv);
        areaDetailView = findViewById(R.id.areaDetailInv);
        areaDepositView = findViewById(R.id.areaDepositInv);
        areaPriceView = findViewById(R.id.areaPriceInv);
        tenantNameInv = findViewById(R.id.tenantNameInv);
        tenantPhoneInv = findViewById(R.id.tenantPhoneInv);
        tenantEmailInv = findViewById(R.id.tenantEmailInv);
        payDateInv = findViewById(R.id.payDateInv);
        backBtn = findViewById(R.id.backBtn);
        getInvoice();









    }








    @Override
    public void onResume() {
        super.onResume();



    }
    public void getInvoice(){
        globaldata.getBookingList().clear();
        SaveInvoiceController sendInvCon = new SaveInvoiceController();
        final Booking booking = sendInvCon.getBookingArea(globaldata.getBookingArray().get(0));
        globaldata.getBookingArray().add(booking);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                bookingID.setText(booking.getBookingId());
                areaTypeView.setText(booking.getAreadetail().getAreazone().getAreaType());
                areaDetailView.setText(booking.getAreadetail().getDetail());
                areaDepositView.setText(""+booking.getAreadetail().getDeposit()+" บาท");
                areaPriceView.setText(""+booking.getAreadetail().getPrice()+" บาท");
                tenantNameInv.setText(booking.getTenant().getFname()+" "+booking.getTenant().getLname());
                tenantPhoneInv.setText(booking.getTenant().getPhoneNum());
                tenantEmailInv.setText(booking.getTenant().getEmail());

                payDateInv.setText(""+formatter.format(booking.getPayDepositDate()));

                // progressDialog.dismiss();


                sendInvoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progress = ProgressDialog.show(SaveInvoice.this, "", "กำลังโหลด", true);

                        File invoicefile = new SaveInvoiceController().createInvoicePdf(booking);
                        final Uri invoicePath = FileProvider.getUriForFile(SaveInvoice.this,BuildConfig.APPLICATION_ID + ".provider",invoicefile);
                        Log.e("invoicePath",""+invoicefile);


                        String fileName = getFileName(invoicePath);
                        Log.e("Path : ",""+fileName);
                        Uri invoiceRealPath = Uri.parse("content://com.estrongs.files/storage/emulated/0/"+fileName);
                        Log.e("invoicePath",""+invoiceRealPath);


/*
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("file*//*");
                        startActivityForResult(Intent.createChooser(intent,"Select File"),0);
*/

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(invoiceRealPath, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                        progress.dismiss();

/*
                        String toEmail = booking.getTenant().getEmail();
                        String subject = "Invoice id : "+bookingID.getText().toString();
                        String message = "ถึง คุณ "+tenantNameInv.getText().toString()+" โปรดชำระเงินจำนวน " + areaDepositView.getText().toString() +" บาท";
                        Intent mail = new Intent(Intent.ACTION_SEND);
                        mail.putExtra(Intent.EXTRA_EMAIL,new String[]{toEmail});
                        mail.putExtra(Intent.EXTRA_SUBJECT, subject);
                        mail.putExtra(Intent.EXTRA_TEXT, message);
                        mail.putExtra(Intent.EXTRA_STREAM, invoicePath);

                        mail.setType("message/rfc822");
                        startActivity(Intent.createChooser(mail, "Send email via:"));
*/

/*
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(SaveInvoice.this);
                        builder.setMessage("ส่งใบจองสำเร็จ");
                        builder.setPositiveButton("กลับสู่หน้าหลัก", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(SaveInvoice.this,home.class));

                            }
                        });

                        builder.show();
*/

                    }
                });

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SaveInvoice.this,home.class));
                    }
                });





                progress.dismiss();

            }
        },8000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("",""+data.getData());
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }





    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }





}