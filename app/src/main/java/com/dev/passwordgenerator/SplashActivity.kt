package com.dev.passwordgenerator

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val splashDelayMs = 1600L

    override fun onCreate(savedInstanceState: Bundle?) {
        // Terapkan tema tersimpan sebelum tampilan dibuat
        ThemeManager.applySavedTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.imgLogo)

        if (ThemeManager.isAnimationEnabled(this)) {
            val anim = AnimationUtils.loadAnimation(this, R.anim.splash_logo_anim)
            logo.startAnimation(anim)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            if (ThemeManager.isAnimationEnabled(this)) {
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            finish()
        }, splashDelayMs)
    }
}
