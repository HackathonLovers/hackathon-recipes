package com.h4ckademy.lextrend.lextrendcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class ScreenShot {
    private View frameView;
    private Bitmap picture;
    private Context context;
    private File path;

    public ScreenShot (View view, Context context, Bitmap picture) {
        frameView = view;
        this.context = context;
        this.picture = picture;
    }


    public void obtainScreenshot() {
        checkPath();
        saveBitmap();
    }

    private void checkPath () {

        String finalDir = File.separator + "Lextrend" + File.separator + "Pictures";
        path = new File(Environment.getExternalStorageDirectory(),finalDir);

        if(!path.exists()) {
            path.mkdirs();
        }
    }

    private void saveBitmap() {
        String fileName = getFileName();
        File file = new File(path, fileName);

        try{
            FileOutputStream fos = new FileOutputStream(file);
            picture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Toast toast = Toast.makeText(context, "Picture saved correctly", Toast.LENGTH_LONG);
            toast.show();
        }
        catch (Exception e){
            Toast toast = Toast.makeText(context, "Error while saving picture", Toast.LENGTH_LONG);
            toast.show();
            Log.wtf("Error", e);
        }

    }

    private String getFileName () {
        return UUID.randomUUID().toString() +
                new SimpleDateFormat("_yyyy-MM-dd_HH-mm").format(new Date()) + ".jpg";
    }


}
