package com.example.qr_monster_go.scan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

import com.example.qr_monster_go.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This activity is used to take a picture of the location of a code after having scanned it
 *
 * this activity gets called by the confirm image dialog and is connected to ScanCodeActivity
 *
 * after an image is taken this activity scales and rotates the image into the proper form and then
 * returns the image data to ScanCodeActivity to be processed and added to the database
 */
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
                // open the camera and set the orientation to portrait
                camera = Camera.open();
                camera.setDisplayOrientation(90); // set camera to portrait
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                // begin displaying the camera preview
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
                //nothing needs to be done here//
            }
        });

        // when the picture is taken
        takePictureButton = findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        // scale and rotate the image
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
        // scale the image then rotate
        Bitmap map = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap scaledMap = Bitmap.createScaledBitmap(map, 480, 640, false);
        scaledMap = rotateImage(scaledMap);

        // compress the photo
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    private Bitmap rotateImage(Bitmap image) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        // return the rotated image
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }
}
