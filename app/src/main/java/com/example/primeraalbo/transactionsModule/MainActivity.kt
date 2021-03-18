package com.example.primeraalbo.transactionsModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.primeraalbo.PrimeraalboApplication
import com.example.primeraalbo.R
import com.example.primeraalbo.common.AppCompactActivityCoroutines
import com.example.primeraalbo.common.dataModel.CategoryPercentList
import com.example.primeraalbo.common.entities.TransactionEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import java.lang.invoke.MethodHandles
import kotlin.math.log

class MainActivity : AppCompactActivityCoroutines() {

    private val url =  "https://gist.githubusercontent.com/astrocumbia/06ec83050ec79170b10a11d1d4924dfe/raw/ad791cddcff6df2ec424bfa3da7cdb86f266c57e/transactions.json"
    private var resultado1 = ""
    private var resultado2 = ""
    private var resultado3 = ""
    private var resultado4 = ""
    private var resultado5:MutableList<CategoryPercentList> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn: Button = findViewById(R.id.button)

        getTransactionsData()

        var mesesMap : MutableMap<String, String> = mutableMapOf()
        mesesMap.put("ENERO","2020-01%")
        mesesMap.put("FEBRERO","2020-02%")
        mesesMap.put("MARZO","2020-03%")
        mesesMap.put("ABRIL","2020-04%")
        mesesMap.put("MAYO","2020-05%")
        mesesMap.put("JUNIO","2020-06%")
        mesesMap.put("JULIO","2020-07%")
        mesesMap.put("AGOSTO","2020-08%")
        mesesMap.put("SEPTIEMBRE","2020-09%")
        mesesMap.put("OCTUBRE","2020-10%")
        mesesMap.put("NOVIEMBRE","2020-11%")
        mesesMap.put("DICIEMBRE","2020-12%")
        Log.d("POINT",mesesMap.toString())

        btn.setOnClickListener {

            launch {
                for (mes in mesesMap){
                    generateReport(mes.key, mes.value)
                }
            }

        }
    }

    private fun generateReport(montName:String,montYear:String) {

        resultado1 = PrimeraalboApplication.database.transactionDao().getTotalPendingTrans(montYear)
        resultado2 = PrimeraalboApplication.database.transactionDao().getTotalRejectedTrans(montYear)
        resultado3 = PrimeraalboApplication.database.transactionDao().getTotalIngresos(montYear)
        resultado4 = PrimeraalboApplication.database.transactionDao().getTotalGastos(montYear)
        resultado5 = PrimeraalboApplication.database.transactionDao().getGastosCategoryPercentList(montYear)


        Log.i("POINT-6", "===================================================")
        Log.i("POINT-0", montName)
        Log.i("POINT-1", resultado1 + " Transacciones pendientes")
        Log.i("POINT-2", resultado2 + " Transacciones Bloqueadas")
        Log.i("POINT-3", "$" + resultado3 + " ingresos")
        Log.i("POINT-4", "$" + resultado4 + " gastos")
        for (res in resultado5) {
            Log.i("POINT-5", "  " + res.category + " " + res.porcentaje + "%")
        }

    }

    private fun getTransactionsData() {

        var jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null, { response ->

            val jsonList = response.toString();
            if(jsonList != null) {
                val mutableList = object : TypeToken<MutableList<TransactionEntity>>() {}.type
                val transactionList = Gson().fromJson<MutableList<TransactionEntity>>(jsonList, mutableList)
                //Log.i("HOLA", transactionList[0].toString())
                saveJSONData(transactionList)
            }
            else{

            }
        },{
            it.printStackTrace()
        })

        PrimeraalboApplication.transactionsApi.addToRequestQueue(jsonObjectRequest)
    }

    fun saveJSONData(transactionList:MutableList<TransactionEntity>){

        doAsync {
            PrimeraalboApplication.database.transactionDao().insertAll(transactionList)
            PrimeraalboApplication.database.transactionDao().UpdateDatesFormart()
            uiThread {
                Log.i("POINT", "HOLA HECHO")
            }
        }

    }


}