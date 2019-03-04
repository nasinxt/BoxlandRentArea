package com.example.nasin.boxlandrentarea.ActivityClass.SendReceipt;

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
import com.example.nasin.boxlandrentarea.Class.Receipt;
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

public class SendReceiptController {

    static DatabaseReference databaseReference;

    public ArrayList countListBooking() {
        final ArrayList<String> bookingIdArray = new ArrayList<String>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("BookingArea");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.child("bookingStatus").getValue().toString().equals("ชำระเงินเเล้ว")){
                    bookingIdArray.add(dataSnapshot.child("bookingId").getValue().toString());
                }


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
        return bookingIdArray;
    }

    public File CreateReceipt(Receipt receipt){

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

        canvas.drawText(" หมายเลขใบเสร็จ : "+receipt.getReceiptId(), 30, 110, paint);
        canvas.drawText(" วันที่ : "+formatter.format(receipt.getPaymentDate()), 30, 130, paint);
        canvas.drawText(" หมายเลขใบจอง : "+receipt.getBooking().getBookingId(), 30, 150, paint);

        canvas.drawText(" ชื่อลูกค้า : "+receipt.getBooking().getTenant().getFname()+" "+receipt.getBooking().getTenant().getLname(), 400, 110, paint);
        canvas.drawText(" อีเมล : "+receipt.getBooking().getTenant().getEmail(), 400, 130, paint);
        canvas.drawText(" เบอร์โทร : "+receipt.getBooking().getTenant().getPhoneNum(), 400, 150, paint);

        canvas.drawText("______________________________________________________________________________________________________________________________________", 0, 180, paint);

        canvas.drawText(" รายการ", 30, 200, paint);
        canvas.drawText(" จำนวน", 150, 200, paint);
        canvas.drawText(" ราคา (มัดจำ)", 350, 200, paint);
        canvas.drawText(" ราคารวม", 430, 200, paint);



        canvas.drawText(" "+receipt.getBooking().getAreadetail().getAreaId(), 30, 250, paint);
        canvas.drawText("1", 150, 250, paint);
        canvas.drawText(" "+receipt.getBooking().getAreadetail().getDeposit(), 350, 250, paint);
        canvas.drawText(" "+receipt.getBooking().getAreadetail().getDeposit()+" บาท ", 430, 250, paint);


        canvas.drawText("______________________________________________________________________________________________________________________________________", 0, 300, paint);

        canvas.drawText("หมายเหตุ ", 30, 320, paint);

        canvas.drawText("ลูกค้าสามารถนำใบเสร็จรับเงินเพื่อไปทำสัญญาการจองพื้นที่ต่อไป", 50, 350, paint);









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
        String targetPdf = directory_path+"Receipt.pdf";
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




    public Receipt getReceipt(final Receipt receiptClass) {
        final Receipt receipt = new Receipt();
        final ArrayList<Receipt> receiptArrayList = new ArrayList<Receipt>();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(receiptClass.getBooking().getTenant().getUsername())
                        .child("BookingArea").child(receiptClass.getBooking().getBookingId()).child("Receipt");
                Log.e("ReceiptId",""+databaseReference);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String PaymentDate = "";


                        receipt.setReceiptId(dataSnapshot.child("receiptId").getValue().toString());
                        receipt.setPaymentStatus(dataSnapshot.child("paymentStatus").getValue().toString());
                        receipt.setPicPayment(dataSnapshot.child("picPayment").getValue().toString());

                        PaymentDate = dataSnapshot.child("paymentdate").getValue().toString();




                        final Booking booking = getBookingArea(receiptClass);

                        final Handler handler = new Handler();
                        final String finalPaymentDate = PaymentDate;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Booking bookingData = booking;

                                receipt.setBooking(bookingData);

                                Log.e("DATA BOOKING-ID",""+receipt.getBooking().getBookingId());

                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                try {
                                    java.util.Date date =  format.parse(finalPaymentDate);
                                    receipt.setPaymentDate(date);
                                    Log.e("PaymentDate",""+date);
                                    receiptArrayList.add(receipt);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        },2500);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


            }

        },3000);



        return receipt;


    }




    public Booking getBookingArea(final Receipt receipt) {
        final Booking booking = new Booking();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(receipt.getBooking().getTenant().getUsername()).child("BookingArea");
                databaseReference.child(receipt.getBooking().getBookingId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        booking.setBookingId(dataSnapshot.child("bookingId").getValue().toString());
                        booking.setBookingStatus(dataSnapshot.child("bookingStatus").getValue().toString());

                        String PayDeposit = dataSnapshot.child("payDepositDate").getValue().toString();
                        String BookingDate = dataSnapshot.child("bookingDateTime").getValue().toString();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        try {
                            java.util.Date date =  format.parse(PayDeposit);
                            booking.setPayDepositDate(date);
                            java.util.Date date2 = format.parse(BookingDate);
                            booking.setBookingDateTime(date2);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        final AreaDetail areaDetail = new AreaDetail();
                        final String areaId = dataSnapshot.child("areaId").getValue().toString();
                        areaDetail.setAreaId(areaId);
                        final AreaDetail areaDetail1 = getAreaDetail(areaDetail);

                        final Tenant tenant1 = FindProfileData(receipt.getBooking().getTenant());


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Tenant tenantResult = tenant1;
                                AreaDetail areaDetailResult = areaDetail1 ;

                                booking.setAreadetail(areaDetailResult);
                                booking.setTenant(tenantResult);


                            }
                        },1600);





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });



            }
        },1700);



        return booking;


    }




    public AreaDetail getAreaDetail(final AreaDetail areaDetail){
        final AreaDetail areaDetailClass = new AreaDetail();
        final ArrayList<String> areaPic = countPhotoArea(areaDetail.getAreaId());
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
                        a1 = getAreaZone(selectArea.get(0));

                        areaDetailClass.setAreazone(a1);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                areaDetailClass.getAreaPic().addAll(areaPic);

            }
        }, 2000);

        return areaDetailClass;
    }




    public ArrayList<String> countPhotoArea(final String areaId) {
        final ArrayList<String>  strings = new ArrayList<String>();
        final ArrayList<String> selectArea = selectArea(areaId);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone")
                        .child(selectArea.get(0)).child("AreaDetail").child(selectArea.get(1)).child("Photo");
                Log.e("path",""+databaseReference + "");
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e(dataSnapshot.getKey(),"Child : "+dataSnapshot.getChildrenCount() + "");
                        Log.e("/* StringKey ",""+dataSnapshot.getKey());
                        final String photo = dataSnapshot.getKey();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone")
                                .child(selectArea.get(0)).child("AreaDetail").child(selectArea.get(1)).child("Photo");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                strings.add(dataSnapshot.child(photo).getValue().toString());
                                Log.e("value",""+dataSnapshot.child(photo).getValue().toString());
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

            }
        }, 1000);
        return strings;

    }





    public static AreaZone getAreaZone(final String areaZoneString) {
        final AreaZone areaZone = new AreaZone();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(areaZoneString);
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


    public ArrayList<String> countListTenant() {
        final ArrayList<String> Tenantid = new ArrayList<String>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Login");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(), "Child : " + dataSnapshot.getChildrenCount() + "");
                Tenantid.add(dataSnapshot.getKey());
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
        return Tenantid;
    }


    public Tenant FindProfileData(final Tenant tenants){
        final ArrayList<String> username = countListTenant();
        final Tenant tenant = new Tenant();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(tenants.getUsername());
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
                        tenant.setStorename(dataSnapshot.child("storedetail").getValue().toString());
                        tenant.setUsername(dataSnapshot.child("username").getValue().toString());
                        tenant.setEmail(dataSnapshot.child("email").getValue().toString());










                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        },1400);



        return tenant;
    }



}
