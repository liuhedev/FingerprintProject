package com.lh.fingerprintproject

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal
import android.util.Log

/**
 * 指纹识别工具类
 */
object FingerprintUtils {

    private lateinit var fingerprintManager: FingerprintManagerCompat
    private var mCancellationSignal: CancellationSignal? = null

    fun init(ctx: Context) {
        fingerprintManager = FingerprintManagerCompat.from(ctx)
    }

    /**
     * 确定指纹硬件是否存在并且功能正常。
     *
     * @return 如果硬件存在且功能正确，则为true，否则为false。
     */
    fun isHardwareDetected(): Boolean {
        return fingerprintManager.isHardwareDetected
    }

    /**
     * 确定是否至少有一个指纹登记过
     *
     * @return 如果至少有一个指纹登记，则为true，否则为false
     */
    fun hasEnrolledFingerprints(): Boolean {
        return fingerprintManager.hasEnrolledFingerprints()
    }

    /**
     * 开始进行指纹识别
     * @param callback 指纹识别回调函数
     */
    fun authenticate(callback: FingerprintManagerCompat.AuthenticationCallback) {
        mCancellationSignal = CancellationSignal()
        fingerprintManager.authenticate(null, 0, mCancellationSignal, callback, null)
    }

    /**
     * 提供取消操作能力
     */
    fun cancel() {
        mCancellationSignal?.cancel()
    }

    /**
     * 跳转设置页面
     */
    fun Context.startToSetFingerprint() {
        val goToSetting = Intent(ACTION_VIEW)
        val pkgName = "com.android.settings"
        val clsName = "com.android.settings.Settings"
        goToSetting.component = ComponentName(pkgName, clsName)
        // 判断是否存在指定Activity
        if (null != goToSetting.resolveActivity(applicationContext.packageManager)) {
            startActivity(goToSetting)
        } else {
            Log.e("lh", "not found activity matching the uri")
        }
    }
}