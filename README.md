[TOC]

# 指纹识别介绍
指纹识别通过指纹传感器采集信息，进行指纹图像的预处理，然后进行特征点提取、校验。其应用场景有：
- 系统解锁
- 应用锁
- 支付认证
- 普通的登录认证

Google支持的设备条件：

``` java
    Android SDK 26  
    Android Build Tools v27.0.2
    Android Support Repository
```

# 集成指纹识别

 ## 判断逻辑
   
   1. 当前设备是否支持指纹识别
   2. 是否设置了锁屏密码，开启了指纹识别（应用配置指纹识别权限）
 
 ## 主要API
 
 [官方Demo地址](https://github.com/googlesamples/android-FingerprintDialog)
 
 ```
    public void authenticate(CryptoObject, CancellationSignal, flags, AuthenticationCallback,Handler)
 ``` 
 * 第一个参数是一个加密对象，防止第三方恶意攻击的包装类：new FingerprintManager.CryptoObject(cipher)。
 * 第二个参数是提供了取消操作的能力：new CancellationSignal() 
 * 第三个参数是一个标志，默认为0。
 * 第四个参数是提供了指纹识别的几个回调方法，包括指纹识别成功、失败等。需要我们重写。
 * 第五个参数用于处理回调事件，可以传null。
 

# 参考
* [Android开发学习—指纹识别系统的原理与使用](https://blog.csdn.net/qq_37293612/article/details/54598302)
* [Android 6.0指纹识别App开发demo](https://blog.csdn.net/createchance/article/details/51991764)