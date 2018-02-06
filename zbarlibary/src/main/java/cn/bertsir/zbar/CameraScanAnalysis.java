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

import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.bertsir.zbar.Qr.Config;
import cn.bertsir.zbar.Qr.Image;
import cn.bertsir.zbar.Qr.ImageScanner;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.Qr.SymbolSet;

/**
 * Created by Yan Zhenjie on 2017/5/5.
 */
class CameraScanAnalysis implements Camera.PreviewCallback {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ImageScanner mImageScanner;
    private Handler mHandler;
    private ScanCallback mCallback;
    private static final String TAG = "CameraScanAnalysis";

    private boolean allowAnalysis = true;
    private Image barcode;

    CameraScanAnalysis() {
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

            Camera.Size size = camera.getParameters().getPreviewSize();
            barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            if(Symbol.is_only_scan_center){
                int cropWidth = (int) (Symbol.cropWidth* (size.height /(float)Symbol.screenWidth));
                int cropHeight = (int) (Symbol.cropHeight* (size.width /(float)Symbol.screenHeight));

                Symbol.cropX = size.width/2 - cropHeight/2;
                Symbol.cropY = size.height/2 - cropWidth/2;

                barcode.setCrop(Symbol.cropX, Symbol.cropY, cropHeight, cropWidth);
            }

            executorService.execute(mAnalysisTask);
        }
    }

    private Runnable mAnalysisTask = new Runnable() {
        @Override
        public void run() {
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