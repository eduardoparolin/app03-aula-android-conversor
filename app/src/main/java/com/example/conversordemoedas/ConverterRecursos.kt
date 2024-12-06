package com.example.conversordemoedas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class ConverterRecursos : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var origemText: TextView
    lateinit var destinoText: TextView
    lateinit var destinoValueText: TextView
    lateinit var tempText: TextView
    lateinit var origemEdit: EditText
    lateinit var calcularButton: Button
    lateinit var converterButton: Button
    private lateinit var requestQueue: RequestQueue
    private lateinit var progressBar: ProgressBar
    lateinit var sharedPref: SharedPreferences
    lateinit var origem: Spinner
    lateinit var destino: Spinner

    var finalValue = 0F

    var origemId: String = "BRL"
    var destinoId: String = "USD"

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
        sharedPref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        origemText = findViewById(R.id.origemText)
        destinoText = findViewById(R.id.destinoText)
        tempText = findViewById(R.id.tempText)
        destinoValueText = findViewById(R.id.destinoValueText)
        origemEdit = findViewById(R.id.origemEdit)
        calcularButton = findViewById(R.id.calcularButton)
        calcularButton.isEnabled = false
        converterButton = findViewById(R.id.converterButton)
        converterButton.isEnabled = false

        converterButton.setOnClickListener(fun(_) {
            var available = checkSaldo()
            if (available) {
                //salvar
                val floatOrigemVal = origemEdit.text.toString().toFloat()
                val current = sharedPref.getFloat("__VALUE_${origemId}", 0F)
                val toUpdate = sharedPref.getFloat("__VALUE_${destinoId}", 0F)
                sharedPref.edit().putFloat("__VALUE_${origemId}", current - floatOrigemVal).apply()
                sharedPref.edit().putFloat("__VALUE_${destinoId}", toUpdate + finalValue).apply()
                finish()
            } else {
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show()
                origemEdit.setText("")
                converterButton.isEnabled = false
                finalValue = 0F
                destinoValueText.setText("--")
            }
        })

        requestQueue = Volley.newRequestQueue(this)

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

        calcularButton.setOnClickListener(fun(_) {
            callApi()
        })

        val voltarButton: Button = findViewById(R.id.voltarButton)

        voltarButton.setOnClickListener(fun(_) {
            finish()
        })

        origem = findViewById(R.id.spinner)
        destino = findViewById(R.id.spinner2)

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

        destino.setSelection(1)
    }

    private fun callApi() {
        progressBar.visibility = View.VISIBLE

        if ((origemId == "BTC" && destinoId == "ETH") || (origemId == "ETH" && destinoId == "BTC")) {
            Toast.makeText(this, "Impossível converter criptomoeda diretamente", Toast.LENGTH_SHORT).show()
            origemEdit.setText("")
            converterButton.isEnabled = false
            finalValue = 0F
            destinoValueText.setText("--")
            origem.setSelection(0)
            destino.setSelection(1)
        }

        if (origemId == destinoId) {
            conversionRate = 1F
            converterButton.isEnabled = true
            val conversion = origemEdit.text.toString().toFloat() * conversionRate
            destinoValueText.setText(conversion.toString())
            progressBar.visibility = View.GONE
            return
        }
        var url = "https://economia.awesomeapi.com.br/json/last/${origemId}-${destinoId}"
        //edge cases
        var invert = false
        if (destinoId == "BTC" || destinoId == "ETH") {
            url = "https://economia.awesomeapi.com.br/json/last/${destinoId}-${origemId}"
            invert = true
        }

//        var urlBtcBrl = "https://economia.awesomeapi.com.br/json/last/BTC-BRL"
//        var urlBtcUsd = "https://economia.awesomeapi.com.br/json/last/BTC-USD"
//        var urlBtcEur = "https://economia.awesomeapi.com.br/json/last/BTC-EUR"
//        var urlEthBrl = "https://economia.awesomeapi.com.br/json/last/BTC-BRL"
//        var urlEthUsd = "https://economia.awesomeapi.com.br/json/last/BTC-USD"
//        var urlEtcEur = "https://economia.awesomeapi.com.br/json/last/BTC-EUR"


        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->

                val gson = Gson()
                val type = object :
                    com.google.gson.reflect.TypeToken<Map<String, CurrencyDetails>>() {}.type
                val currencyMap: Map<String, CurrencyDetails> = gson.fromJson(response, type)

                val dynamicKey = currencyMap.keys.firstOrNull()
                val currencyDetails = dynamicKey?.let { currencyMap[it] }

                if (currencyDetails != null) {
                    conversionRate = currencyDetails.bid.toFloat()
                    converterButton.isEnabled = true
                    val conversion = if (invert) (origemEdit.text.toString()
                        .toFloat() / conversionRate) else (origemEdit.text.toString()
                        .toFloat() * conversionRate)
                    destinoValueText.setText(String.format("%,.6f", conversion))
                    finalValue = conversion
                    progressBar.visibility = View.GONE
//                    tempText.text = """
//                    Key: $dynamicKey
//                    Name: ${currencyDetails.name}
//                    High: ${currencyDetails.high}
//                    Low: ${currencyDetails.low}
//                    Bid: ${currencyDetails.bid}
//                    Ask: ${currencyDetails.ask}
//                    Percentage Change: ${currencyDetails.pctChange}
//                    Last Updated: ${currencyDetails.create_date}
//                """.trimIndent()
                } else {
//                    tempText.text = "No data found."
                }
            },
            { error ->
                // Hide the ProgressBar
                progressBar.visibility = View.GONE

//                textView.text = "Error: ${error.message}"
            }
        )

        requestQueue.add(stringRequest)
    }

    fun checkSaldo(): Boolean {
        val saldo = sharedPref.getFloat("__VALUE_${origemId}", 0F)
        return saldo >= origemEdit.text.toString().toFloat()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel all requests when the activity is destroyed
        requestQueue.cancelAll { true }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        origemEdit.setText("")
        destinoValueText.setText("--")
        converterButton.isEnabled = false

        val item = parent.getItemAtPosition(pos)

        var id = "BRL"
        if (item.toString() == "Real") {
            id = "BRL"
        } else if (item.toString() == "Dolar") {
            id = "USD"
        } else if (item.toString() == "Euro") {
            id = "EUR"
        } else if (item.toString() == "Bitcoin") {
            id = "BTC"
        } else if (item.toString() == "Ethereum") {
            id = "ETH"
        }

        if (parent.id == R.id.spinner) {
            origemText.text = item.toString()
            origemId = id
        } else {
            destinoText.text = item.toString()
            destinoId = id
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback.
    }
}

data class CurrencyDetails(
    val code: String,
    val codein: String,
    val name: String,
    val high: String,
    val low: String,
    val varBid: String,
    val pctChange: String,
    val bid: String,
    val ask: String,
    val timestamp: String,
    val create_date: String
)