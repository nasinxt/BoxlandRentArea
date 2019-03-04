package com.example.nasin.boxlandrentarea.ActivityClass.BookingArea;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.Booking;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BookingController {
    static DatabaseReference databaseReference;


    public void bookingArea(Booking booking){
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase table = fb.child("Tenant").child(booking.getTenant().getUsername()).child("BookingArea");

        Firebase bookingFireBase = table.child(booking.getBookingId());
        bookingFireBase.child("bookingId").setValue(booking.getBookingId());
        bookingFireBase.child("bookingDateTime").setValue(formatter.format(booking.getBookingDateTime()));
        bookingFireBase.child("bookingStatus").setValue(booking.getBookingStatus());
        bookingFireBase.child("payDepositDate").setValue(formatter.format(booking.getPayDepositDate()));
        bookingFireBase.child("areaId").setValue(booking.getAreadetail().getAreaId());
        updateAreaStatus(booking.getAreadetail());
    }


    private void updateAreaStatus(final AreaDetail AreaDetails){
        Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase table2 = fb.child("AreaZone").child(AreaDetails.getAreazone().getZoneId()).child("AreaDetail");
        Firebase EditStatus = table2.child(AreaDetails.getAreaId());
        EditStatus.child("Status").setValue("ถูกจองแล้ว");
    }


    public int[] countListBooking() {
        final int[] ChildrenCount = {0};

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
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username).child("BookingArea");
                            databaseReference.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    Log.e("PathChildrenCountS : ",""+dataSnapshot.getKey());
                                    ChildrenCount[0]++;
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


        return ChildrenCount;

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





    public static AreaZone getAreaZone(String zoneID) {
        final AreaZone areaZone = new AreaZone();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(zoneID);
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

    public Tenant FindProfileData(Tenant login){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(login.getUsername());
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
                tenant.setEmail(dataSnapshot.child("email").getValue().toString());
                tenant.setStoreDetail(dataSnapshot.child("storedetail").getValue().toString());
                tenant.setUsername(dataSnapshot.child("username").getValue().toString());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return tenant;
    }



    public Booking getAutoBookingId(final String bookingId) {
        final Booking booking = new Booking();
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
                                            if(dataSnapshot.child("bookingId").getValue().toString().equals(bookingId.toLowerCase())){
                                                Tenant tenant = new Tenant();
                                                tenant.setUsername(username);
                                                booking.setBookingId(dataSnapshot.child("bookingId").getValue().toString());
                                                booking.setTenant(tenant);

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


        return booking;

    }

    public Booking AutoCancelBooking(final String bookingId,String username,String AreaId,String ZoneId) {
        final Booking booking = new Booking();

        Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase table = fb.child("Tenant").child(username).child("BookingArea");
        final Firebase firebaseBooking  = table.child(bookingId);
        Log.e("AutoCancel-Path"," "+firebaseBooking);

        Firebase fb2 = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase table2 = fb2.child("AreaZone").child(ZoneId).child("AreaDetail");
        final Firebase areaDetail  = table2.child(AreaId);
        Log.e("AutoCancelArea-Path"," "+areaDetail);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(username).child("BookingArea").child(bookingId);
        Log.e("databaseReference",""+databaseReference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if( dataSnapshot.child("bookingStatus").getValue().toString().equals("รอการชำระเงิน")){
                    areaDetail.child("Status").setValue("ว่าง");
                    firebaseBooking.child("bookingStatus").setValue("ถูกยกเลิก");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return booking;


    }



}
