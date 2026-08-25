package com.dev.passwordgenerator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applySavedTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<Toolbar>(R.id.toolbarAbout)
        toolbar.setNavigationOnClickListener { finish() }

        val txtEmail = findViewById<TextView>(R.id.txtDevEmail)
        val txtGithub = findViewById<TextView>(R.id.txtDevGithub)

        // Ketuk email untuk membuka aplikasi email
        txtEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${txtEmail.text}")
            }
            startActivity(Intent.createChooser(intent, "Kirim email"))
        }

        // Ketuk tautan GitHub untuk membuka di browser
        txtGithub.setOnClickListener {
            val url = "https://" + txtGithub.text.toString()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}
