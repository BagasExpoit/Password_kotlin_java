package com.dev.passwordgenerator

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applySavedTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbarSettings)
        toolbar.setNavigationOnClickListener { finish() }

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupTheme)
        val switchAnimation = findViewById<Switch>(R.id.switchAnimation)

        // Set state awal sesuai preferensi tersimpan
        when (ThemeManager.getThemeMode(this)) {
            ThemeManager.MODE_LIGHT -> radioGroup.check(R.id.radioLight)
            ThemeManager.MODE_DARK -> radioGroup.check(R.id.radioDark)
            else -> radioGroup.check(R.id.radioSystem)
        }
        switchAnimation.isChecked = ThemeManager.isAnimationEnabled(this)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.radioLight -> ThemeManager.MODE_LIGHT
                R.id.radioDark -> ThemeManager.MODE_DARK
                else -> ThemeManager.MODE_SYSTEM
            }
            ThemeManager.setThemeMode(this, mode)
            recreate()
        }

        switchAnimation.setOnCheckedChangeListener { _, isChecked ->
            ThemeManager.setAnimationEnabled(this, isChecked)
        }
    }
}
