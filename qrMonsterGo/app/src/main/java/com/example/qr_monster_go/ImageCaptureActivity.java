package com.example.qr_monster_go;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageCaptureActivity extends AppCompatActivity {
    private Camera camera;
    private SurfaceView surfaceView;
    private Button takePictureButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        surfaceView = findViewById(R.id.image_capture_surface);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                camera = Camera.open();
                camera.setDisplayOrientation(90); // set camera to portrait
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                //***//
            }
        });

        takePictureButton = findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        byte[] scaledBytes = scaleImage(bytes);

                        // add image map to intent and finish activity
                        Intent intent = new Intent();
                        intent.putExtra("imageMap", scaledBytes);
                        setResult(0, intent);

                        finish();
                    }
                });
            }
        });
    }

    private byte[] scaleImage(byte[] data) {
        Bitmap map = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap scaledMap = Bitmap.createScaledBitmap(map, 480, 640, false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }
}