package com.example.nasin.boxlandrentarea.ActivityClass.SaveInvoice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.Booking;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SaveInvoiceController {
    DatabaseReference databaseReference;



    public Booking getBookingArea(final Booking bookingClass) {
        final Booking booking = new Booking();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(bookingClass.getTenant().getUsername()).child("BookingArea");
                databaseReference.child(bookingClass.getBookingId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        booking.setBookingId(dataSnapshot.child("bookingId").getValue().toString());
                        booking.setBookingStatus(dataSnapshot.child("bookingStatus").getValue().toString());
                        String PayDeposit = dataSnapshot.child("payDepositDate").getValue().toString();
                        String BookingDate = dataSnapshot.child("bookingDateTime").getValue().toString();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        try {
                            Date date = format.parse(PayDeposit);
                            booking.setPayDepositDate(date);
                            Date date2 = format.parse(BookingDate);
                            booking.setBookingDateTime(date2);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        final AreaDetail areaDetail = new AreaDetail();
                        final Tenant tenantId = new Tenant();
                        // get AreaDetail

                        final String areaId = dataSnapshot.child("areaId").getValue().toString();
                        areaDetail.setAreaId(areaId);
                        final AreaDetail areaDetail1 = getAreaDetail(areaDetail);

                        // get Tenant
                        final Tenant tenant = FindProfileDataByIdcard(bookingClass.getTenant().getUsername());


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AreaDetail areaDetailResult = areaDetail1 ;

                                booking.setAreadetail(areaDetailResult);
                                booking.setTenant(tenant);


                            }
                        }, 3000);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


            }
        }, 3000);


        return booking;


    }




    public AreaDetail getAreaDetail(final AreaDetail areaDetail){
        final AreaDetail areaDetailClass = new AreaDetail();
        final ArrayList<String> selectArea = selectArea(areaDetail.getAreaId());


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(selectArea.get(0))
                        .child("AreaDetail").child(selectArea.get(1));
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String areaName = dataSnapshot.child("AreaName").getValue().toString();
                        String areaSize = dataSnapshot.child("Size").getValue().toString();
                        String detail = dataSnapshot.child("Detail").getValue().toString();
                        String areaDeposit = dataSnapshot.child("Deposit").getValue().toString();
                        String areaPrice = dataSnapshot.child("RentPrice").getValue().toString();
                        String areaStatus = dataSnapshot.child("Status").getValue().toString();
                        areaDetailClass.setAreaId(areaName);
                        areaDetailClass.setSize(areaSize);
                        areaDetailClass.setDetail(detail);
                        areaDetailClass.setDeposit(Double.parseDouble(areaDeposit));
                        areaDetailClass.setPrice(Double.parseDouble(areaPrice));
                        areaDetailClass.setStatus(areaStatus);




                        AreaZone a1 = new AreaZone();
                        a1.setZoneId(selectArea.get(0));
                        areaDetailClass.setAreazone(a1);
                        a1 = getAreaZone(areaDetailClass);

                        areaDetailClass.setAreazone(a1);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }, 3000);

        return areaDetailClass;
    }




    public static AreaZone getAreaZone(final AreaDetail areaDetail) {
        final AreaZone areaZone = new AreaZone();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(areaDetail.getAreazone().getZoneId());
        Log.e("databaseReference",""+databaseReference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                areaZone.setAreaPic(dataSnapshot.child("areaPic").getValue().toString());
                areaZone.setZoneId(dataSnapshot.child("zoneId").getValue().toString());
                areaZone.setAreaType(dataSnapshot.child("areaType").getValue().toString());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return areaZone;
    }


    public ArrayList<String> selectArea(final String areaId) {
        final ArrayList<String>  strings = new ArrayList<String>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone");
        Log.e("path",""+databaseReference + "");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),"Child : "+dataSnapshot.getChildrenCount() + "");
                Log.e("/* StringKey ",""+dataSnapshot.getKey());
                final String zoneId = dataSnapshot.getKey();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(dataSnapshot.getKey()).child("AreaDetail");
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(areaId.equals(dataSnapshot.getKey())){
                            strings.add(zoneId);
                            Log.e("/* StringKey ",""+dataSnapshot.getKey());

                            strings.add(dataSnapshot.getKey());

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return strings;

    }

    public Tenant FindProfileDataByIdcard(final String username) {

        final Tenant tenant = new Tenant();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    tenant.setFname(dataSnapshot.child("firstname").getValue().toString());
                                    tenant.setLname(dataSnapshot.child("lastname").getValue().toString());
                                    tenant.setAddress(dataSnapshot.child("address").getValue().toString());
                                    tenant.setGender(dataSnapshot.child("gender").getValue().toString());
                                    tenant.setIdcardnum(dataSnapshot.child("idcard").getValue().toString());
                                    tenant.setPhoneNum(dataSnapshot.child("phonenumber").getValue().toString());
                                    tenant.setStorename(dataSnapshot.child("storename").getValue().toString());
                                    tenant.setEmail(dataSnapshot.child("email").getValue().toString());
                                    tenant.setStoreDetail(dataSnapshot.child("storedetail").getValue().toString());


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

        }, 3000);


        return tenant;
    }






    public File createInvoicePdf(Booking booking){
        Log.e("Email : ",""+booking.getTenant().getEmail());

        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 841, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();



        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //canvas.drawCircle(50, 50, 10, paint);
        paint.setColor(Color.BLACK);
        // canvase draw Page

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        canvas.drawText(" BOXLAND", 270, 40, paint);

        canvas.drawText(" 540 ม.5 ตำบลหนองหาร Sansai, Chiang Mai, Thailand 50290", 140, 70, paint);


        canvas.drawText(" ใบเเจ้งชำระเงิน", 270, 90, paint);

        canvas.drawText(" หมายเลขคำสั่งซื้อ : "+booking.getBookingId(), 30, 110, paint);
        canvas.drawText(" วันที่ : "+formatter.format(booking.getBookingDateTime()), 30, 130, paint);
        canvas.drawText(" จ่ายเงินได้ถึงวันที่ : "+formatter.format(booking.getPayDepositDate()), 30, 150, paint);

        canvas.drawText(" ชื่อลูกค้า : "+booking.getTenant().getFname()+" "+booking.getTenant().getLname(), 400, 110, paint);
        canvas.drawText(" อีเมล : "+booking.getTenant().getEmail(), 400, 130, paint);
        canvas.drawText(" เบอร์โทร : "+booking.getTenant().getPhoneNum(), 400, 150, paint);

        canvas.drawText("______________________________________________________________________________________________________________________________________", 0, 180, paint);

        canvas.drawText(" รายการ", 30, 200, paint);
        canvas.drawText(" จำนวน", 150, 200, paint);
        canvas.drawText(" ราคา (มัดจำ)", 350, 200, paint);
        canvas.drawText(" ราคารวม", 430, 200, paint);



        canvas.drawText(" "+booking.getAreadetail().getAreaId(), 30, 250, paint);
        canvas.drawText("1", 150, 250, paint);
        canvas.drawText(" "+booking.getAreadetail().getDeposit(), 350, 250, paint);
        canvas.drawText(" "+booking.getAreadetail().getDeposit()+" บาท ", 430, 250, paint);


        canvas.drawText("______________________________________________________________________________________________________________________________________", 0, 300, paint);

        canvas.drawText("หมายเหตุ ", 30, 320, paint);

        canvas.drawText("โปรดชำระเงินก่อนวันที่ "+formatter.format(booking.getPayDepositDate())+" มิฉะนั้นการจองถือเป็นการยกเลิก", 50, 350, paint);

        canvas.drawText("ช่องทางการชำระเงิน ", 30, 390, paint);
        canvas.drawText("ธนาคารกรุงไทย     012-1-61744-0", 50, 410, paint);
        canvas.drawText("พร้อมเพย์          081-831-5608", 50, 430, paint);

        canvas.drawText("ส่งสำเนา (หลักฐาน) การชำระเงิน มาใน Application เพื่อเป็นการยืนยันการชำระเงิน", 90, 460, paint);





        //canvas.drawt
        // finish the page
        document.finishPage(page);




        // write the document content
        String directory_path = "mnt/sdcard/" ;
        Log.e("Path",""+directory_path);
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"InvoicePdf.pdf";
        File filePath = new File(targetPdf);


        Log.e("Final-Path",""+filePath);


        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
        }
        // close the document
        document.close();


        return filePath;
    }






}
