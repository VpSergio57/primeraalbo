package com.example.primeraalbo

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.primeraalbo.common.database.TransactionDataBase
import com.example.primeraalbo.common.database.TransactionsApi

class PrimeraalboApplication: Application() {

        companion object{
            lateinit var database: TransactionDataBase
            lateinit var transactionsApi: TransactionsApi
        }

        override fun onCreate() {
            super.onCreate()
//            database = Room.databaseBuilder(this,
//            TransactionDataBase::class.java,
//            "TransactionDatabase")
//            .build()
            Log.d("HOLA", "AQUI INSTANCE")
            database = TransactionDataBase.getDatabase(this)

            transactionsApi = TransactionsApi.getInstance(this)


        }

}