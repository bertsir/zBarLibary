/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.bertsir.zbar;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;

import cn.bertsir.zbar.utils.QRUtils;

/**
 * <p>Camera manager.</p>
 */
public final class CameraManager {
    private static final String TAG = "CameraManager";

    private final CameraConfiguration mConfiguration;
    private Context context;

    private Camera mCamera;

    public CameraManager(Context context) {
        this.context = context;
        this.mConfiguration = new CameraConfiguration(context);
    }

    /**
     * Opens the mCamera driver and initializes the hardware parameters.
     *
     * @throws Exception ICamera open failed, occupied or abnormal.
     */
    public synchronized void openDriver() throws Exception {
        if (mCamera != null) return;

        mCamera = Camera.open();
        if (mCamera == null) throw new IOException("The camera is occupied.");

        mConfiguration.initFromCameraParameters(mCamera);

        Camera.Parameters parameters = mCamera.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten();
        try {
            mConfiguration.setDesiredCameraParameters(mCamera, false);

        } catch (RuntimeException re) {
            if (parametersFlattened != null) {
                parameters = mCamera.getParameters();
                parameters.unflatten(parametersFlattened);
                try {
                    mCamera.setParameters(parameters);

                    mConfiguration.setDesiredCameraParameters(mCamera, true);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * Camera is opened.
     *
     * @return true, other wise false.
     */
    public boolean isOpen() {
        return mCamera != null;
    }

    /**
     * Get camera configuration.
     *
     * @return {@link CameraConfiguration}.
     */
    public CameraConfiguration getConfiguration() {
        return mConfiguration;
    }

    /**
     * Camera start preview.
     *
     * @param holder          {@link SurfaceHolder}.
     * @param previewCallback {@link Camera.PreviewCallback}.
     * @throws IOException if the method fails (for example, if the surface is unavailable or unsuitable).
     */
    public void startPreview(SurfaceHolder holder, Camera.PreviewCallback previewCallback) throws IOException {
        if (mCamera != null) {
            //解决nexus5x扫码倒立的情况
            if(android.os.Build.MANUFACTURER.equals("LGE") &&
                    android.os.Build.MODEL.equals("Nexus 5X")) {
                mCamera.setDisplayOrientation(QRUtils.getInstance().isScreenOriatationPortrait(context) ? 270 : 180);
            }else {
                mCamera.setDisplayOrientation(QRUtils.getInstance().isScreenOriatationPortrait(context) ? 90 : 0);
            }
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
        }
    }


    /**
     * Camera stop preview.
     */
    public void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (Exception ignored) {
                // nothing.
            }
            try {
                mCamera.setPreviewDisplay(null);
            } catch (IOException ignored) {
                // nothing.
            }
        }
    }

    /**
     * Focus on, make a scan action.
     *
     * @param callback {@link Camera.AutoFocusCallback}.
     */
    public void autoFocus(Camera.AutoFocusCallback callback) {
        if (mCamera != null)
            try {
                mCamera.autoFocus(callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * set Camera Flash
     */
    public void setFlash(){
        if(mCamera != null){
            Camera.Parameters parameters = mCamera.getParameters();
            if(parameters.getFlashMode() == null) {
                return;
            }
            if(parameters.getFlashMode().endsWith(Camera.Parameters.FLASH_MODE_TORCH)){
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
            mCamera.setParameters(parameters);
        }
    }

    /**
     * set Camera Flash
     */
    public void setFlash(boolean open){
        if(mCamera != null){
            Camera.Parameters parameters = mCamera.getParameters();
            if(parameters.getFlashMode() == null) {
                return;
            }
            if(!open){
                if(parameters.getFlashMode().endsWith(Camera.Parameters.FLASH_MODE_TORCH)){
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
            }else {
                if(parameters.getFlashMode().endsWith(Camera.Parameters.FLASH_MODE_OFF)){
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            }
            mCamera.setParameters(parameters);
        }
    }

    /**
     * 相机设置焦距
     */
    public void setCameraZoom(float ratio){
        if(mCamera != null){
            Camera.Parameters parameters = mCamera.getParameters();
            if(!parameters.isZoomSupported()){
                return;
            }
            int maxZoom = parameters.getMaxZoom();
            if(maxZoom == 0){
                return;
            }
            int zoom = (int) (maxZoom * ratio);
            parameters.setZoom(zoom);
            mCamera.setParameters(parameters);
        }
    }
}
