package com.example.nasin.boxlandrentarea.ActivityClass.EditProfile;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileController {
    DatabaseReference databaseReference;


    public Tenant getProfileDetail(Tenant tenantclass){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(tenantclass.getUsername());
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
                tenant.setEmail(dataSnapshot.child("email").getValue().toString());
                tenant.setStoreDetail(dataSnapshot.child("storedetail").getValue().toString());






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return tenant;
    }





    public void editProfile(Tenant tenant) {

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
