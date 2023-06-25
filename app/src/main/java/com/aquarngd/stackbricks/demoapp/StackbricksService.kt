package com.aquarngd.stackbricks.demoapp

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
                        val json=JSONObject(response.body!!.string())
                        if(SimpleDateFormat("yyyy-MM-ddThh:mm:ssZ", Locale.ROOT).parse(json.getString("published_at"))!! >publishTime){
                            DownloadPackage(json.getJSONArray("assets").getJSONObject(0).getString("browser_download_url"),context)
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
        okHttpClient.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                throw e
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    response.body?.byteStream()?.use{input->
                        FileOutputStream(File(context.filesDir,"app-release.apk")).use{output->
                            input.copyTo(output)
                        }
                    }
                }
            }
        })

    }
    public fun updateWhenAvailable(): Boolean {
        return false
    }
}