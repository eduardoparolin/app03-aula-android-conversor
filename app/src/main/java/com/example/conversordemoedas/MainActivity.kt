package com.example.conversordemoedas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val value = sharedPref.getInt("__MONEY_VALUE_REAIS", 0)

        val depositarReaisButton: Button = findViewById(R.id.depositarReaisButton)
        val listarRecursosButton: Button = findViewById(R.id.listarRecursosButton)
        val converterRecursosButton: Button = findViewById(R.id.converterRecursosButton)
        val moneyValueText: TextView = findViewById(R.id.moneyValue)
        moneyValueText.text = "R$" + value.toString()

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
}