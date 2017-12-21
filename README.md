# zBarLibary
zbar扫描快，zxing可以生成和识别本地，So,我就把他们结合在了一起，这样二维码识别就更便捷了


## 预览
![](http://upload-images.jianshu.io/upload_images/3029020-b0044075b21b2f7c.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/363)
![](http://upload-images.jianshu.io/upload_images/3029020-6d39c71ef24deaee.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/363)


### 更新日志：

#### 2017-12-21:
1.修复了左上角返回按钮无效的问题

#### 2017-10-13:
1.修复了识别二维码的一个BUG(感谢 穿越硝烟的迷彩)

#### 2017-09-22：
1.重构了整个包，修改了包名</br>
2.添加了全平台的so文件，其实看官们保留x86和v7a就够了</br>
3.新增打开闪光灯</br>
4.新增了从相册识别</br>
5.修改了启动方法和结果回调</br>
6.新增了一大波看官们可以配置的功能</br>




### 使用方法
## 1.识别二维码（条形码）
<pre>
QrConfig qrConfig = new QrConfig.Builder()
                .setDesText("(识别二维码)")//扫描框下文字
                .setShowDes(false)//是否显示扫描框下面文字
                .setShowLight(true)//显示手电筒按钮
                .setShowTitle(true)//显示Title
                .setShowAlbum(true)//显示从相册选择按钮
                .setCornerColor(Color.WHITE)//设置扫描框颜色
                .setLineColor(Color.WHITE)//设置扫描线颜色
                .setLineSpeed(QrConfig.LINE_MEDIUM)//设置扫描线速度
                .setScanType(QrConfig.TYPE_QRCODE)//设置扫描框类型（二维码，条形码）
                .setPlaySound(false)//是否扫描成功后bi~的声音
                .setTitleText("扫描二维码")//设置Tilte文字
                .setTitleBackgroudColor(Color.BLUE)//设置状态栏颜色
                .setTitleTextColor(Color.BLACK)//设置Title文字颜色
                .create();
        QrManager.getInstance().init(qrConfig).startScan(MainActivity.this, new QrManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
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



#### 二维码也就这些需求吧，这么简单就可以搞定了，识别速度是zxing的很多倍！方便了你的话可不可以给个Start，如遇BUG请Issues

### DEMO
![](http://tu.bertsir.top/images/2017/09/22/CyKm.png)
