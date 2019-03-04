package com.example.nasin.boxlandrentarea.ActivityClass.BrowseRentArea;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BrowseRentAreaController {

    public ArrayList<AreaDetail> findAreaByName(final AreaDetail AreaData) {

        final ArrayList<AreaDetail> areaDetails = new ArrayList<AreaDetail>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(AreaData.getAreazone().getZoneId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final AreaZone areaZone = new AreaZone();
                areaZone.setZoneId(dataSnapshot.child("zoneId").getValue().toString());
                areaZone.setAreaPic(dataSnapshot.child("areaPic").getValue().toString());

                databaseReference.child("AreaDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        int count = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String areaName = snapshot.child("AreaName").getValue(String.class);
                            String deposit = snapshot.child("Deposit").getValue(String.class);
                            String detail = snapshot.child("Detail").getValue(String.class);
                            String price = snapshot.child("RentPrice").getValue(String.class);
                            String size = snapshot.child("Size").getValue(String.class);
                            String status = snapshot.child("Status").getValue(String.class);
                            String zone = snapshot.child("Zone").getValue(String.class);


                                if (areaName.toLowerCase().contains(AreaData.getAreaId().toLowerCase())) {

                                    final AreaDetail areadetail = new AreaDetail();

                                    areadetail.setAreaId(areaName);
                                    areadetail.setStatus(status);
                                    areadetail.setDeposit(Double.parseDouble(deposit));
                                    areadetail.setDetail(detail);
                                    areadetail.setPrice(Double.parseDouble(price));
                                    areadetail.setSize(size);
                                    areadetail.setAreazone(areaZone);
                                    areaDetails.add(areadetail);



                                    count++;



                                }

                                if (count == 5) {
                                    break;
                                }



                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return areaDetails;
    }




    public ArrayList<AreaDetail> findAreaByNameUser(final AreaDetail AreaData) {
        final ArrayList<AreaDetail> areaDetails = new ArrayList<AreaDetail>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(AreaData.getAreazone().getZoneId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final AreaZone areaZone = new AreaZone();
                areaZone.setZoneId(dataSnapshot.child("zoneId").getValue().toString());
                areaZone.setAreaPic(dataSnapshot.child("areaPic").getValue().toString());

                databaseReference.child("AreaDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        int count = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String areaName = snapshot.child("AreaName").getValue(String.class);
                            String deposit = snapshot.child("Deposit").getValue(String.class);
                            String detail = snapshot.child("Detail").getValue(String.class);
                            String price = snapshot.child("RentPrice").getValue(String.class);
                            String size = snapshot.child("Size").getValue(String.class);
                            String status = snapshot.child("Status").getValue(String.class);
                            String zone = snapshot.child("Zone").getValue(String.class);

                            if(status.equals("ว่าง")){

                                if (areaName.toLowerCase().contains(AreaData.getAreaId().toLowerCase())) {


                                    final AreaDetail areadetail = new AreaDetail();


                                    areadetail.setAreaId(areaName);
                                    areadetail.setStatus(status);
                                    areadetail.setDeposit(Double.parseDouble(deposit));
                                    areadetail.setDetail(detail);
                                    areadetail.setPrice(Double.parseDouble(price));
                                    areadetail.setSize(size);
                                    areadetail.setAreazone(areaZone);
                                    areaDetails.add(areadetail);



                                    count++;



                                }

                                if (count == 5) {
                                    break;
                                }

                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return areaDetails;

    }

    public ArrayList<AreaDetail> userSearch(AreaDetail AreaData) {

        final ArrayList<AreaDetail> areaDetails = new ArrayList<AreaDetail>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(AreaData.getAreazone().getZoneId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final AreaZone areaZone = new AreaZone();
                areaZone.setZoneId(dataSnapshot.child("zoneId").getValue().toString());
                areaZone.setAreaPic(dataSnapshot.child("areaPic").getValue().toString());

                databaseReference.child("AreaDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        int count = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String areaName = snapshot.child("AreaName").getValue(String.class);
                            String deposit = snapshot.child("Deposit").getValue(String.class);
                            String detail = snapshot.child("Detail").getValue(String.class);
                            String price = snapshot.child("RentPrice").getValue(String.class);
                            String size = snapshot.child("Size").getValue(String.class);
                            String status = snapshot.child("Status").getValue(String.class);
                            String zone = snapshot.child("Zone").getValue(String.class);

                            if(status.equals("ว่าง")){
                                    final AreaDetail areadetail = new AreaDetail();
                                    areadetail.setAreaId(areaName);
                                    areadetail.setStatus(status);
                                    areadetail.setDeposit(Double.parseDouble(deposit));
                                    areadetail.setDetail(detail);
                                    areadetail.setPrice(Double.parseDouble(price));
                                    areadetail.setSize(size);
                                    areadetail.setAreazone(areaZone);
                                    areaDetails.add(areadetail);


                                    count++;





                                if (count == 5) {
                                    break;
                                }

                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return areaDetails;

    }


    public ArrayList<AreaDetail> findAreaByZone(AreaDetail AreaData) {
        final ArrayList<AreaDetail> areaDetails = new ArrayList<AreaDetail>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AreaZone").child(AreaData.getAreazone().getZoneId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final AreaZone areaZone = new AreaZone();
                areaZone.setZoneId(dataSnapshot.child("zoneId").getValue().toString());
                areaZone.setAreaPic(dataSnapshot.child("areaPic").getValue().toString());

                databaseReference.child("AreaDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        int count = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String areaName = snapshot.child("AreaName").getValue(String.class);
                            String deposit = snapshot.child("Deposit").getValue(String.class);
                            String detail = snapshot.child("Detail").getValue(String.class);
                            String price = snapshot.child("RentPrice").getValue(String.class);
                            String size = snapshot.child("Size").getValue(String.class);
                            String status = snapshot.child("Status").getValue(String.class);




                                    final AreaDetail areadetail = new AreaDetail();


                                    areadetail.setAreaId(areaName);
                                    areadetail.setStatus(status);
                                    areadetail.setDeposit(Double.parseDouble(deposit));
                                    areadetail.setDetail(detail);
                                    areadetail.setPrice(Double.parseDouble(price));
                                    areadetail.setSize(size);
                                    areadetail.setAreazone(areaZone);
                                    areaDetails.add(areadetail);



                                    count++;





                                if (count == 5) {
                                    break;
                                }



                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return areaDetails;

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
                        areaZoneArray.add(areaZone);




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