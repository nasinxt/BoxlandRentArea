package com.example.nasin.boxlandrentarea.ActivityClass.ListBookingArea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.ConfirmPayment.ConfirmPayment;
import com.example.nasin.boxlandrentarea.Class.Booking;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class ListBookingArea extends AppCompatActivity {

    GlobalClass globaldata;
    TableLayout tableLayout,tableLayout2;
    EditText searchBooking;
    Button searchBookingBtn;
    ProgressDialog progress;
    ArrayList<Booking> listBooking = new ArrayList<Booking>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_booking_area);
        Firebase.setAndroidContext(this);
        globaldata = (GlobalClass) getApplicationContext();
        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        ListBookingController listBookingCon  = new ListBookingController();

        listBooking = listBookingCon.getBookingArea(globaldata.getTenantArray().get(0));

        tableLayout = findViewById(R.id.bookingResult);
        tableLayout2 = findViewById(R.id.cancleResult);
        tableLayout.removeAllViews();
        tableLayout2.removeAllViews();

        globaldata.getBookingArray().clear();







        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                globaldata.getBookingArray().addAll(listBooking);

                for(Booking booking : listBooking){
                    Log.e(""+booking.getBookingId()," "+booking.getBookingStatus());

                    if(booking.getBookingStatus().equals("ถูกยกเลิก")){
                        getCancleBookingArea(booking);

                    }else if(booking.getBookingStatus().equals("ทำการจองสำเร็จ")){
                        getConfirmBookingArea(booking);

                    }else if(booking.getBookingStatus().equals("รอการชำระเงิน")){
                        getBookingArea(booking);

                    }else {
                       // getBookingArea(booking);

                    }

                }



                progress.dismiss();
            }
        },3000);


    }





    private void getBookingArea(final Booking booking) {

        tableLayout = findViewById(R.id.bookingResult);
        if(tableLayout != null){
            tableLayout.removeAllViews();
        }
        TableRow tableRow = new TableRow(ListBookingArea.this);
        LinearLayout areaResultLinearText = new LinearLayout(ListBookingArea.this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);


        ImageView imageView = new ImageView(ListBookingArea.this);
        final TextView areanameText = new TextView(ListBookingArea.this);
        TextView status = new TextView(ListBookingArea.this);

        imageView.setImageResource(R.drawable.ic_grocery);
        areanameText.setText( booking.getBookingId() );
        status.setText(booking.getBookingStatus());


        areaResultLinearText.addView(areanameText, layoutParams);
        areaResultLinearText.addView(status ,layoutParams);
        areaResultLinearText.setOrientation(LinearLayout.VERTICAL);



        areaResultLinearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(Booking booking :  listBooking){
                    if(booking.getBookingId().toLowerCase().equals(areanameText.getText().toString().toLowerCase())  ){
                        globaldata.getBookingArray().clear();
                        booking.setTenant(globaldata.getTenantArray().get(0));
                        globaldata.getBookingArray().add(booking);
                        Toast.makeText(ListBookingArea.this, ""+booking.getBookingStatus(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ListBookingArea.this,ConfirmPayment.class));
                    }
                }

            }
        });

        tableRow.addView(imageView);
        tableRow.addView(areaResultLinearText);

        tableLayout.addView(tableRow);


    }





    private void getCancleBookingArea(final Booking booking) {

        tableLayout2 = findViewById(R.id.cancleResult);
        TableRow tableRow = new TableRow(ListBookingArea.this);
        LinearLayout areaResultLinearText = new LinearLayout(ListBookingArea.this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);


        ImageView imageView = new ImageView(ListBookingArea.this);
        final TextView areanameText = new TextView(ListBookingArea.this);
        TextView status = new TextView(ListBookingArea.this);
        ImageView button = new ImageView(ListBookingArea.this);

        imageView.setImageResource(R.drawable.ic_grocery);
        areanameText.setText( booking.getBookingId() );
        status.setText(booking.getBookingStatus());


        areaResultLinearText.addView(areanameText, layoutParams);
        areaResultLinearText.addView(status ,layoutParams);
        areaResultLinearText.setOrientation(LinearLayout.VERTICAL);


        tableRow.addView(imageView);
        tableRow.addView(areaResultLinearText);

        tableLayout2.addView(tableRow);


    }

    private void getConfirmBookingArea(final Booking booking) {

        tableLayout2 = findViewById(R.id.confirmPaymentList);
        TableRow tableRow = new TableRow(ListBookingArea.this);
        LinearLayout areaResultLinearText = new LinearLayout(ListBookingArea.this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);


        ImageView imageView = new ImageView(ListBookingArea.this);
        final TextView areanameText = new TextView(ListBookingArea.this);
        TextView status = new TextView(ListBookingArea.this);
        ImageView button = new ImageView(ListBookingArea.this);

        imageView.setImageResource(R.drawable.ic_grocery);
        areanameText.setText( booking.getBookingId() );
        status.setText(booking.getBookingStatus());


        areaResultLinearText.addView(areanameText, layoutParams);
        areaResultLinearText.addView(status ,layoutParams);
        areaResultLinearText.setOrientation(LinearLayout.VERTICAL);


        tableRow.addView(imageView);
        tableRow.addView(areaResultLinearText);

        tableLayout2.addView(tableRow);


    }


}
