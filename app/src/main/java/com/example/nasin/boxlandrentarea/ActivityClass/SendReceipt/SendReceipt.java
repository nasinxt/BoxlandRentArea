package com.example.nasin.boxlandrentarea.ActivityClass.SendReceipt;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.adminHome;
import com.example.nasin.boxlandrentarea.ActivityClass.home;
import com.example.nasin.boxlandrentarea.BuildConfig;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Receipt;
import com.example.nasin.boxlandrentarea.R;

import java.io.File;
import java.text.SimpleDateFormat;

public class SendReceipt extends AppCompatActivity {

    TextView receiptId,tenantName,tenantPhone,areaIdReceipt,areaZoneReceipt,depositReceipt,paymentdate,bookingDateReceipt;
    Button sendReciptBtn;
    GlobalClass globaldata;
    ProgressDialog progress;
    Receipt receipt = new Receipt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receipt);
        globaldata = (GlobalClass) getApplicationContext();

        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        receiptId = findViewById(R.id.receiptId);
        tenantName = findViewById(R.id.tenantName);
        tenantPhone = findViewById(R.id.tenantPhone);
        areaIdReceipt = findViewById(R.id.areaIdReceipt);
        areaZoneReceipt = findViewById(R.id.areaZoneReceipt);
        depositReceipt = findViewById(R.id.depositReceipt);
        paymentdate = findViewById(R.id.paymentdate);
        bookingDateReceipt = findViewById(R.id.paymentdate);

        sendReciptBtn = findViewById(R.id.sendReciptBtn);


        getReceipt();


    }


    @Override
    public void onResume() {
        super.onResume();


    }

    public void getReceipt() {
        globaldata.getBookingList().clear();
        SendReceiptController sendReceiptCon = new SendReceiptController();

        receipt = sendReceiptCon.getReceipt(globaldata.getReceipts().get(0));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                receiptId.setText(""+receipt.getReceiptId());
                tenantName.setText(receipt.getBooking().getTenant().getFname() + " " + receipt.getBooking().getTenant().getLname());
                tenantPhone.setText(receipt.getBooking().getTenant().getPhoneNum());
                areaIdReceipt.setText(""+receipt.getBooking().getAreadetail().getAreaId());
                areaZoneReceipt.setText(""+receipt.getBooking().getAreadetail().getAreazone().getAreaType());
                depositReceipt.setText(""+receipt.getBooking().getAreadetail().getDeposit()+" บาท");
                paymentdate.setText(""+formatter.format(receipt.getPaymentDate()));
                bookingDateReceipt.setText(""+formatter.format(receipt.getBooking().getBookingDateTime()));



                sendReciptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        File invoicefile = new SendReceiptController().CreateReceipt(receipt);
                        final Uri invoicePath = FileProvider.getUriForFile(SendReceipt.this, BuildConfig.APPLICATION_ID + ".provider", invoicefile);
                        Log.e("invoicePath", "" + invoicePath);

                        String toEmail = receipt.getBooking().getTenant().getEmail();
                        String subject = "ใบเสร็จ : " + receipt.getReceiptId();
                        String message = "ถึง คุณ " + tenantName.getText().toString() + " ได้ทำการชำระเงินเเล้วโปรดนำใบเสร็จไปที่ boxland เพื่อทำสัญญาเช่าต่อไป " ;
                        Intent mail = new Intent(Intent.ACTION_SEND);
                        mail.putExtra(Intent.EXTRA_EMAIL, new String[]{toEmail});
                        mail.putExtra(Intent.EXTRA_SUBJECT, subject);
                        mail.putExtra(Intent.EXTRA_TEXT, message);
                        mail.putExtra(Intent.EXTRA_STREAM, invoicePath);

                        mail.setType("message/rfc822");
                        startActivity(Intent.createChooser(mail, "Send email via:"));


                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(SendReceipt.this);
                        builder.setMessage("ทำการส่งใบเสร็จเรียบร้อย");
                        builder.setPositiveButton("กลับสู่หน้าหลัก", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(SendReceipt.this, adminHome.class));

                            }
                        });

                        builder.show();


                    }
                });

                progress.dismiss();

            }
        }, 10000);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

}