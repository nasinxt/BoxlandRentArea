package com.example.nasin.boxlandrentarea.ActivityClass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.nasin.boxlandrentarea.ActivityClass.BrowseRentArea.BrowseRentArea;
import com.example.nasin.boxlandrentarea.ActivityClass.EditProfile.EditProfile;
import com.example.nasin.boxlandrentarea.ActivityClass.ListBookingArea.ListBookingArea;
import com.example.nasin.boxlandrentarea.ActivityClass.Login.Login;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class home extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    Spinner searchType;
    private ArrayList<String> searchTypeString = new ArrayList<String>();
    ViewFlipper slideImg;
    GlobalClass globaldata;


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        slideImg = findViewById(R.id.slideImg);
        firebaseAuth = FirebaseAuth.getInstance();
        globaldata = (GlobalClass) getApplicationContext();

        int images[] = {R.drawable.ad1,R.drawable.ad2,R.drawable.ad3,R.drawable.ad4,R.drawable.ad5};
        for (int img : images){
            slideImg(img);

        }






        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(home.this,home.class));
                        break;

                    case R.id.search:
                        startActivity(new Intent(home.this,BrowseRentArea.class));
                        break;

                    case R.id.account:

                        if(globaldata.getTenantArray().size() == 0 || globaldata.getTenantArray().get(0) == null ){

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(home.this);
                            builder.setMessage("กรุณาเข้าสู่ระบบ");
                            builder.setPositiveButton("เข้าสู่ระบบ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                  startActivity(new Intent(home.this,Login.class));
                                }
                            });
                            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //dialog.dismiss();
                                }
                            });
                            builder.show();


                        }else {
                            startActivity(new Intent(home.this,EditProfile.class));

                        }
                        break;

                    case R.id.booking:
                        if(globaldata.getTenantArray().size() == 0){

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(home.this);
                            builder.setMessage("กรุณาเข้าสู่ระบบ");
                            builder.setPositiveButton("เข้าสู่ระบบ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(home.this,Login.class));

                                }
                            });
                            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //dialog.dismiss();
                                }
                            });
                            builder.show();


                        }else {
                            startActivity(new Intent(home.this,ListBookingArea.class));

                        }
                        break;

                }
                return true;
            }
        });


        LinearLayout howToBtn = findViewById(R.id.howToBtn);
        howToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                LayoutInflater inflater = getLayoutInflater();
                View view1 = inflater.inflate(R.layout.howtobook, null);
                builder.setView(view1);

                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();
            }
        });



        LinearLayout contactBtn = findViewById(R.id.contactBtn);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                LayoutInflater inflater = getLayoutInflater();

                View view1 = inflater.inflate(R.layout.contact, null);
                builder.setView(view1);

                final TextView phonenum = (TextView) view1.findViewById(R.id.contactPhone);
                final TextView facebook = (TextView) view1.findViewById(R.id.contactFacebook);
                final TextView email = (TextView) view1.findViewById(R.id.contactEmail);


                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                phonenum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+phonenum.getText().toString()));
                        startActivity(intent);

                    }
                });

                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(facebook.getText().toString()));
                        startActivity(intent);

                    }
                });

                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, email.getText().toString());

                        startActivity(Intent.createChooser(intent, "Send Email"));
                    }
                });




                builder.show();
            }
        });









    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }


    public void slideImg (int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        slideImg.addView(imageView);
        slideImg.setFlipInterval(3000);
        slideImg.setAutoStart(true);


        slideImg.setInAnimation(this,android.R.anim.slide_out_right);
        slideImg.setOutAnimation(this,android.R.anim.slide_in_left);


    }






}
