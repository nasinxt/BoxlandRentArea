package com.example.nasin.boxlandrentarea.ActivityClass.ViewRentAreaDetail;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nasin.boxlandrentarea.ActivityClass.ViewAreaDetail.ViewAreaDetailController;
import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ViewRentAreaDetail extends AppCompatActivity {

    TextView areaStatusView,areaPriceView,areaDepositView,areaDetailView,areaSizeView,areaTypeView,areaNameView;
    ImageView areaViewPic;
    RelativeLayout nextPic;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Uri IMAGES = null;
    private ArrayList<Uri> ImagesArray = new ArrayList<Uri>();
    //private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();



    GlobalClass globaldata;
    int countPic = 0;;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rent_area_detail);
        globaldata = (GlobalClass) getApplicationContext();

        areaNameView = findViewById(R.id.areaNameView);
        areaTypeView  = findViewById(R.id.areaTypeView);
        areaSizeView = findViewById(R.id.areaSizeView);
        areaDetailView = findViewById(R.id.areaDetailView);
        areaDepositView = findViewById(R.id.areaDepositView);
        areaPriceView = findViewById(R.id.areaPriceView);
        areaStatusView = findViewById(R.id.areaStatusView);

        // areaViewPic = (ImageView) findViewById(R.id.areaViewPic);
        // nextPic = (RelativeLayout) findViewById(R.id.nextPic);

        progress = ProgressDialog.show(this, "",
                "กำลังโหลด", true);
        findRentAreaDetail();




       



    }

    

    @Override
    public void onResume(){
        super.onResume();


    }

    public void findRentAreaDetail(){

        final AreaDetail areaDetail = new ViewRentAreaDetailController().findAreaDetail(globaldata.getAreadetail().get(0));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                globaldata.getAreadetail().clear();
                globaldata.getAreadetail().add(globaldata.getAreaDetailClass());
                areaNameView.setText(areaDetail.getAreaId());
                areaTypeView.setText(areaDetail.getAreazone().getAreaType());
                areaSizeView.setText(areaDetail.getSize()+" ตรม.");
                areaDetailView.setText(areaDetail.getDetail());
                areaDepositView.setText(areaDetail.getDeposit()+" บาท");
                areaPriceView.setText(areaDetail.getPrice()+" บาท");
                areaStatusView.setText(areaDetail.getStatus());

                Log.e("Pic-Size",""+areaDetail.getAreaPic().size());

                if(areaDetail.getAreaPic().size()>0){
                    for (String areaPic : areaDetail.getAreaPic()){
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Area").child(areaDetail.getAreaId()).child(areaPic);
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



    private void init() {


        mPager = (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(new SlidingImage_Adapter(ViewRentAreaDetail.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

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


public class   SlidingImage_Adapter extends PagerAdapter {


        private ArrayList<Uri> IMAGES;
        private LayoutInflater inflater;
        private Context context;


    public SlidingImage_Adapter(Context context, ArrayList<Uri> IMAGES) {
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

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewRentAreaDetail.this);
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


