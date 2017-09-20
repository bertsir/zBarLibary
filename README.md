# zBarLibary
zbar扫描快，zxing可以生成和识别本地，So,我就把他们结合在了一起


## 预览
![](http://tu.bertsir.top/images/2017/09/20/scan.gif)
![](http://tu.bertsir.top/images/2017/09/20/create.gif)


### 使用方法
## 1.识别二维码（条形码）
<pre>
  startActivityForResult(new Intent(getApplicationContext(), QRActivity.class), FunctionConfig.REQUEST_CAMERA);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FunctionConfig.REQUEST_CAMERA) {
                Log.e(TAG, "onActivityResult: " + data.getStringExtra("QRcontent"));
                Toast.makeText(getApplicationContext(), data.getStringExtra("QRcontent"), Toast.LENGTH_SHORT).show();
            }
        }
    }
</pre>
OK,就这么简单！

##### 如果扫描界面不符合你的需求，来吧QRActivity的布局文件你随便改，保证改起来比别的库简单！

## 2.生成二维码
<pre>
Bitmap qrCode = QRUtils.getInstance().createQRCode("www.qq.com");
</pre>

## 3.识别本地二维码
<pre>
//可以传图片路径，Bitmap,ImageView 是不是很人性化
String s = QRUtils.getInstance().decodeQRcode(iv_qr);
</pre>



#### 二维码也就这些需求吧，这么简单就可以搞定了，识别速度是zxing的很多倍！方便了你的话可不可以给个Start

### DEMO
![](http://tu.bertsir.top/images/2017/09/20/zBMm.png)
