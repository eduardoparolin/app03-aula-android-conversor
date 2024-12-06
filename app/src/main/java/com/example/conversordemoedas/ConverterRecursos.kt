package com.example.conversordemoedas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConverterRecursos : AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    lateinit var origemText: TextView
    lateinit var destinoText: TextView
    lateinit var destinoValueText: TextView
    lateinit var origemEdit: EditText
    lateinit var calcularButton: Button
    lateinit var converterButton: Button

    var conversionRate: Float = 1F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_converter_recursos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        origemText = findViewById(R.id.origemText)
        destinoText = findViewById(R.id.destinoText)
        destinoValueText = findViewById(R.id.destinoValueText)
        origemEdit = findViewById(R.id.origemEdit)
        calcularButton = findViewById(R.id.calcularButton)
        calcularButton.isEnabled = false
        converterButton = findViewById(R.id.converterButton)
        converterButton.isEnabled = false

        origemEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calcularButton.isEnabled = (origemEdit.text.length > 0)
                converterButton.isEnabled = false
            }
        })

        calcularButton.setOnClickListener(fun (_) {
            //verificar valor na conta
            //buscar cotação
            converterButton.isEnabled = true
            val conversion = origemEdit.text.toString().toFloat() * conversionRate
            destinoValueText.setText(conversion.toString())
        })

        val voltarButton: Button = findViewById(R.id.voltarButton)

        voltarButton.setOnClickListener(fun (_) {
            finish()
        })

        val origem: Spinner = findViewById(R.id.spinner)
        val destino: Spinner = findViewById(R.id.spinner2)

        ArrayAdapter.createFromResource(
            this,
            R.array.money_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            origem.adapter = adapter
            destino.adapter = adapter
        }
        destino.onItemSelectedListener = this
        origem.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        origemEdit.setText("")
        destinoValueText.setText("--")
        converterButton.isEnabled = false
        val item = parent.getItemAtPosition(pos)
        if (parent.id == R.id.spinner) {
            origemText.text = item.toString()
        } else {
            destinoText.text = item.toString()
        }
        if (origemText.text == destinoText.text) {
            conversionRate = 1F
        } else {

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback.
    }
}