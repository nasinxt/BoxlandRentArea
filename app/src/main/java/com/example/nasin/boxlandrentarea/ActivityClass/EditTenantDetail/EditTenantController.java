package com.example.nasin.boxlandrentarea.ActivityClass.EditTenantDetail;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditTenantController {
    DatabaseReference databaseReference;




    public Tenant findTenant(final Tenant tenantSearch) {
        final Tenant tenantClass = new Tenant();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    final int[] count = {0};

                DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(tenantSearch.getUsername());
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
                                    tenantClass.setUsername(dataSnapshot.child("username").getValue().toString());



                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }




        }, 1500);

        return tenantClass;

    }



    public void editTenantDetail(Tenant tenant) {
        Log.e("User",""+tenant.getUsername());

        Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase userderail = fb.child("Tenant").child(tenant.getUsername());


        userderail.child("idcard").setValue(tenant.getIdcardnum());
        userderail.child("firstname").setValue(tenant.getFname());
        userderail.child("lastname").setValue(tenant.getLname());
        userderail.child("gender").setValue(tenant.getGender());
        userderail.child("storename").setValue(tenant.getStorename());
        userderail.child("phonenumber").setValue(tenant.getPhoneNum());
        userderail.child("address").setValue(tenant.getAddress());
        userderail.child("email").setValue(tenant.getEmail());
        userderail.child("storedetail").setValue(""+tenant.getStoreDetail());



    }


}
