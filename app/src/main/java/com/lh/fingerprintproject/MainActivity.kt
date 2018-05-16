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
import android.content.Intent
import android.content.ComponentName
import com.lh.fingerprintproject.FingerprintUtils.startToSetFingerprint


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

            //当指纹有效但未被识别时调用。
            override fun onAuthenticationFailed() {
                toast("识别失败，请重试\n 重试次数")
            }

            //当遇到不可恢复的错误并且操作完成时调用。
            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
            }

            //在认证期间遇到可恢复的错误时调用。
            override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
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


