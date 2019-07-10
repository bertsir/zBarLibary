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
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.detector.Detector;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
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

    private MultiFormatReader multiFormatReader = new MultiFormatReader();


    CameraScanAnalysis(Context context) {
        this.context = context;
        mImageScanner = new ImageScanner();
        if (Symbol.scanType == QrConfig.TYPE_QRCODE) {
            mImageScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
            mImageScanner.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        } else if (Symbol.scanType == QrConfig.TYPE_BARCODE) {
            mImageScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
            mImageScanner.setConfig(Symbol.CODE128, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.CODE39, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.EAN13, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.EAN8, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.UPCA, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.UPCE, Config.ENABLE, 1);
            mImageScanner.setConfig(Symbol.UPCE, Config.ENABLE, 1);
        } else if (Symbol.scanType == QrConfig.TYPE_ALL) {
            mImageScanner.setConfig(Symbol.NONE, Config.X_DENSITY, 3);
            mImageScanner.setConfig(Symbol.NONE, Config.Y_DENSITY, 3);
        } else if (Symbol.scanType == QrConfig.TYPE_CUSTOM) {
            mImageScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
            mImageScanner.setConfig(Symbol.scanFormat, Config.ENABLE, 1);
        } else {
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
            this.data = data;
            this.camera = camera;

            size = camera.getParameters().getPreviewSize();
            barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            if (Symbol.is_only_scan_center) {
                cropWidth = (int) (Symbol.cropWidth * (size.height / (float) Symbol.screenWidth));
                cropHeight = (int) (Symbol.cropHeight * (size.width / (float) Symbol.screenHeight));

                Symbol.cropX = size.width / 2 - cropHeight / 2;
                Symbol.cropY = size.height / 2 - cropWidth / 2;

                barcode.setCrop(Symbol.cropX, Symbol.cropY, cropHeight, cropWidth);

            }

            executorService.execute(mAnalysisTask);

        }
    }

    /**
     * 相机设置焦距
     */
    public void cameraZoom(Camera mCamera) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (!parameters.isZoomSupported()) {
                return;
            }
            int maxZoom = parameters.getMaxZoom();
            if (maxZoom == 0) {
                return;
            }
            if (parameters.getZoom() + 10 > parameters.getMaxZoom()) {
                return;
            }
            parameters.setZoom(parameters.getZoom() + 10);
            mCamera.setParameters(parameters);
        }
    }

    private Runnable mAnalysisTask = new Runnable() {
        @Override
        public void run() {

            if (Symbol.is_auto_zoom && Symbol.scanType == QrConfig.TYPE_QRCODE
                    && QRUtils.getInstance().isScreenOriatationPortrait(context)) {
                if (Symbol.cropX == 0 || Symbol.cropY == 0 || cropWidth == 0 || cropHeight == 0) {
                    return;
                }
                LuminanceSource source = new PlanarYUVLuminanceSource(data, size.width,
                        size.height, Symbol.cropX, Symbol.cropY, cropWidth, cropHeight, true);
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
                        int len = (int) Math.sqrt(Math.abs(point1X - point2X) * Math.abs(point1X - point2X) + Math.abs(point1Y - point2Y) * Math.abs(point1Y - point2Y));
                        if (len < cropWidth / 4 && len > 10) {
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
                if (Symbol.looperScan) {
                    allowAnalysis = true;
                }
            } else {
                if (Symbol.doubleEngine) {
                    decode(data, size.width, size.height);
                } else {
                    allowAnalysis = true;
                }
            }
        }
    };


    /**
     * zxing解码
     *
     * @param data
     * @param width
     * @param height
     */
    private void decode(byte[] data, int width, int height) {
        Result rawResult = null;
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;
        data = rotatedData;
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height, 0,
                0, width, height, true);
        ;
        Hashtable<DecodeHintType, Object> scanOption = new Hashtable<>();
        scanOption.put(DecodeHintType.CHARACTER_SET, "utf-8");
        Collection<Reader> readers = new ArrayList<>();
        readers.add((new QRCodeReader()));
        scanOption.put(DecodeHintType.POSSIBLE_FORMATS,readers);
        multiFormatReader.setHints(scanOption);
        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
                String resultStr = rawResult.toString();
                if (!TextUtils.isEmpty(resultStr)) {
                    Message message = mHandler.obtainMessage();
                    message.obj = resultStr;
                    message.sendToTarget();
                    if (Symbol.looperScan) {
                        allowAnalysis = true;
                    }
                } else allowAnalysis = true;
            } catch (ReaderException re) {
                allowAnalysis = true;
                //Log.i("解码异常",re.toString());
            } finally {
                multiFormatReader.reset();
            }
        } else {
            allowAnalysis = true;
        }

    }


}