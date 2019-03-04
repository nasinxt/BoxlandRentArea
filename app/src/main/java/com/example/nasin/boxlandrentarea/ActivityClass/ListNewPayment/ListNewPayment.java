package com.example.nasin.boxlandrentarea.ActivityClass.ListNewPayment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.ApprovePayment.ApprovePayment;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Receipt;
import com.example.nasin.boxlandrentarea.R;

import java.util.ArrayList;

public class ListNewPayment extends AppCompatActivity {
    TableLayout tableLayout;
    GlobalClass globaldata;
    Button listOldPayment;
    ArrayList<Receipt> receipts = new ArrayList<Receipt>();
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_new_payment);
        tableLayout = findViewById(R.id.listApprovePayment);
        ListNewPaymentController lit = new ListNewPaymentController();
        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        receipts = lit.getReceipt();

        globaldata = (GlobalClass) getApplicationContext();
        listOldPayment = findViewById(R.id.listOldPayment);
        listOldPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListNewPayment.this,ListOldPayment.class));
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("Size",""+receipts.size());
                tableLayout = findViewById(R.id.listApprovePayment);

                if(receipts.size()>0){
                    if(tableLayout != null ){
                        tableLayout.removeAllViews();
                    }

                    for (Receipt receipt : receipts){
                        Log.e("TEXT",""+receipt.getReceiptId());
                        getReceipt(receipt);
                    }
                    progress.dismiss();

                }else {
                    progress.dismiss();

                }



            }
        }, 3000);


    }

    private void getReceipt(final Receipt receipt) {
        Toast.makeText(this, "GetReceipt", Toast.LENGTH_SHORT).show();

        tableLayout = findViewById(R.id.listApprovePayment);

        TableRow tableRow = new TableRow(ListNewPayment.this);
        LinearLayout areaResultLinearText = new LinearLayout(ListNewPayment.this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);


        ImageView imageView = new ImageView(ListNewPayment.this);
        final TextView receiptId = new TextView(ListNewPayment.this);
        TextView status = new TextView(ListNewPayment.this);
        ImageView button = new ImageView(ListNewPayment.this);

        imageView.setImageResource(R.drawable.ic_grocery);
        receiptId.setText( receipt.getReceiptId() );
        receiptId.setTextColor(getApplication().getResources().getColor(R.color.white));

        status.setText(receipt.getPaymentStatus());
        status.setTextColor(getApplication().getResources().getColor(R.color.white));


        areaResultLinearText.addView(receiptId, layoutParams);
        areaResultLinearText.addView(status ,layoutParams);
        areaResultLinearText.setOrientation(LinearLayout.VERTICAL);



        areaResultLinearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Receipt receipt : receipts){
                    if(receiptId.getText().toString().toLowerCase().equals(receipt.getReceiptId().toLowerCase())){
                        globaldata.getReceipts().clear();
                        Receipt receipt1 = new Receipt();
                        receipt1.setReceiptId(receiptId.getText().toString());
                        globaldata.getReceipts().add(receipt);
                        startActivity(new Intent(ListNewPayment.this,ApprovePayment.class));
                    }

                }




            }
        });

        tableRow.addView(imageView);
        tableRow.addView(areaResultLinearText);

        tableLayout.addView(tableRow);



    }



}
