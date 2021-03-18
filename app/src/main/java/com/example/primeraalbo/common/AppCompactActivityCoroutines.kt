package com.example.primeraalbo.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class AppCompactActivityCoroutines :  CoroutineScope, AppCompatActivity(){

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        job = SupervisorJob()
    }

    override fun onDestroy(){
        super.onDestroy()
        job.cancel()
    }

}