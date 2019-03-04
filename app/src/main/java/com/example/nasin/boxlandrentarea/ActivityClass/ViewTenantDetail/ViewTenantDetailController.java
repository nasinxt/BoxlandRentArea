package com.example.nasin.boxlandrentarea.ActivityClass.ViewTenantDetail;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewTenantDetailController {

    public ArrayList<Tenant> countListTenant() {
        final ArrayList<Tenant> logins = new ArrayList<Tenant>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Login");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.e(dataSnapshot.getKey(), "Child : " + dataSnapshot.getChildrenCount() + "");

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Login").child(dataSnapshot.getKey());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.child("loginType").getValue().toString().equals("admin")){
                            Tenant login = new Tenant();
                            login.setUsername(dataSnapshot.child("username").getValue().toString());
                            login.setPassword(dataSnapshot.child("password").getValue().toString());
                            logins.add(login);
                            Log.e(dataSnapshot.getKey(), "Username : " + login.getUsername() + "");

                        }

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
        return logins;
    }


    public Tenant findTenant(final Tenant tenantSearch) {
        final  ArrayList<Tenant> LoginString = countListTenant();
        final Tenant tenantClass = new Tenant();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(final Tenant loginclass : LoginString){
                    final int[] count = {0};

                    DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference().child("Login").child(loginclass.getUsername()).child("Tenant");
                    Log.e("TENANT DETAIL",""+databaseReference);


                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.e("Search",""+tenantSearch.getIdcardnum());

                            if(dataSnapshot.child("idcard").getValue().toString().contains(tenantSearch.getIdcardnum())){

                                tenantClass.setFname(dataSnapshot.child("firstname").getValue().toString());
                                tenantClass.setLname(dataSnapshot.child("lastname").getValue().toString());
                                tenantClass.setAddress(dataSnapshot.child("address").getValue().toString());
                                tenantClass.setGender(dataSnapshot.child("gender").getValue().toString());
                                tenantClass.setIdcardnum(dataSnapshot.child("idcard").getValue().toString());
                                tenantClass.setPhoneNum(dataSnapshot.child("phonenumber").getValue().toString());
                                tenantClass.setStorename(dataSnapshot.child("storename").getValue().toString());
                                tenantClass.setEmail(dataSnapshot.child("email").getValue().toString());
                                tenantClass.setStoreDetail(dataSnapshot.child("storedetail").getValue().toString());




                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                }

            }
        }, 1500);

        return tenantClass;

    }


    public Boolean removeTenant(Tenant tenant) {
        Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase table = fb.child("Tenant");
        final Firebase user = table.child(tenant.getUsername());
        user.removeValue();

        return true;

    }


}
