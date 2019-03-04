package com.example.nasin.boxlandrentarea.ActivityClass.AddArea;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nasin.boxlandrentarea.ActivityClass.Login.Login;
import com.example.nasin.boxlandrentarea.ActivityClass.adminHome;
import com.example.nasin.boxlandrentarea.ActivityClass.home;
import com.example.nasin.boxlandrentarea.Class.AreaDetail;
import com.example.nasin.boxlandrentarea.Class.AreaZone;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddRentArea extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    EditText areaname;
    Spinner areazone;
    EditText areasize;
    EditText arearent;
    EditText deposit;
    EditText detail;
    Spinner spinstoretypeE;
    ArrayList<String> areazoneList = new ArrayList<String>();

    private ArrayList<Uri> filePath = new ArrayList<Uri>();
    int totalItemsSelected;
    GlobalClass globaldata;
    private List<String> fileNameList;
    private List<String> fileDoneList;
    private StorageReference mStorage;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rent_area);
        Firebase.setAndroidContext(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        globaldata = (GlobalClass) getApplicationContext();

        areaname = (EditText) findViewById(R.id.areaname);
        areazone = (Spinner) findViewById(R.id.areazone);
        areasize = (EditText) findViewById(R.id.areasize);
        arearent = (EditText) findViewById(R.id.arearent);
        deposit = (EditText) findViewById(R.id.deposit);
        detail = (EditText) findViewById(R.id.detail);
        spinstoretypeE = (Spinner) findViewById(R.id.areazone);

        progress = ProgressDialog.show(AddRentArea.this, "", "กำลังโหลด", true);


        addAreaType();

        ArrayAdapter<String> StringZone = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, areazoneList);
        areazone.setAdapter(StringZone);


        Button addArea = (Button) findViewById(R.id.addArea);
        addArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = ProgressDialog.show(AddRentArea.this, "", "กำลังโหลด", true);

                if(areaname.getText().toString().isEmpty()){
                    progress.dismiss();
                    Toast.makeText(AddRentArea.this, "กรุณากรอกชื่อพื้นที่", Toast.LENGTH_SHORT).show();
                }else if(deposit.getText().toString().isEmpty()){
                    progress.dismiss();
                    Toast.makeText(AddRentArea.this, "กรุณากรอกค่ามัดจำ", Toast.LENGTH_SHORT).show();

                }else if(arearent.getText().toString().isEmpty()){
                    progress.dismiss();
                    Toast.makeText(AddRentArea.this, "กรุณากรอกอัตราการเช่า", Toast.LENGTH_SHORT).show();

                }else if(detail.getText().toString().isEmpty()){
                    progress.dismiss();
                    Toast.makeText(AddRentArea.this, "กรุณากรอกรายละเอียด", Toast.LENGTH_SHORT).show();

                }else if(areasize.getText().toString().isEmpty()){
                    progress.dismiss();
                    Toast.makeText(AddRentArea.this, "กรุณากรอกขนาดพื้นที่", Toast.LENGTH_SHORT).show();
                }else if(totalItemsSelected == 0){
                    progress.dismiss();
                    Toast.makeText(AddRentArea.this, "กรุณากรอกขนาดพื้นที่", Toast.LENGTH_SHORT).show();
                }else {
                    for(AreaZone areaZone : globaldata.getAreaZone()){
                        if(areaZone.getAreaType().equals(spinstoretypeE.getSelectedItem().toString())){
                            Toast.makeText(AddRentArea.this, "", Toast.LENGTH_SHORT).show();
                            AreaDetail areaDetail = new AreaDetail();
                            areaDetail.setAreazone(areaZone);
                            areaDetail.setAreaId(areaname.getText().toString());
                            areaDetail.setDeposit(Double.parseDouble(deposit.getText().toString()));
                            areaDetail.setPrice(Double.parseDouble(arearent.getText().toString()));
                            areaDetail.setDetail(detail.getText().toString());
                            areaDetail.setSize(areasize.getText().toString());
                            areaDetail.setStatus("ว่าง");


                            uploadpic(areaDetail);


                        }

                    }

                }




            }
        });

        Button selectpic = (Button) findViewById(R.id.selectpic);
        selectpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileNameList = new ArrayList<>();
                fileDoneList = new ArrayList<>();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

                Toast.makeText(AddRentArea.this, "Select", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    filePath.add(fileUri);
                }


            } else  {
                Toast.makeText(this, "กรุณาเลือกรูปภาพมากกว่า 1 รูป", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



    private void addAreaType() {
        globaldata.getAreaZone().clear();
        final ArrayList<String> storetype = new ArrayList<String>();
        final ArrayList<AreaZone> areaZone = AddRentAreaController.getAreaZone();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("Size",""+areaZone.size());


                for (AreaZone areaZone : areaZone){
                    storetype.add(areaZone.getAreaType());
                    Log.e("Size",""+areaZone.getAreaType());

                    globaldata.getAreaZone().add(areaZone);

                }

                spinstoretypeE = (Spinner) findViewById(R.id.areazone);
                ArrayAdapter<String> StringStoreType = new ArrayAdapter<String>(AddRentArea.this,
                        android.R.layout.simple_dropdown_item_1line,storetype);
                spinstoretypeE.setAdapter(StringStoreType);


                progress.dismiss();
            }
        },2500);

    }




    public void uploadpic(final AreaDetail areaDetail) {

        final int sizePicture = totalItemsSelected;

        for (int i = 0; i < totalItemsSelected; i++) {

            Log.e("totalItemsSelected",""+totalItemsSelected);
            String fileName = getFileName(filePath.get(i));

            StorageReference fileToUpload = mStorage.child("Area").child(areaname.getText().toString()).child(areaname.getText().toString() + "-" + i);

            final int finalI = i;
            fileToUpload.putFile(filePath.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileDoneList.add(finalI, "อัพโหลดสำเร็จ");
                    areaDetail.getAreaPic().add(areaname.getText().toString() + "-" + finalI);
                    Log.e("PICTURE",""+areaname.getText().toString() + "-" + finalI);

                    if((finalI+1) == sizePicture){
                        Log.e("PicSize"," "+sizePicture+" == "+areaDetail.getAreaPic().size());
                        AddRentAreaController addController = new AddRentAreaController();
                        addController.addRentArea(areaDetail);

                        Toast.makeText(AddRentArea.this, "บันทึกสำเร็จ", Toast.LENGTH_SHORT).show();
                        progress.dismiss();

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(AddRentArea.this);
                        builder.setMessage("กลับสู่หน้าหลัก");
                        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(AddRentArea.this,adminHome.class));

                            }
                        });
                        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();

                    }

                }
            });


        }



    }

}