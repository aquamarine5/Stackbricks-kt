package com.aquarngd.stackbricks.demoapp

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.sentry.Sentry
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class StackbricksService(githubUser: String, githubRepo: String) {
    private val _githubRepo = githubRepo
    private val _githubUser = githubUser
    private val _githubCombined = "$_githubUser/$_githubRepo"

    private val okHttpClient = OkHttpClient()

    init {

    }

    public fun checkUpdate(publishTime:Date,context: Context): Boolean {
        val result=false
        val req=Request.Builder().url("https://api.github.com/repos/$_githubCombined/releases/latest").build()
        okHttpClient.newCall(req)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }

                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful){
                        //println(response.body!!.string())
                        val json=JSONObject(response.body!!.string())
                        if(SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ROOT).parse(json.getString("published_at").replace("T"," ").replace("Z",""))!! >publishTime){
                            println(json.getJSONArray("assets").getJSONObject(0).getString("browser_download_url"))
                            DownloadPackage(json.getJSONArray("assets").getJSONObject(0).getString("browser_download_url"),context)
                            println(111)
                        }

                    }
                }
            })
        return false
    }
    fun DownloadPackage(url:String,context:Context){
        val request = Request.Builder()
            .url(url)
            .build()
        println(url)
        okHttpClient.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                throw e
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    Sentry.captureMessage("Received response")
                    response.body?.byteStream()?.use{input->
                        var f=File(context.filesDir,"app-release.apk")
                        FileOutputStream(f).use{output->
                            input.copyTo(output)
                        }
                        println(f.absolutePath)
                    }
                }

            }
        })

    }
    public fun updateWhenAvailable(): Boolean {
        return false
    }
}