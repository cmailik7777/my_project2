package com.myauto.designer;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Designer2 on 26.02.2018.
 */

public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceHolder surfaceHolder;

    public ImageSurfaceView(Context context, Camera camera) {
        super(context);
        this.camera = camera;


        this.camera.setDisplayOrientation(90);
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Camera.Parameters params = camera.getParameters();

        if (this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE){
            params.set("orientation","portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        }else {
            params.set("orientation","landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);
        }

        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        int w = 0, h = 0;
        for (Camera.Size size : sizes) {
            if (size.width > w || size.height > h) {
                w = size.width;
                h = size.height;
            }

        }
        params.setPictureSize(w, h);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        camera.setParameters(params);
        try {
            this.camera.setPreviewDisplay(holder);
            this.camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.camera.stopPreview();
        this.camera.release();
    }


}
