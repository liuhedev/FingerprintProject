# FingerprintProject
FingerprintManager 指纹识别

参考：https://blog.csdn.net/qq_37293612/article/details/54598302

# 应用场景
- 系统解锁
- 应用锁
- 支付认证
- 普通的登录认证

指纹识别是在Android 6.0之后新增的功能，因此在使用的时候需要先判断用户手机的系统版本是否支持指纹识别。另外，实际开发场景中，使用指纹的主要场景有两种：
* 纯本地使用。即用户在本地完成指纹识别后，不需要将指纹的相关信息给后台。 
* 与后台交互。用户在本地完成指纹识别后，需要将指纹相关的信息传给后台。

由于使用指纹识别功能需要一个加密对象（CryptoObject）该对象一般是由对称加密或者非对称加密获得。 
上述两种开发场景的实现大同小异，主要区别在于加密过程中密钥的创建和使用，一般来说: 
纯本地的使用指纹识别功能，只需要对称加密即可； 
后台交互则需要使用非对称加密：将私钥用于本地指纹识别，识别成功后将加密信息传给后台，后台开发人员用公钥解密，以获得用户信息。

使用FingerprintManagerCompat可以直接绕过权限（google推荐，api会帮我们做一些代码兼容问题） 

