package com.example.nasin.boxlandrentarea.ActivityClass.BookingArea;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nasin.boxlandrentarea.ActivityClass.SaveInvoice.SaveInvoice;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewRentAreaDetail.ViewRentAreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.Booking;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.Class.Tenant;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BookingArea extends AppCompatActivity {
    TextView areaStatusView,areaPriceView,areaDepositView,areaDetailView,areaSizeView,areaTypeView,areaNameView;
    TextView tenantNameInv,tenantPhoneInv,tenantEmailInv,payDateInv;
    DatabaseReference databaseReference;
    GlobalClass globaldata;
    RelativeLayout nextPic;
    int countPic=0;
    ImageView areaViewPic;
    ProgressDialog progress;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Uri IMAGES = null;
    private ArrayList<Uri> ImagesArray = new ArrayList<Uri>();
    //private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_area);
        Firebase.setAndroidContext(this);
        globaldata = (GlobalClass) getApplicationContext();



        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        Button bookingBtn = findViewById(R.id.bookingBtn);
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(globaldata.getTenantArray().size()>0){
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BookingArea.this);
                    builder.setMessage("คุณเเน่ใจว่าจะจองหรือไม่");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            progress = ProgressDialog.show(BookingArea.this, "", "กำลังโหลด", true);
                            addBookingArea();
                        }
                    });
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.show();
                }else {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BookingArea.this);
                    builder.setMessage("ต้องทำการเข้าสู่ระบบก่อน");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            progress = ProgressDialog.show(BookingArea.this, "", "กำลังโหลด", true);
                            addBookingArea();
                        }
                    });
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.show();
                }





            }
        });



        areaNameView = findViewById(R.id.areaNameBooking);
        areaTypeView = findViewById(R.id.areaTypeBooking);
        areaSizeView = findViewById(R.id.areaSizeBooking);
        areaDetailView = findViewById(R.id.areaDetailBooking);
        areaDepositView = findViewById(R.id.areaDepositBooking);
        areaPriceView = findViewById(R.id.areaPriceBooking);
        areaStatusView =findViewById(R.id.areaStatusBooking);



        getAreaDetail();

        globaldata = (GlobalClass) getApplicationContext();
        /*

         */


    }











    @Override
    public void onResume(){
        super.onResume();

    }

    public  void  getAreaDetail(){
        final AreaDetail[] areaDetail = {new AreaDetail()};
        areaDetail[0].setAreaId(globaldata.getAreaID());
        Log.e("BOOKING-ID",""+globaldata.getAreaID());

        globaldata = (GlobalClass) getApplicationContext();
        BookingController bookingController = new BookingController();
        areaDetail[0] = bookingController.getAreaDetail(areaDetail[0]);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                globaldata.getAreadetail().clear();
                globaldata.getAreadetail().add(areaDetail[0]);
                Log.e("Size : ",""+globaldata.getAreadetail().size());

                areaNameView.setText(globaldata.getAreadetail().get(0).getAreaId());
                areaTypeView.setText(globaldata.getAreadetail().get(0).getAreazone().getZoneId());
                areaSizeView.setText(globaldata.getAreadetail().get(0).getSize()+" ตรม.");
                areaDetailView.setText(globaldata.getAreadetail().get(0).getDetail());
                areaDepositView.setText(""+globaldata.getAreadetail().get(0).getDeposit()+" บาท");
                areaPriceView.setText(""+ globaldata.getAreadetail().get(0).getPrice()+" บาท");
                areaStatusView.setText(globaldata.getAreadetail().get(0).getStatus());


                Log.e("Link",""+FirebaseStorage.getInstance().getReference().child("Area").child(globaldata.getAreadetail().get(0).getAreaId()).child(globaldata.getAreadetail().get(0).getAreaPic().get(countPic).toString()));

                if(globaldata.getAreadetail().size()>0){
                    for (String areaPic : globaldata.getAreadetail().get(0).getAreaPic()){
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Area").child(globaldata.getAreadetail().get(0).getAreaId()).child(areaPic);
                        Log.e("strorage Path",""+storageReference);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                Log.e("Url",""+uri);
                                countPic = 0;
                                ImagesArray.add(uri);



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                }


                progress.dismiss();
            }
        },3500);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                init();
            }
        },6000);

    }

    public void addBookingArea(){
        final BookingController bookingController = new BookingController();
        final Tenant tenant = bookingController.FindProfileData(globaldata.getTenantArray().get(0));
        final int[] BookingIdFinal = bookingController.countListBooking();


        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        final Date date = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        final Date currentdate = date;


        final Date nextdate = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextdate);
        calendar.add(Calendar.DATE, 5); // add days


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("BookingID",""+BookingIdFinal[0]);

                Booking booking = new Booking();
                if(BookingIdFinal[0]<1){
                    booking.setBookingId("Booking-1001");

                }else {
                    String bookingStr = "Booking-"+(1001+BookingIdFinal[0]);
                    booking.setBookingId(bookingStr);

                }
                booking.setAreadetail(globaldata.getAreadetail().get(0));
                booking.setTenant(tenant);
                booking.setBookingDateTime(currentdate);
                booking.setPayDepositDate(calendar.getTime());
                booking.setBookingStatus("รอการชำระเงิน");
                BookingController bookingController = new BookingController();
                bookingController.bookingArea(booking);

                globaldata.getBookingArray().clear();
                globaldata.getBookingArray().add(booking);

                //Auto Cancel
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent notificationIntent = new Intent(BookingArea.this, AutoCancelReceiver.class);
                notificationIntent.putExtra("BookingId",booking.getBookingId());
                notificationIntent.putExtra("Username",booking.getTenant().getUsername());
                notificationIntent.putExtra("AreaId",booking.getAreadetail().getAreaId());
                notificationIntent.putExtra("ZoneId",booking.getAreadetail().getAreazone().getZoneId());
                PendingIntent broadcast = PendingIntent.getBroadcast(BookingArea.this, 100+BookingIdFinal[0], notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 300);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);


                //Go to
                startActivity(new Intent(BookingArea.this,SaveInvoice.class));





                progress.dismiss();
            }
        },2000);


    }



    private void init() {
        mPager = (ViewPager) findViewById(R.id.pagerBooking);
        mPager.setAdapter(new SlidingImage_AdapterBooking(BookingArea.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicatorBooking);

        indicator.setViewPager(mPager);




        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);
        Log.e("NUM_PAGES",""+ImagesArray.size());



        NUM_PAGES =ImagesArray.size();



        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }



    public class SlidingImage_AdapterBooking extends PagerAdapter {
        private ArrayList<Uri> IMAGES;
        private LayoutInflater inflater;
        private Context context;


        public SlidingImage_AdapterBooking(Context context, ArrayList<Uri> IMAGES) {
            this.context = context;
            this.IMAGES=IMAGES;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return IMAGES.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.image);


            Picasso.get().load(IMAGES.get(position)).into(imageView);
            Log.e("IMAGE",""+IMAGES.get(position));


            view.addView(imageLayout, 0);

            imageLayout.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BookingArea.this);
                    final View mView = getLayoutInflater().inflate(R.layout.dialog_zoom_image, null);
                    final PhotoView photoView  = mView.findViewById(R.id.imageViewCenterCrop);
                    //Glide.with(getApplicationContext()).load(mapUri[0]).into(photoView);

                    Picasso.get().load(IMAGES.get(position)).into(photoView);
                    mBuilder.setView(mView);
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();

                    Log.e("TAG", "This page was clicked: " + position);
                }
            });

            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }





}

