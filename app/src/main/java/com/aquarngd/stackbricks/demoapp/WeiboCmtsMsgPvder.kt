package com.aquarngd.stackbricks.demoapp

import android.icu.util.VersionInfo
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class WeiboCmtsMsgPvder() :IMsgPvder {
    override val ID: String
        get() = MsgPvderID
    companion object{

        const val MsgPvderID="stbkt.msgpvder.weibocmts"
    }
    override fun GetUpdateMessage(msgPvderData: String): UpdateMessage {
        var data:List<String> = listOf()
        val request=Request.Builder()
            .url("https://weibo.com/ajax/statuses/buildComments?flow=1&is_reload=1&id=${msgPvderData}&is_show_bulletin=2&is_mix=0&count=10&fetch_level=0")
            .build()
        StackbricksService.okHttpClient.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                throw e
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val json = JSONObject(response.body!!.string())
                    data=json.getJSONArray("data").getJSONObject(0).getString("text").split(";;")

                }
            }
        })
        return UpdateMessage(VersionInfo.getInstance(data[1]),data[2],data[3])
    }
}