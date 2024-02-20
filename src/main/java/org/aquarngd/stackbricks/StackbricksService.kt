package org.aquarngd.stackbricks

import android.content.Context
import android.icu.util.VersionInfo
import okhttp3.OkHttpClient
import org.aquarngd.stackbricks.IMsgPvder
import org.aquarngd.stackbricks.MsgPvderManager
import org.aquarngd.stackbricks.UpdateMessage

class StackbricksService(val context: Context, val msgPvderId: String, val msgPvderData: String) {
    companion object {
        val okHttpClient = OkHttpClient()
    }

    var mUpdatePackage: UpdatePackage? = null
    var mUpdateMessage: UpdateMessage? = null
    fun getMsgPvder(): IMsgPvder? {
        return MsgPvderManager.ParseFromId(msgPvderId)
    }

    fun getPkgPvder(pkgPvderId: String): IPkgPvder? {
        return PkgPvderManager.ParseFromId(pkgPvderId)
    }

    suspend fun checkUpdate(): Boolean {
        val currentVersion = VersionInfo.getInstance(
            context.packageManager.getPackageInfo(
                context.packageName,
                0
            ).versionName
        )
        return getUpdateMessage().version > currentVersion
    }

    suspend fun updateWhenAvailable(): Boolean {
        return if (checkUpdate()) {
            getUpdatePackage().InstallApk(context)
            true
        } else false
    }

    suspend fun getUpdatePackage(): UpdatePackage {
        return if (mUpdatePackage != null)
            mUpdatePackage!!
        else {
            mUpdatePackage = getPkgPvder(getUpdateMessage().pkgPvderId)!!
                .DownloadPackage(context, getUpdateMessage(), getUpdateMessage().pkgPvderData)
            mUpdatePackage!!
        }
    }

    suspend fun getUpdateMessage(): UpdateMessage {
        return if (mUpdateMessage != null)
            mUpdateMessage!!
        else {
            mUpdateMessage = getMsgPvder()!!.GetUpdateMessage(msgPvderData)
            mUpdateMessage!!
        }
    }
}