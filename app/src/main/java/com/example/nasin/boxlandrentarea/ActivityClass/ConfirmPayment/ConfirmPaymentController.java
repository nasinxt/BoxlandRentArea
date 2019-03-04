package com.example.nasin.boxlandrentarea.ActivityClass.ConfirmPayment;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.Booking;
import com.example.nasin.boxlandrentarea.Class.Receipt;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.firebase.client.Firebase;
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
import java.util.Date;

public class ConfirmPaymentController {
    static DatabaseReference databaseReference;
    StorageReference mStorageRef;



    public Booking getBookingArea(final Booking Booking) {
        final Booking booking = new Booking();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(Booking.getTenant().getUsername()).child("BookingArea");
                databaseReference.child(Booking.getBookingId()).addValueEventListener(new ValueEventListener() {
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
                        final Tenant tenant = FindProfileData(Booking.getTenant());


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AreaDetail areaDetailResult = areaDetail1 ;

                                booking.setAreadetail(areaDetailResult);
                                booking.setTenant(tenant);


                            }
                        }, 1500);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


            }
        }, 1700);


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
        }, 2000);

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

    public Tenant FindProfileData(final Tenant tenants) {
        final Tenant tenant = new Tenant();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(tenants.getUsername() != null){
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(tenants.getUsername());
                    Log.e("IF IDCARD", "" + databaseReference);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e("IF IDCARD", "" + tenants.getIdcardnum());

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

                }

        }, 1000);


        return tenant;
    }




    public ArrayList<String> countPayment() {
        final int[] ChildrenCount = {0};
        final ArrayList<String> bookingIdArray = new ArrayList<String>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("PathChildrenCount : ",""+dataSnapshot.getKey());
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
                                                    if(!dataSnapshot.child("bookingStatus").getValue().toString().equals("รอการชำระเงิน")){
                                                        String bookingId = dataSnapshot.child("bookingId").getValue().toString();
                                                        bookingIdArray.add(bookingId);
                                                        Log.e("CountBooking",""+ChildrenCount[0]);
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


        return bookingIdArray;

    }






    public void addRecipt(final Receipt receipt) {
        final Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");
        final Firebase table = fb.child("Tenant").child(receipt.getBooking().getTenant().getUsername()).child("BookingArea");

        mStorageRef = FirebaseStorage.getInstance().getReference();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Firebase Recipt = null;
                Recipt = table.child(receipt.getBooking().getBookingId()).child("Receipt");

                final Handler handler = new Handler();
                final Firebase finalRecipt = Recipt;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                        finalRecipt.child("receiptId").setValue(receipt.getReceiptId());
                        finalRecipt.child("paymentdate").setValue(formatter.format(receipt.getPaymentDate()));
                        finalRecipt.child("paymentStatus").setValue(receipt.getPaymentStatus());
                        finalRecipt.child("picPayment").setValue(receipt.getPicPayment());
                        finalRecipt.child("bookingId").setValue(receipt.getBooking().getBookingId());

                        Firebase BookingArea = fb.child("Tenant").child(receipt.getBooking().getTenant().getUsername()).child("BookingArea").child(receipt.getBooking().getBookingId());
                        BookingArea.child("bookingStatus").setValue("ชำระเงินเเล้ว");

                    }
                }, 1500);



            }
        }, 1800);
    }



    public Booking cancelBooking(final Booking bookings) {
        final Booking booking = new Booking();

        Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase table = fb.child("Tenant").child(bookings.getTenant().getUsername()).child("BookingArea");
        Firebase firebaseBooking  = table.child(bookings.getBookingId());
        Log.e("Cancel-Path"," "+firebaseBooking);
        firebaseBooking.child("bookingStatus").setValue("ถูกยกเลิก");

        Firebase fb2 = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase table2 = fb2.child("AreaZone").child(bookings.getAreadetail().getAreazone().getZoneId()).child("AreaDetail");
        Firebase AreaDetail  = table2.child(bookings.getAreadetail().getAreaId());
        Log.e("CancelArea"," "+AreaDetail);

        AreaDetail.child("Status").setValue("ว่าง");


        return booking;


    }



}
