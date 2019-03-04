package com.example.nasin.boxlandrentarea.ActivityClass.ListTenant;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListTenantController {

    public ArrayList<String> countListTenant() {
        final ArrayList<String> Tenantid = new ArrayList<String>();


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("PathChildrenCount : ",""+dataSnapshot.getKey());
                final String username = dataSnapshot.getKey();
                Tenantid.add(username);



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

        return Tenantid;
    }






    public ArrayList<Tenant> findTenant(final String searchString) {
        final  ArrayList<String> LoginString = countListTenant();
        final  ArrayList<Tenant> tenantArray = new ArrayList<Tenant>();
        Log.e("Size Login"," "+LoginString);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

        for(final String tenant : LoginString){
            final int[] count = {0};

            if(!tenant.equals("admin")){

              DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(tenant);


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Tenant tenantClass = new Tenant();

                        Log.e("Search",""+searchString+" "+dataSnapshot.child("firstname").getValue().toString());

                        if(dataSnapshot.child("firstname").getValue().toString().contains(searchString.toLowerCase())){

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

                        tenantArray.add(tenantClass);





                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                if (count[0] == 5) {
                    break;
                }
            }

        }

            }
        }, 1500);

        return tenantArray;

    }


    public ArrayList<Tenant> findAllTenant() {
        final  ArrayList<String> LoginString = countListTenant();
        final  ArrayList<Tenant> tenantArray = new ArrayList<Tenant>();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(final String tenant : LoginString){
                    if(!tenant.equals("admin")){
                        final int[] count = {0};

                        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(tenant);
                        Log.e("TENANT DETAIL",""+databaseReference);


                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final Tenant tenantClass = new Tenant();

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


                                tenantArray.add(tenantClass);



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        if (count[0] == 5) {
                            break;
                        }

                    }

                }

            }
        }, 1500);

        return tenantArray;

    }

    public Boolean removeTenant(Tenant tenant) {
        Firebase fb = new Firebase("https://boxland-dd894.firebaseio.com/");
        Firebase table = fb.child("Tenant");
        final Firebase user = table.child(tenant.getUsername());
        user.removeValue();

        return true;

    }




}
