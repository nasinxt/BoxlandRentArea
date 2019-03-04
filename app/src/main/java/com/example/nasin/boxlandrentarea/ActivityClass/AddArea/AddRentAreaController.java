package com.example.nasin.boxlandrentarea.ActivityClass.AddArea;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddRentAreaController {


    public void addRentArea(AreaDetail areaDetail) {
        Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");


        Firebase table = fb.child("AreaZone").child(""+areaDetail.getAreazone().getZoneId()).child("AreaDetail");
        final Firebase area = table.child(areaDetail.getAreaId());
        Log.e("PATH",""+area);

        area.child("AreaName").setValue(areaDetail.getAreaId());
        area.child("Size").setValue(areaDetail.getSize());
        area.child("RentPrice").setValue(""+areaDetail.getPrice());
        area.child("Deposit").setValue(""+areaDetail.getDeposit());
        area.child("Detail").setValue(areaDetail.getDetail());
        area.child("Status").setValue("ว่าง");

        Log.e("getAreaPicSize",""+areaDetail.getAreaPic().size());
        for (int i = 0; i < areaDetail.getAreaPic().size(); i++) {
            area.child("Photo").child("Picture" + i).setValue(areaDetail.getAreaPic().get(i));
        }


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
