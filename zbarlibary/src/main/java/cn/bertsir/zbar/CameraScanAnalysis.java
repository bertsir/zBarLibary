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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.detector.Detector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.bertsir.zbar.Qr.Config;
import cn.bertsir.zbar.Qr.Image;
import cn.bertsir.zbar.Qr.ImageScanner;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.Qr.SymbolSet;
import cn.bertsir.zbar.utils.QRUtils;

/**
 */
class CameraScanAnalysis implements Camera.PreviewCallback {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ImageScanner mImageScanner;
    private Handler mHandler;
    private ScanCallback mCallback;
    private static final String TAG = "CameraScanAnalysis";

    private boolean allowAnalysis = true;
    private Image barcode;
    private int cropWidth;
    private int cropHeight;
    private Camera.Size size;
    private byte[] data;
    private Camera camera;
    private Context context;


    CameraScanAnalysis(Context context) {
        this.context = context;
        mImageScanner = new ImageScanner();
        if (Symbol.scanType == QrConfig.TYPE_QRCODE) {
            mImageScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
            mImageScanner.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        } else if(Symbol.scanType == QrConfig.TYPE_BARCODE){
            mImageScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
            mImageScanner.setConfig(Symbol.CODE128, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.CODE39, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.EAN13, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.EAN8, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.UPCA, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.UPCE, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.UPCE, Config.ENABLE, 1);
        }else if(Symbol.scanType == QrConfig.TYPE_ALL){
            mImageScanner.setConfig(Symbol.NONE, Config.X_DENSITY, 3);
            mImageScanner.setConfig(Symbol.NONE, Config.Y_DENSITY, 3);
        }else if(Symbol.scanType == QrConfig.TYPE_CUSTOM){
            mImageScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
            mImageScanner.setConfig(Symbol.scanFormat, Config.ENABLE, 1);
        }else {
            mImageScanner.setConfig(Symbol.NONE, Config.X_DENSITY, 3);
            mImageScanner.setConfig(Symbol.NONE, Config.Y_DENSITY, 3);
        }

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (mCallback != null) mCallback.onScanResult((String) msg.obj);
            }
        };
    }

    void setScanCallback(ScanCallback callback) {
        this.mCallback = callback;
    }

    void onStop() {
        this.allowAnalysis = false;
    }

    void onStart() {
        this.allowAnalysis = true;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (allowAnalysis) {
            allowAnalysis = false;
            this.data =data;
            this.camera = camera;

            size = camera.getParameters().getPreviewSize();
            barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            if(Symbol.is_only_scan_center){
                cropWidth = (int) (Symbol.cropWidth* (size.height /(float)Symbol.screenWidth));
                cropHeight = (int) (Symbol.cropHeight* (size.width /(float)Symbol.screenHeight));

                Symbol.cropX = size.width/2 - cropHeight /2;
                Symbol.cropY = size.height/2 - cropWidth /2;

                barcode.setCrop(Symbol.cropX, Symbol.cropY, cropHeight, cropWidth);

            }

            executorService.execute(mAnalysisTask);

        }
    }

    /**
     * 相机设置焦距
     */
    public void cameraZoom(Camera mCamera){
        if(mCamera != null){
            Camera.Parameters parameters = mCamera.getParameters();
            if(!parameters.isZoomSupported()){
                return;
            }
            int maxZoom = parameters.getMaxZoom();
            if(maxZoom == 0){
                return;
            }
            if(parameters.getZoom() +10 > parameters.getMaxZoom()){
                return;
            }
            parameters.setZoom(parameters.getZoom()+10);
            mCamera.setParameters(parameters);
        }
    }

    private Runnable mAnalysisTask = new Runnable() {
        @Override
        public void run() {

            if(Symbol.is_auto_zoom && Symbol.scanType == QrConfig.TYPE_QRCODE
                    && QRUtils.getInstance().isScreenOriatationPortrait(context)){
                LuminanceSource source = new PlanarYUVLuminanceSource(data, size.width,
                        size.height, Symbol.cropX, Symbol.cropY, cropWidth,cropHeight, true);
                if (source != null) {
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    DetectorResult detectorResult = null;
                    try {
                        detectorResult = new Detector(bitmap.getBlackMatrix()).detect();
                        ResultPoint[] p = detectorResult.getPoints();
                        //计算扫描框中的二维码的宽度，两点间距离公式
                        float point1X = p[0].getX();
                        float point1Y = p[0].getY();
                        float point2X = p[1].getX();
                        float point2Y = p[1].getY();
                        int len =(int) Math.sqrt(Math.abs(point1X-point2X)*Math.abs(point1X-point2X)+Math.abs(point1Y-point2Y)*Math.abs(point1Y-point2Y));
                        if(len < cropWidth/4 && len > 10){
                            cameraZoom(camera);
                        }
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }



            int result = mImageScanner.scanImage(barcode);

            String resultStr = null;
            if (result != 0) {
                SymbolSet symSet = mImageScanner.getResults();
                for (Symbol sym : symSet)
                    resultStr = sym.getData();
            }

            if (!TextUtils.isEmpty(resultStr)) {
                Message message = mHandler.obtainMessage();
                message.obj = resultStr;
                message.sendToTarget();
            } else allowAnalysis = true;
        }
    };
}