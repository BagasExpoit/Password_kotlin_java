package com.dev.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var txtPassword: TextView
    private lateinit var txtLengthValue: TextView
    private lateinit var seekLength: SeekBar
    private lateinit var chkUppercase: CheckBox
    private lateinit var chkLowercase: CheckBox
    private lateinit var chkNumbers: CheckBox
    private lateinit var chkSymbols: CheckBox
    private lateinit var btnTogglePassword: ImageButton

    private var currentPassword: String = ""
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applySavedTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        txtPassword = findViewById(R.id.txtGeneratedPassword)
        txtLengthValue = findViewById(R.id.txtLengthValue)
        seekLength = findViewById(R.id.seekLength)
        chkUppercase = findViewById(R.id.chkUppercase)
        chkLowercase = findViewById(R.id.chkLowercase)
        chkNumbers = findViewById(R.id.chkNumbers)
        chkSymbols = findViewById(R.id.chkSymbols)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)

        val btnGenerate = findViewById<MaterialButton>(R.id.btnGenerate)
        val btnCopy = findViewById<MaterialButton>(R.id.btnCopy)

        seekLength.progress = 12
        txtLengthValue.text = seekLength.progress.toString()

        seekLength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = if (progress < 4) 4 else progress
                txtLengthValue.text = value.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnGenerate.setOnClickListener { generatePassword() }
        btnCopy.setOnClickListener { copyToClipboard() }
        btnTogglePassword.setOnClickListener { togglePasswordVisibility() }

        generatePassword()
    }

    private fun generatePassword() {
        val length = txtLengthValue.text.toString().toIntOrNull() ?: 12
        // Memanggil kelas Java PasswordUtils dari Kotlin
        currentPassword = PasswordUtils.generate(
            length,
            chkUppercase.isChecked,
            chkLowercase.isChecked,
            chkNumbers.isChecked,
            chkSymbols.isChecked
        )
        // Password baru selalu disembunyikan dulu demi keamanan
        isPasswordVisible = false
        updatePasswordDisplay()
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        updatePasswordDisplay()
    }

    private fun updatePasswordDisplay() {
        if (isPasswordVisible) {
            txtPassword.text = currentPassword
            btnTogglePassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        } else {
            txtPassword.text = "•".repeat(currentPassword.length)
            btnTogglePassword.setImageResource(android.R.drawable.ic_menu_view)
        }
    }

    private fun copyToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("password", currentPassword)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, getString(R.string.copied_toast), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
