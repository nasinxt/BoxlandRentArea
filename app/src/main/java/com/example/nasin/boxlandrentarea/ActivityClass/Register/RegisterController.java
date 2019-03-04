package com.example.nasin.boxlandrentarea.ActivityClass.Register;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterController {

    public ArrayList<String> countTenant(final Tenant tenant){
        final ArrayList<String> strings = new ArrayList<String>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("Key",""+dataSnapshot.getKey());
                if(dataSnapshot.getKey().equals(tenant.getUsername())){
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
        return strings;
    }



    public void addTenant(final Tenant tenant){
        final int check = countTenant(tenant).size();



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.e("Key",""+ check);


                    Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");

                    Firebase user = fb.child("Tenant");
                    Firebase userderail = user.child(tenant.getUsername());


                    userderail.child("username").setValue(tenant.getUsername());
                    userderail.child("password").setValue(tenant.getPassword());
                    userderail.child("idcard").setValue(tenant.getIdcardnum());
                    userderail.child("firstname").setValue(tenant.getFname());
                    userderail.child("lastname").setValue(tenant.getLname());
                    userderail.child("gender").setValue(tenant.getGender());
                    userderail.child("storename").setValue(tenant.getStorename());
                    userderail.child("storedetail").setValue(tenant.getStoreDetail());
                    userderail.child("phonenumber").setValue(tenant.getPhoneNum());
                    userderail.child("address").setValue(tenant.getAddress());
                    userderail.child("email").setValue(tenant.getEmail());



                }
            },2000);




    }



}
