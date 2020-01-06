package com.example.loginandsearchcustomer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomerActivity extends AppCompatActivity {




    //    Button mCaptureButton;
    ImageButton mImageView;
    Button mBtnSearch;
    ScrollView mScrollView;

    final int kodeKamera=99;
    final int kodeGaleri=100;
    Uri imageUri;

    Integer REQUEST_CAMERA= 1, SELECT_FILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

//        mCaptureButton = (Button) findViewById(R.id.id_btn_image);
        mImageView = (ImageButton) findViewById(R.id.image_view);
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mScrollView = (ScrollView) findViewById(R.id.id_scroll);
        mScrollView.setVisibility(View.INVISIBLE);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mBtnSearch.isClickable();
                if (checked) {
                    mScrollView.setVisibility(View.VISIBLE);
                } else {
                    mScrollView.setVisibility(View.INVISIBLE);

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_OK){
            if (requestCode==REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap)bundle.get("data");
                mImageView.setImageBitmap(bitmap);

                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getFilesDir();
                // Create imageDir
                File mypath=new File(directory,"profile.jpg");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    fos = openFileOutput("profile.jpg", Context.MODE_PRIVATE);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("YourTag", directory.getAbsolutePath());
            }else if (requestCode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                mImageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void SelectImage(){
        final  CharSequence[] items = {"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CAMERA);

                }else if (items[i].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"),SELECT_FILE);

                }else if (items[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
}
