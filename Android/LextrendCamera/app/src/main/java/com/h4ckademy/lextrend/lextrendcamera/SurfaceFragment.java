package com.h4ckademy.lextrend.lextrendcamera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class SurfaceFragment extends Fragment {

    @InjectView( R.id.stream )
    MjpegView mv;
    private boolean showFps = false;
    private AlertDialog.Builder dialog;

    public static SurfaceFragment newInstance(){
        return newInstance("Default Value");
    }

    public static SurfaceFragment newInstance( String value ){
        Bundle bundle = new Bundle();
        bundle.putString("key", value);
        SurfaceFragment surfaceFragment= new SurfaceFragment();
        surfaceFragment.setArguments( bundle );
        return surfaceFragment;
    }

    public SurfaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mjpeg_, container, false);

        ButterKnife.inject(this, rootView);

        return rootView;
    }

    public void onPause() {
        super.onPause();
        mv.stopPlayback();
    }

    public void onStop() {
        super.onStop();
        mv.stopPlayback();
        mv.setBackgroundColor(Color.BLACK);
    }


    public void start(final Context context) {
        // Write the correct ip of your local conection.
        // The port (8081) must not be changed
        String URL = "http://192.168.1.149:8081";

            DoRead.DoReadCallback callback = new DoRead.DoReadCallback() {
                @Override
                public void onFinish(MjpegInputStream result) {
                    Log.wtf("SurfaceFragment", "onFinish");
                    mv.setBackgroundColor(Color.TRANSPARENT);
                    mv.setSource(result);
                    mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
                    showFps = !showFps;
                    mv.showFps( showFps );
                }

                @Override
                public void onError(String errorMsg) {
                    Log.wtf("Error", errorMsg);
                    createDialog(context);
                    dialog.show();
                }
            };

            new DoRead(callback, context).execute(URL);

    }

    public void createDialog(Context context) {
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle("There is no video in the server");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    public Bitmap takeScreenshot() {
        return mv.takeScreenshotImage();
    }
}
