package com.example.nasin.boxlandrentarea.ActivityClass.ListBookingArea;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListBookingController {

    DatabaseReference databaseReference;
    ArrayList<Booking> bookingArrayList = new ArrayList<Booking>();

    public ArrayList<String> countListBooking(final Tenant login) {
        final int[] ChildrenCount = {0};
        final ArrayList<String> bookingIdArray = new ArrayList<String>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("PathChildrenCount : ",""+dataSnapshot.getKey());
                final String username = dataSnapshot.getKey();
                if(login.getUsername().toLowerCase().equals(username.toLowerCase())){ // Start if

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
                                                String bookingId = dataSnapshot.child("bookingId").getValue().toString();
                                                bookingIdArray.add(bookingId);
                                                Log.e("CountBooking",""+ChildrenCount[0]);
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

            } // endif

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


    public ArrayList<Booking> getBookingArea(final Tenant login) {
        bookingArrayList.clear();
        final ArrayList<String> bookingIdArray = countListBooking(login);
        Log.e("bookingIdArray",""+bookingIdArray.size());
        final Tenant tenant = findProfileData(login);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(String bookingId : bookingIdArray){
                    final Booking booking = new Booking();

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(login.getUsername()).child("BookingArea");
                    databaseReference.child(bookingId).addValueEventListener(new ValueEventListener() {
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


                                // get AreaDetail

                                final AreaDetail areaDetail = new AreaDetail();
                                final String areaId = dataSnapshot.child("areaId").getValue().toString();
                                areaDetail.setAreaId(areaId);
                                final AreaDetail areaDetail1 = getAreaDetail(areaDetail);

                                // get Tenant

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AreaDetail areaDetailResult = areaDetail1 ;

                                        booking.setAreadetail(areaDetailResult);
                                        booking.setTenant(tenant);


                                    }
                                },1600);



                            Log.e("BEFORE Data",""+booking.getBookingId());
                            bookingArrayList.add(booking);
                            Log.e("After Data",""+bookingArrayList.get(bookingArrayList.size()-1).getBookingId());



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });


                }
            }
        },1700);



        return bookingArrayList;


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



    public Tenant findProfileData(Tenant login){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(login.getUsername());
        Log.e("TENANT DETAIL",""+databaseReference);
        final Tenant tenant = new Tenant();

        //Toast.makeText(globaldata, ""+databaseReference.toString(), Toast.LENGTH_LONG).show();

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
                tenant.setStoreDetail(dataSnapshot.child("storedetail").getValue().toString());





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return tenant;
    }


    public static ArrayList<AreaZone> getAreaZone() {
        final ArrayList<AreaZone> areaZoneArray = new ArrayList<AreaZone>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),"Child : "+dataSnapshot.getChildrenCount() + "");

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(dataSnapshot.getKey());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final AreaZone areaZone = new AreaZone();
                        areaZone.setAreaPic(dataSnapshot.child("areaPic").getValue().toString());
                        areaZone.setZoneId(dataSnapshot.child("zoneId").getValue().toString());
                        areaZone.setAreaType(dataSnapshot.child("areaType").getValue().toString());


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

        return areaZoneArray;
    }










}
