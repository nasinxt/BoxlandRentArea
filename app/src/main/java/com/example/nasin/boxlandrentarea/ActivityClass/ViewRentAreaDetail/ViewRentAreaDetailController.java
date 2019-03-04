package com.example.nasin.boxlandrentarea.ActivityClass.ViewRentAreaDetail;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class ViewRentAreaDetailController {


    public AreaDetail findAreaDetail(final AreaDetail areaDetail){
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
                a1 = getAreaZone(areaDetail);

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


}
