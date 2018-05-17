package com.lh.fingerprintproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.lh.fingerprintproject.FingerprintUtils.startToSetFingerprint

/**
 * 开启指纹识别的判断逻辑：
 * [1]是否支持指纹识别
 * [2]是否设置了锁屏密码，开启了指纹识别（应用配置指纹识别权限）
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mSDKVersionTv: TextView
    private lateinit var mHasPermissionTv: TextView
    private lateinit var mStartBtn: Button
    private var hasPermission = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSDKVersionTv = findViewById(R.id.tv_main_sdk_version)
        mHasPermissionTv = findViewById(R.id.tv_fingerprint_permission)
        mStartBtn = findViewById(R.id.btn_main_fingerprint)

        mSDKVersionTv.text = "系统版本：${Build.VERSION.SDK_INT}"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED
            mHasPermissionTv.text = "是否有指纹权限：$hasPermission"
        } else {
            hasPermission = true
            mHasPermissionTv.text = "是否有指纹权限：M(23-6.0)以下不需要运行时权限"
        }

        FingerprintUtils.init(this)
        mStartBtn.setOnClickListener {
            if (FingerprintUtils.isHardwareDetected()) {
                if (FingerprintUtils.hasEnrolledFingerprints()) {
                    handleFinger()
                } else {
                    toast("您未录制任何指纹，跳转设置")
                    startToSetFingerprint()
                }
            } else {
                toast("您的手机不支持指纹识别")
            }
        }
    }

    private fun handleFinger() {
        FingerprintUtils.authenticate(object : FingerprintManagerCompat.AuthenticationCallback() {
            //在识别指纹成功时调用。
            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                toast("识别成功")
            }

            //当指纹有效但未被识别时调用，系统给我们提供了5次重试机会
            override fun onAuthenticationFailed() {
                toast("识别失败，请重试\n 重试次数")
            }

            /**
             *  当5次重试机会用完后，会调用此方法。
             * 具体的禁用时间由手机厂商的系统不同而有略微差别，有的是1分钟，有的是30秒等等。
             * 而且，由于手机厂商的系统区别，有些系统上调用了onAuthenticationError()后，
             * 在禁用时间内，其他APP里面的指纹识别功能也无法使用，甚至系统的指纹解锁功能也无法使用。
             * 而有的系统上，在禁用时间内调用其他APP的指纹解锁功能，或者系统的指纹解锁功能，就能立即重置指纹识别功能。
             */
            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                toast(errString as String)
            }

            // Called when a recoverable error has been encountered during authentication. The help string is provided to give the user guidance for what went wrong, such as "Sensor dirty, please clean it."
            override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                toast(helpString as String)
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0x99) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "已经获取指纹权限", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "已拒绝取指纹权限", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun Context.toast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}


