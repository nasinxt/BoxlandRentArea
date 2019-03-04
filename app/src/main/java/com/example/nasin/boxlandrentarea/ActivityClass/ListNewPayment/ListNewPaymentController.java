package com.example.nasin.boxlandrentarea.ActivityClass.ListNewPayment;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.Booking;
import com.example.nasin.boxlandrentarea.Class.Receipt;
import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListNewPaymentController {
    DatabaseReference databaseReference;
    ArrayList<Booking> bookings = new ArrayList<Booking>();
    ArrayList<Receipt> receiptArrayList = new ArrayList<Receipt>();




    public ArrayList<Booking> countListBooking() {
        final ArrayList<Booking> bookingArray = new ArrayList<Booking>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String username = dataSnapshot.getKey();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username);
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.e("PathChildrenCount : ",""+dataSnapshot.getKey());
                            if(dataSnapshot.getKey().equals("BookingArea")){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username).child("BookingArea");
                                databaseReference.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        Log.e("PathChildrenCount : ",""+dataSnapshot.getKey());
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username).child("BookingArea").child(dataSnapshot.getKey());
                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.child("bookingStatus").getValue().toString() != null){
                                                    if(dataSnapshot.child("bookingStatus").getValue().toString().equals("ชำระเงินเเล้ว")){
                                                        Booking booking = new Booking();
                                                        Tenant tenant = new Tenant();
                                                        tenant.setUsername(username);

                                                        booking.setBookingId(dataSnapshot.child("bookingId").getValue().toString());
                                                        booking.setTenant(tenant);
                                                        bookingArray.add(booking);

                                                    }
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

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


        return bookingArray;

    }
    public ArrayList<Booking> countListOldBooking() {
        final ArrayList<Booking> bookingArray = new ArrayList<Booking>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String username = dataSnapshot.getKey();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username);
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.e("PathChildrenCount : ",""+dataSnapshot.getKey());
                        if(dataSnapshot.getKey().equals("BookingArea")){
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username).child("BookingArea");
                            databaseReference.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Log.e("PathChildrenCount : ",""+dataSnapshot.getKey());
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username).child("BookingArea").child(dataSnapshot.getKey());
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.child("bookingStatus").getValue().toString() != null){
                                                if(dataSnapshot.child("bookingStatus").getValue().toString().equals("ทำการจองสำเร็จ")){
                                                    Booking booking = new Booking();
                                                    Tenant tenant = new Tenant();
                                                    tenant.setUsername(username);

                                                    booking.setBookingId(dataSnapshot.child("bookingId").getValue().toString());
                                                    booking.setTenant(tenant);
                                                    bookingArray.add(booking);

                                                }
                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

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


        return bookingArray;

    }




    public ArrayList<Receipt> getReceipt() {
        final ArrayList<Receipt> receiptArrayList = new ArrayList<Receipt>();
        final ArrayList<Booking> booking = countListBooking();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(final Booking booking : booking){
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant")
                            .child(booking.getTenant().getUsername()).child("BookingArea").child(booking.getBookingId()).child("Receipt");
                    Log.e("ReceiptId",""+databaseReference);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Receipt receipt = new Receipt();

                            if(!dataSnapshot.child("paymentStatus").getValue().toString().equals("ถูกยกเลิก")){
                                receipt.setReceiptId(dataSnapshot.child("receiptId").getValue().toString());
                                receipt.setPaymentStatus(dataSnapshot.child("paymentStatus").getValue().toString());
                                receipt.setPicPayment(dataSnapshot.child("picPayment").getValue().toString());

                                String PaymentDate = dataSnapshot.child("paymentdate").getValue().toString();
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                try {
                                    java.util.Date date =  format.parse(PaymentDate);
                                    receipt.setPaymentDate(date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Log.e("ReceiptID",""+receipt.getReceiptId());
                                receipt.setBooking(booking);
                                receiptArrayList.add(receipt);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });

                }
            }
        },2500);



        return receiptArrayList;


    }


    public ArrayList<Receipt> getOldReceipt() {
        final ArrayList<Receipt> receiptArrayList = new ArrayList<Receipt>();
        final ArrayList<Booking> booking = countListOldBooking();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(final Booking booking : booking){
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant")
                            .child(booking.getTenant().getUsername()).child("BookingArea").child(booking.getBookingId()).child("Receipt");
                    Log.e("ReceiptId",""+databaseReference);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Receipt receipt = new Receipt();

                            Log.e("Payment Status ",""+dataSnapshot.child("paymentStatus").getValue().toString());
                            if(dataSnapshot.child("paymentStatus").getValue().toString().equals("ชำระเงินสำเร็จ")){

                                receipt.setReceiptId(dataSnapshot.child("receiptId").getValue().toString());
                                receipt.setPaymentStatus(dataSnapshot.child("paymentStatus").getValue().toString());
                                receipt.setPicPayment(dataSnapshot.child("picPayment").getValue().toString());

                                String PaymentDate = dataSnapshot.child("paymentdate").getValue().toString();
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                try {
                                    java.util.Date date =  format.parse(PaymentDate);
                                    receipt.setPaymentDate(date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Log.e("ReceiptID",""+receipt.getReceiptId());
                                receipt.setBooking(booking);
                                receiptArrayList.add(receipt);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });

                }
            }
        },2500);



        return receiptArrayList;


    }




    public AreaDetail getAreaDetail(final AreaDetail AreaDetails){
        final AreaDetail areaDetailClass = new AreaDetail();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaDetail").child(AreaDetails.getAreaId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String areaName = dataSnapshot.child("AreaName").getValue().toString();
                String areaType = dataSnapshot.child("Zone").getValue().toString();
                String areaSize = dataSnapshot.child("Size").getValue().toString();
                String areaDetail = dataSnapshot.child("Detail").getValue().toString();
                String areaDeposit = dataSnapshot.child("Deposit").getValue().toString();
                String areaPrice = dataSnapshot.child("RentPrice").getValue().toString();
                String areaStatus = dataSnapshot.child("Status").getValue().toString();
                areaDetailClass.setAreaId(areaName);
                areaDetailClass.setSize(areaSize);
                areaDetailClass.setDetail(areaDetail);
                areaDetailClass.setDeposit(Double.parseDouble(areaDeposit));
                areaDetailClass.setPrice(Double.parseDouble(areaPrice));
                areaDetailClass.setStatus(areaStatus);



                AreaZone a1 = new AreaZone();
                a1.setZoneId(areaType);
                a1 = getAreaZone(a1);

                areaDetailClass.setAreazone(a1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return areaDetailClass;
    }


    public AreaZone getAreaZone(final AreaZone AreaZone){
        final AreaZone areaZone = new AreaZone();

        DatabaseReference zoneData = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(AreaZone.getZoneId());
        zoneData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                areaZone.setZoneId(dataSnapshot.child("zoneid").getValue().toString());
                areaZone.setAreaPic(dataSnapshot.child("areaPic").getValue().toString());
                areaZone.setAreaType(dataSnapshot.child("areaType").getValue().toString());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return areaZone;
    }







}
