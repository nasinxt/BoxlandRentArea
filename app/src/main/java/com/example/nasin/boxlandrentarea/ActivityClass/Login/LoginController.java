package com.example.nasin.boxlandrentarea.ActivityClass.Login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginController {



    public ArrayList<Tenant> getLogin(final Tenant tenant){
        final ArrayList<Tenant> Login = new ArrayList<Tenant>();
        Log.e(""+tenant.getUsername(),"");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.e(dataSnapshot.getKey(),"databaseReference : "+dataSnapshot.getChildrenCount() + "");
                if(tenant.getUsername().equals(dataSnapshot.getKey())){
                    Log.e(dataSnapshot.getKey(),"00000000000000000000 : "+dataSnapshot.getChildrenCount() + "");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenant").child(tenant.getUsername());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String username = dataSnapshot.child("username").getValue().toString();
                            String password = dataSnapshot.child("password").getValue().toString();
                            Log.e(dataSnapshot.getKey(),"00000000000000000000 : "+username + "");

                            tenant.setUsername(username);
                            tenant.setPassword(password);
                            Login.add(tenant);


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }



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


        return Login;
    }


}
