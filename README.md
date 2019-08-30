
![](http://tu.bertsir.com/images/2019/06/27/Cool-Sky_meitu_1.jpg)

zbar扫描快，zxing可以生成和识别本地，So,我就把他们结合在了一起，这样二维码识别就更便捷了（包含主要功能，二维码识别生成，条形码识别生成）


## 预览
![](http://upload-images.jianshu.io/upload_images/3029020-b0044075b21b2f7c.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/363)
![](http://upload-images.jianshu.io/upload_images/3029020-6d39c71ef24deaee.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/363)
![WechatIMG25.png](https://upload-images.jianshu.io/upload_images/3029020-c502460466c67315.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/360)

## Plan
1.优化代码结构</br>
2.优化双识别引擎</br>
3.更换图片裁切库</br>
4.优化自动拉近功能


## 更新日志


### 2019-08-30 (1.3.5)
1.新增了连续扫描时间间隔的设置（setLooperWaitTime）</br>
2.优化了屏幕旋转重建Activity的问题


### 2019-08-19 (1.3.4)
1.<font color=red>修改识别结果返回类型，由String变为ScanResult</font>(包含码的内容和码的类型，后期可能还会拓展)



### 2019-07-10 (1.3.3)
1.新增双识别引擎(zbar+zxing同时识别，建议只用在需求二维码扫描不出来的时候做尝试使用，且暂只支持对二维码有效，在开启这个功能的时候只识别框中内容将失效)</br>
2.新增对打开相册文案的自定义</br>
3.新增持续扫描</br>



### 2019-06-27 (1.3.2)
1.支持双指缩放摄像头了（现在支持三种摄像头的缩放方式了）

### 2019-06-18 (1.3.1)
1.修复BUG

### 2019-06-12 (1.3.0)
1.修复了开启自动拉近时部分机型崩溃的问题


### 2019-04-08 (1.2.9)
1.新增屏幕方向指定API(setScreenOrientation 默认为竖屏)</br>
注：当屏幕为横向时自动拉近距离将不可用

### 2019-04-08 (1.2.8)
1.强制指定扫描页面为竖屏模式（废弃）

### 2019-03-26 (1.2.6)
1.从相册选择识别支持了条形码

### 2019-03-14 (1.2.5)
1.优化了低配置机型开启自动拉近的UI卡顿问题

### 2019-03-13 (1.2.4)
1.解决多应用引用本库引起的安装失败问题

### 2019-03-11(1.2.3)
1.修复了部分机型在设置SeekBar颜色时可能出现的类强转异常

### 2019-03-04
1.新增自动焦距调整(实验性功能)</br>
2.解决了可能会出现的FileProvider的冲突

### 2019-03-01
1.新增手动焦距调整

### 2019-01-28
1.修复了Nexus 5X扫码摄像头倒置的问题(感谢Selince)

### 2019-01-15
1.新增了从相册选择图片识别时候的图片裁剪</br>
2.修复了相机对焦时的部分问题


### 2018-11-15
1.修复无闪光灯机器打开手电筒闪退的BUG(感谢 DwayneZhang)

### 2018-10-23
1.提高从相册选择二维码识别的成功率

### 2018-09-10
1.修复从相册选择路径的bug</br>
2.修复从相册选择对存储权限的申请</br>
3.提高生成二维码的容错率

### 2018-08-13
1.加入内部权限处理

### 2018-03-13
1.修复生成条形码时下方文字的BUG（感谢simplepeng）</br>
2.优化识别本地二维码（由zxing识别转为zbar识别，大幅提升复杂图片中二维码的识别）</br>
3.新增识别本地条形码</br>


### 2018-02-26
1.新增生成带logo的二维码</br>
2.调整生成二维码的边框值</br>
3.提升生成二维码的容错率</br>


### 2018-02-09
1.修复了从相册选择二维码无结果返回的BUG

### 2018-02-08
1.修复中文"你好"扫描乱码的情况</br>
2.修复生成二维码不能使用中文的问题</br>
3.新增生成二维码指定大小的方法</br>

### 2018-02-07
1.修复打开手电筒扫码成功无结果也不返回的问题

### 2018-02-06
1.修复不能隐藏扫描框下方描述的BUG</br>
2.新增只识别扫描框中的内容</br>
3.新增自定义扫码类型</br>
4.新增自定义扫描框类型</br>
5.新增自定义提示音</br>

### 2018-02-02
1.修复二维码扫描的误识别，提高二维码扫描速度

### 2017-12-21:
1.修复了左上角返回按钮无效的问题

### 2017-10-13:
1.修复了识别二维码的一个BUG(感谢 穿越硝烟的迷彩)

### 2017-09-22：
1.重构了整个包，修改了包名</br>
2.添加了全平台的so文件，其实看官们保留x86和v7a就够了</br>
3.新增打开闪光灯</br>
4.新增了从相册识别</br>
5.修改了启动方法和结果回调</br>
6.新增了一大波看官们可以配置的功能</br>


## 引入
#### 方式一（需要修改布局的）：
GitHub下载库，使用File -> new -> Import Module方式

#### 方式二（不需要修改布局）：
最新版本（推荐）：
<pre>
 implementation 'cn.bertsir.zbarLibary:zbarlibary:latest.release'
</pre>
指定版本：
<pre>
implementation 'cn.bertsir.zbarLibary:zbarlibary:1.3.5'
</pre>
注意：如果不需要尝鲜后续功能，并且保持现有稳定，建议使用指定版本号



#### 关于包的大小问题
为了确保全平台的兼容，默认库中携带了arm64-v8a，armeabi，armeabi-v7a，mips，mips64，x86，x86_64，的so文件，可能会导致安装包体积大，和其他第三方SDK冲突的问题，可以使用以下代码解决大小和冲突
<pre>
android {
    ......
    defaultConfig {
        ......
		......
		......
        ndk {
            abiFilters "armeabi-v7a"  // 指定要ndk需要兼容的架构(这样其他依赖包里mips,x86,armeabi,arm-v8之类的so会被过滤掉)
        }
    }
}
</pre>


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
                .setScanType(QrConfig.TYPE_QRCODE)//设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
                .setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE)//设置扫描框类型（二维码还是条形码，默认为二维码）
                .setCustombarcodeformat(QrConfig.BARCODE_I25)//此项只有在扫码类型为TYPE_CUSTOM时才有效
                .setPlaySound(true)//是否扫描成功后bi~的声音
                .setNeedCrop(true)//从相册选择二维码之后再次截取二维码
                .setDingPath(R.raw.test)//设置提示音(不设置为默认的Ding~)
                .setIsOnlyCenter(true)//是否只识别框中内容(默认为全屏识别)
                .setTitleText("扫描二维码")//设置Tilte文字
                .setTitleBackgroudColor(Color.BLUE)//设置状态栏颜色
                .setTitleTextColor(Color.BLACK)//设置Title文字颜色
                .setShowZoom(false)//是否手动调整焦距
                .setAutoZoom(false)//是否自动调整焦距
                .setFingerZoom(false)//是否开始双指缩放
                .setScreenOrientation(QrConfig.SCREEN_PORTRAIT)//设置屏幕方向
                .setDoubleEngine(false)//是否开启双引擎识别(仅对识别二维码有效，并且开启后只识别框内功能将失效)
                .setOpenAlbumText("选择要识别的图片")//打开相册的文字
                .setLooperScan(false)//是否连续扫描二维码
                .setLooperWaitTime(5*1000)//连续扫描间隔时间
                .create();
   QrManager.getInstance().init(qrConfig).startScan(MainActivity.this, new QrManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(ScanResult result) {
                Log.e(TAG, "onScanSuccess: "+result );
                Toast.makeText(getApplicationContext(), "内容："+result.getContent()
                                +"  类型："+result.getType(), Toast.LENGTH_SHORT).show();
            }
        });
</pre>
OK,就这么简单！

##### 如果扫描界面不符合你的需求，来吧QRActivity的布局文件你随便改，保证改起来比别的库简单！

## 2.生成码
###  2.1生成二维码 
<pre>
Bitmap qrCode = QRUtils.getInstance().createQRCode("www.qq.com");
</pre>

####  2.1.1生成二维码并添加Logo
<pre>
Bitmap qrCode = QRUtils.getInstance().createQRCodeAddLogo(et_qr_content.getText().toString(),BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
</pre>
###  2.2生成条形码
<pre>
QRUtils.TextViewConfig textViewConfig = new QRUtils.TextViewConfig();
textViewConfig.setSize(10);
 Bitmap barCodeWithText = QRUtils.getInstance().createBarCodeWithText(getApplicationContext(), content, 300, 100, textViewConfig);
</pre>

## 3.识别本地
### 3.1 识别本地二维码
<pre>
//可以传图片路径，Bitmap,ImageView 是不是很人性化
String s = QRUtils.getInstance().decodeQRcode(iv_qr);
</pre>

### 3.2 识别本地条形码
<pre>
//可以传图片路径，Bitmap,ImageView 是不是很人性化
String s = QRUtils.getInstance().decodeBarcode(iv_qr);
</pre>

## 4.参数描述
| name | format | description |
| ------------- |:-------------:| :-------------:|
| setDesText | String | 设置扫描框下方描述文字 |
| setShowDes | Boolean | 设置是否显示扫描框下方描述文字 |
| setShowLight | Boolean | 是否开启手电筒功能 |
| setShowAlbum | Boolean | 是否开启从相册选择功能 |
| setShowTitle | Boolean | 是否显示Title |
| setTitleText | String | 设置Title文字 |
| setTitleBackgroudColor | int | 设置Title背景色 |
| setTitleTextColor | int | 设置Title文字颜色 |
| setCornerColor | int | 设置扫描框颜色 |
| setLineColor | int | 设置扫描线颜色 |
| setLineSpeed | int | 设置扫描线速度</br>QrConfig.LINE_FAST(快速)</br>QrConfig.LINE_MEDIUM(中速）<br>QrConfig.LINE_SLOW(慢速) |
| setScanType | int | 设置扫描类型</br>QrConfig.TYPE_QRCODE(二维码)</br>QrConfig.TYPE_BARCODE(条形码)</br>QrConfig.TYPE_ALL(全部类型)</br>QrConfig.TYPE_CUSTOM(指定类型) |
| setScanViewType | int | 设置扫描框类型</br>QrConfig.SCANVIEW_TYPE_QRCODE(二维码)</br>QrConfig.SCANVIEW_TYPE_BARCODE(条形码) |
| setCustombarcodeformat| int| 设置指定扫码类型（举例：QrConfig.BARCODE_EAN13）,此项只有在ScanType设置为自定义时才生效 |
| setIsOnlyCenter| Boolean | 设置是否只识别扫描框中的内容（默认为全屏扫描） |
| setPlaySound | Boolean | 设置扫描成功后是否有提示音 |
| setDingPath | int| 自定义提示音（举例：R.raw.test，不设置为默认的) |
| setNeedCrop | Boolean | 从相册选择二维码之后再次手动框选二维码(默认为true) |
| setShowZoom | Boolean | 是否开启手动调整焦距(默认为false) |
| setAutoZoom | Boolean | 是否开启自动调整焦距(默认为false) |
| setFingerZoom | Boolean | 是否开启双指调整焦距(默认为false) |
| setScreenOrientation | int | 设置屏幕方向</br>QrConfig.SCREEN_PORTRAIT(纵向)</br>QrConfig.SCREEN_LANDSCAPE(横向）<br>QrConfig.SCREEN_SENSOR(传感器方向) |
| setDoubleEngine | Boolean | 是否开启双识别引擎(默认为false) |
| setLooperScan | Boolean | 是否开启连续扫描(默认为false) |
| setOpenAlbumText | String | 设置打开相册的文字 |
| setLooperWaitTime | int | 设置连续扫描间隔时间，单位毫秒（默认为0） |

## 5.混淆
<pre>
-keep class cn.bertsir.zbar.Qr.** { *; }
</pre>

#### 二维码也就这些需求吧，这么简单就可以搞定了，识别速度是zxing的很多倍！方便了你的话可不可以给个Start，如遇BUG请Issues


### DEMO
![](https://www.pgyer.com/app/qrcode/CyKm)

#### [传送门](https://www.pgyer.com/CyKm "传送门")

### 打赏扫这里👇一分也是❤️（可以在备注中填写自己的github链接）

![](http://upload-images.jianshu.io/upload_images/3029020-8066ee3c42334a86.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)    ![](http://upload-images.jianshu.io/upload_images/3029020-5d220c5715c59947.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)

#### 打赏历史（排名不分先后）

| name | platform | price | 备注
| ------------- |:-------------:| :-------------:|:-------------:|
| 找不到记录的可爱大佬们 | 微信/支付宝 | -- | -- |
| *走 | 微信 | ￥5.00 | -- |
| Q*x | 微信 | ￥2.33 | 感谢开源zBar辛苦 |
| *瑞波 | 支付宝 | ￥6.66 | -- |
| *瑞波 | 支付宝 | ￥6.66 | -- |
| *世东 | 支付宝 | ￥6.60 | 感谢 |
| *天 | 微信 | ￥5.00 | -- |


## License
<pre>
MIT License

Copyright (c) 2018 bertsir

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

</pre>