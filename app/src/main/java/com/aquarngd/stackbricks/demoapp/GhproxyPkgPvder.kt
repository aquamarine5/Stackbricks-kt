package com.aquarngd.stackbricks.demoapp

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.Okio
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.net.IDN

class GhproxyPkgPvder : IPkgPvder {
    override val ID: String = PkgPvderID

    companion object {
        const val PkgPvderID = "stbkt.pkgpvder.ghproxy"
    }

    override suspend fun DownloadPackage(
        context: Context,
        updateMessage: UpdateMessage,
        pkgPvderData: String
    ): UpdatePackage {
        val apkFile: File = File.createTempFile(
            "stackbricks_apk_${updateMessage.version}",
            ".apk",
            context.cacheDir
        )

        val data = pkgPvderData.split("]]")
        val url =
            "https://ghproxy.com/github.com/${data[0]}/${data[1]}/releases/download/${data[2]}/${data[3]}"
        val req = Request.Builder()
            .url(url)
            .build()
        return withContext(Dispatchers.IO) {
            StackbricksService.okHttpClient.newCall(req).execute().apply {
                if (body != null) {
                    apkFile.sink().buffer().apply {
                        writeAll(body!!.source())
                        close()
                    }
                }
            }
            UpdatePackage(apkFile)
        }
    }
}
