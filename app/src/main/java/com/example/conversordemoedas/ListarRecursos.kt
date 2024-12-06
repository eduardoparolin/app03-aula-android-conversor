package com.example.conversordemoedas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListarRecursos : AppCompatActivity() {
    lateinit var realText: TextView
    lateinit var dolarText: TextView
    lateinit var euroText: TextView
    lateinit var btcText: TextView
    lateinit var etcText: TextView
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_recursos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sharedPref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        realText = findViewById(R.id.reaisText)
        dolarText = findViewById(R.id.dolarText)
        euroText = findViewById(R.id.euroText)
        btcText = findViewById(R.id.btcText)
        etcText = findViewById(R.id.etcText)

        updateValues()

        val voltarButton: Button = findViewById(R.id.voltarButton)

        voltarButton.setOnClickListener(fun (_) {
            finish()
        })

    }

    fun updateValues() {
        val valueReais = sharedPref.getFloat("__VALUE_REAIS", 0F)
        realText.text = "R$" + valueReais.toString()
        val valueDolar = sharedPref.getFloat("__VALUE_DOLAR", 0F)
        dolarText.text = "R$" + valueDolar.toString()
        val valueEuro = sharedPref.getFloat("__VALUE_EURO", 0F)
        euroText.text = "R$" + valueEuro.toString()
        val valueBtc = sharedPref.getFloat("__VALUE_BTC", 0F)
        btcText.text = "R$" + valueBtc.toString()
        val valueEtc = sharedPref.getFloat("__VALUE_ETC", 0F)
        etcText.text = "R$" + valueEtc.toString()
    }
}