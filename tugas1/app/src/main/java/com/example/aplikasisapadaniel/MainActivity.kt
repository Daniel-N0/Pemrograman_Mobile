package com.example.aplikasisapadaniel

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNama = findViewById<EditText>(R.id.etNama)
        val btnSapa = findViewById<Button>(R.id.btnSapa)
        val tvHasil = findViewById<TextView>(R.id.tvHasil)
        val switchMode = findViewById<Switch>(R.id.switchMode)
        val mainLayout = findViewById<LinearLayout>(R.id.mainLayout)

        btnSapa.setOnClickListener {
            val nama = etNama.text.toString()
            if (nama.isEmpty()) {
                tvHasil.text = "Nama harus diisi!"
            } else {
                tvHasil.text = "Hello $nama!"
            }
        }

        switchMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mainLayout.setBackgroundColor(Color.BLACK)
                tvHasil.setTextColor(Color.WHITE)
                switchMode.setTextColor(Color.WHITE)
                etNama.setTextColor(Color.WHITE)
            } else {
                mainLayout.setBackgroundColor(Color.WHITE)
                tvHasil.setTextColor(Color.BLACK)
                switchMode.setTextColor(Color.BLACK)
                etNama.setTextColor(Color.BLACK)
            }
        }
    }
}