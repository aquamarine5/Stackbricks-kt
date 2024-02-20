package org.aquarngd.stackbricks

import android.accounts.NetworkErrorException
import android.icu.util.VersionInfo
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject

class WeiboCommentsMsgPvder : IMsgPvder {
    override val ID: String
        get() = MsgPvderID

    companion object {
        const val MsgPvderID = "stbkt.msgpvder.weibocmts"
    }

    override suspend fun GetUpdateMessage(msgPvderData: String): UpdateMessage {
        var data: List<String> = listOf()
        val request = Request.Builder()
            .url("https://weibo.com/ajax/statuses/buildComments?flow=1&is_reload=1&id=${msgPvderData}&is_show_bulletin=2&is_mix=0&count=10&fetch_level=0")
            .build()
        return withContext(Dispatchers.IO) {
            StackbricksService.okHttpClient.newCall(request).execute().apply {
                if (isSuccessful) {
                    val json = JSONObject(body!!.string())
                    Log.d(WeiboCommentsMsgPvder::class.simpleName, json.toString())
                    data = json.getJSONArray("data").getJSONObject(0).getString("text").split(";;")
                } else throw NetworkErrorException("a")
            }
            UpdateMessage(VersionInfo.getInstance(data[1]), data[2], data[3])
        }

    }
}