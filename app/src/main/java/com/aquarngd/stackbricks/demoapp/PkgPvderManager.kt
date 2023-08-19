package com.aquarngd.stackbricks.demoapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.io.File


class PkgPvderManager {
    companion object{
        private val MsgPvderMatchDict= mapOf<String,IPkgPvder>(
            GhproxyPkgPvder.PkgPvderID to GhproxyPkgPvder()
        )
        fun ParseFromId(msgPvderId:String):IPkgPvder?{
            return MsgPvderMatchDict[msgPvderId]
        }
    }
}
data class UpdatePackage(val apkFile:File){
    fun InstallApk(context: Context){
        Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive")
            context.startActivity(this)
        }
    }
}
interface IPkgPvder{
    val ID:String
    fun DownloadPackage(context: Context, updateMessage: UpdateMessage, pkgPvderData: String):UpdatePackage
}