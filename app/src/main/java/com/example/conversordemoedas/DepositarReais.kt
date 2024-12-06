package com.example.conversordemoedas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DepositarReais : AppCompatActivity() {
    lateinit var addSaldoEdit: EditText
    lateinit var saldoAtualText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_depositar_reais)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        saldoAtualText = findViewById(R.id.saldoAtualText)
        val salvarButton: Button = findViewById(R.id.salvarButton)
        addSaldoEdit = findViewById(R.id.addSaldoEdit)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        updateSaldo(sharedPref)
        salvarButton.setOnClickListener(fun(_) {
            val floatVal = addSaldoEdit.text.toString().toFloat()
            val current = sharedPref.getFloat("__VALUE_REAIS", 0F)
            sharedPref.edit().putFloat("__VALUE_REAIS", current + floatVal).apply()
            addSaldoEdit.setText("")
            updateSaldo(sharedPref)
        })

        addSaldoEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println(count)
                salvarButton.isEnabled = (addSaldoEdit.text.length > 0)
            }
        })
    }

    fun updateSaldo(sharedPref: SharedPreferences) {
        val value = sharedPref.getFloat("__VALUE_REAIS", 0F)
        saldoAtualText.text = "R$" + value.toString()
    }
}