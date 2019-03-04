package com.example.nasin.boxlandrentarea.ActivityClass.EditAreaDetail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nasin.boxlandrentarea.Class.GlobalClass;
import com.example.nasin.boxlandrentarea.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditPicArea extends AppCompatActivity {
    GlobalClass globaldata;
     Uri filePath;
    String filename = null;
    private final int PICK_IMAGE_REQUEST = 71;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pic_area);
        globaldata = (GlobalClass) getApplicationContext();

        progress = ProgressDialog.show(this, "", "กำลังโหลด", true);

        gerPicArea();

    }

    public void gerPicArea(){

        for(final String pathPic : globaldata.getAreaDetailClass().getAreaPic()){
            Log.e("Path",""+pathPic);
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child("Area").child(globaldata.getAreaDetailClass().getAreaId()).child(pathPic);
        Log.e("strorage Path",""+storageReference1);

        storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                LinearLayout linearLayout = findViewById(R.id.listPicture);

                Log.e("Url",""+uri);
                ImageView imageView = new ImageView(EditPicArea.this);
                imageView.setMaxWidth(500);
                imageView.setMaxHeight(200);
                Glide.with(getApplicationContext()).load(uri).into(imageView);
                final TextView textView = new TextView(EditPicArea.this);

                textView.setText(pathPic);
                textView.setTextColor(getApplication().getResources().getColor(R.color.white));

                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditPicArea.this, ""+textView.getText().toString(), Toast.LENGTH_SHORT).show();
                        filename = textView.getText().toString();

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(EditPicArea.this);
                        builder.setMessage("แก้ไขรูปภาพ");
                        builder.setPositiveButton("อัพโหลดรูปใหม่", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progress = ProgressDialog.show(EditPicArea.this, "", "กำลังโหลด", true);

                                chooseImage();

                            }
                        });
                        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        }

        progress.dismiss();

    }


    public void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(EditPicArea.this);


        final Boolean[] check = {null};
        final StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (filePath != null) {

            StorageReference fileToUpload = mStorageRef.child("Area").child(globaldata.getAreaDetailClass().getAreaId()).child(filename);
            fileToUpload.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progress.dismiss();

                            Toast.makeText(EditPicArea.this, "ทำรายการสำเร็จ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(EditPicArea.this,EditPicArea.class));


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.dismiss();
                            Toast.makeText(EditPicArea.this, "ทำรายการไม่สำเร็จโปรดลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show();

                            check[0] = false;
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });

        } else {
            Toast.makeText(EditPicArea.this, "กรุณาอัพโหลดรูปภาพ", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(filePath!=null){
                uploadImage();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}
