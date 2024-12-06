package com.example.conversordemoedas

import android.app.ComponentCaller
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var moneyValueText: TextView
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val depositarReaisButton: Button = findViewById(R.id.depositarReaisButton)
        val listarRecursosButton: Button = findViewById(R.id.listarRecursosButton)
        val converterRecursosButton: Button = findViewById(R.id.converterRecursosButton)
        moneyValueText = findViewById(R.id.moneyValue)

        sharedPref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        updateSaldo()


        depositarReaisButton.setOnClickListener(fun (_) {
            val intent = Intent(this, DepositarReais::class.java)

            startActivity(intent)
        })
        listarRecursosButton.setOnClickListener(fun (_) {
            val intent = Intent(this, ListarRecursos::class.java)
            startActivity(intent)
        })
        converterRecursosButton.setOnClickListener(fun (_) {
            val intent = Intent(this, ConverterRecursos::class.java)
            startActivity(intent)
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        updateSaldo()
    }

    fun updateSaldo() {
        val value = sharedPref.getFloat("__VALUE_REAIS", 0F)

        moneyValueText.text = "R$" + value.toString()
    }
}