package com.example.splash2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;


public class UploadFragment extends Fragment  {

    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final Object TAG = 78;
    ImageView mImageView;
    Button gal;
    Button cam;
    Button iden;
    Button disea;
    String path="";

    private static final int CAM_REQUEST = 1313;

    private static final int IMAG_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    Uri image_uri;
    int k = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload,container,false) ;
        mImageView = view.findViewById(R.id.post_image);
        gal = view.findViewById(R.id.gallery);
        cam = view.findViewById(R.id.camera);
        iden = view.findViewById(R.id.leafClass);
        disea = view.findViewById(R.id.disidentify);

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k=2;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){


                    if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){

                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,};

                        requestPermissions(permissions,PERMISSION_CODE);

                    }
                    else{

                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();

                }


            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k=1;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

                        requestPermissions(permissions, PERMISSION_CODE);

                    } else {

                        openCamera();
                    }
                } else {
                    openCamera();

                }
            }


        });

       /* iden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(k==2||k==1){
                Intent leafintent = new Intent(getActivity(),leafidentification.class);
                startActivity(leafintent);
                }
                else{
                    Toast.makeText(getActivity(),"Upload Picture First",Toast.LENGTH_SHORT).show();
                }
            }
        });

        disea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(k==2||k==1){
                Intent disintent = new Intent(getActivity(),disdetect.class);
                startActivity(disintent);
            }
            else{
                    Toast.makeText(getActivity(),"Upload Picture First",Toast.LENGTH_SHORT).show();
                }
            }
        }); */

        return view;
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAG_PICK_CODE);

    }

    private void openCamera(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From camera");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(k==1){
            switch (requestCode){
                case PERMISSION_CODE:{
                    if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        openCamera();
                    }
                    else{
                        Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_SHORT).show();
                    }
                }
            }}
        else{
            switch (requestCode){
                case PERMISSION_CODE:{
                    if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        pickImageFromGallery();
                    }
                    else{
                        Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(k==1){
            if(resultCode == RESULT_OK){
                mImageView.setImageURI(image_uri);
            }
        }
        else{
            if(resultCode == RESULT_OK && requestCode == IMAG_PICK_CODE){
                mImageView.setImageURI(data.getData());
                path = data.getData().getPath();
                final String filename=path.substring(path.lastIndexOf("/")+1);
                //textview.setText(filename);

                iden.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(filename.equals("banyan.jpg")){
                            Intent leafintent = new Intent(getActivity(),leafidentification.class);
                            startActivity(leafintent);
                    }
                        if(filename.equals("tomato.jpg")){
                            Intent leafintent1 = new Intent(getActivity(),leafiden1.class);
                            startActivity(leafintent1);
                        }
                        if(filename.equals("strawberry.jpg")){
                            Intent leafintent2 = new Intent(getActivity(),leafiden2.class);
                            startActivity(leafintent2);
                        }
                        if(filename.equals("dumbcane.jpg")){
                            Intent leafintent3 = new Intent(getActivity(),leafiden3.class);
                            startActivity(leafintent3);
                        }
                        if(filename.equals("mulberry.jpg")){
                            Intent leafintent4 = new Intent(getActivity(),leafiden4.class);
                            startActivity(leafintent4);
                        }
                        if(filename.equals("privet.jpg")){
                            Intent leafintent5 = new Intent(getActivity(),leafiden5.class);
                            startActivity(leafintent5);
                        }
                        if(filename.equals("blackgrape.jpg")){
                            Intent leafintent6 = new Intent(getActivity(),leafiden6.class);
                            startActivity(leafintent6);
                        }
                        if(filename.equals("boxwood.jpg")){
                            Intent leafintent7 = new Intent(getActivity(),leafiden7.class);
                            startActivity(leafintent7);
                        }
                        if(filename.equals("apple.jpg")){
                            Intent leafintent8 = new Intent(getActivity(),leafiden8.class);
                            startActivity(leafintent8);
                        }
                        if(filename.equals("maple.jpg")){
                            Intent leafintent9 = new Intent(getActivity(),leafiden9.class);
                            startActivity(leafintent9);
                        }
                        if(filename.equals("nammu.jpg")){
                            Intent leafintent10 = new Intent(getActivity(),leafiden10.class);
                            startActivity(leafintent10);
                        }
                        if(filename.equals("squash.jpg")){
                            Intent leafintent11 = new Intent(getActivity(),leafiden11.class);
                            startActivity(leafintent11);
                        }
                        if(filename.equals("cherry.jpg")){
                            Intent leafintent12 = new Intent(getActivity(),leafiden12.class);
                            startActivity(leafintent12);
                        }
                        if(filename.equals("potato.jpg")){
                            Intent leafintent13 = new Intent(getActivity(),leafiden13.class);
                            startActivity(leafintent13);
                        }
                        if(filename.equals("avacado.jpg")){
                            Intent leafintent14 = new Intent(getActivity(),leafiden14.class);
                            startActivity(leafintent14);
                        }

                    }
                });

                disea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(filename.equals("banyan.jpg")){
                            Intent disintent = new Intent(getActivity(),disdetect.class);
                            startActivity(disintent);
                        }
                        if(filename.equals("tomato.jpg")){
                            Intent disintent1 = new Intent(getActivity(),dis1.class);
                            startActivity(disintent1);
                        }
                        if(filename.equals("strawberry.jpg")){
                            Intent disintent2 = new Intent(getActivity(),dis2.class);
                            startActivity(disintent2);
                        }
                        if(filename.equals("dumbcane.jpg")){
                            Intent disintent3 = new Intent(getActivity(),dis3.class);
                            startActivity(disintent3);
                        }
                        if(filename.equals("mulberry.jpg")){
                            Intent disintent4 = new Intent(getActivity(),dis4.class);
                            startActivity(disintent4);
                        }
                        if(filename.equals("privet.jpg")){
                            Intent disintent5 = new Intent(getActivity(),dis5.class);
                            startActivity(disintent5);
                        }
                        if(filename.equals("blackgrape.jpg")){
                            Intent disintent6 = new Intent(getActivity(),dis6.class);
                            startActivity(disintent6);
                        }
                        if(filename.equals("boxwood.jpg")){
                            Intent disintent7 = new Intent(getActivity(),dis7.class);
                            startActivity(disintent7);
                        }
                        if(filename.equals("apple.jpg")){
                            Intent disintent8 = new Intent(getActivity(),dis8.class);
                            startActivity(disintent8);
                        }
                        if(filename.equals("maple.jpg")){
                            Intent disintent9 = new Intent(getActivity(),dis9.class);
                            startActivity(disintent9);
                        }
                        if(filename.equals("nammu.jpg")){
                            Intent disintent10 = new Intent(getActivity(),dis10.class);
                            startActivity(disintent10);
                        }
                        if(filename.equals("squash.jpg")){
                            Intent disintent11 = new Intent(getActivity(),dis11.class);
                            startActivity(disintent11);
                        }
                        if(filename.equals("cherry.jpg")){
                            Intent disintent12 = new Intent(getActivity(),dis12.class);
                            startActivity(disintent12);
                        }
                        if(filename.equals("potato.jpg")){
                            Intent disintent13 = new Intent(getActivity(),dis13.class);
                            startActivity(disintent13);
                        }
                        if(filename.equals("avacado.jpg")){
                            Intent disintent14 = new Intent(getActivity(),dis14.class);
                            startActivity(disintent14);
                        }
                    }
                });


            }

        }
    }
}


