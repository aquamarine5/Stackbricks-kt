package com.aquarngd.stackbricks.demoapp

import android.content.Context
import android.icu.util.VersionInfo
import okhttp3.OkHttpClient

class StackbricksService(val context: Context,val msgPvderId:String,val msgPvderData:String){
    companion object{
        val okHttpClient=OkHttpClient()
    }
    var updatePackage:UpdatePackage?=null
    var updateMessage:UpdateMessage?=null
    fun getMsgPvder():IMsgPvder?{
        return MsgPvderManager.ParseFromId(msgPvderId)
    }
    fun getPkgPvder(pkgPvderId:String):IPkgPvder?{
        return PkgPvderManager.ParseFromId(pkgPvderId)
    }
    fun checkUpdate():Boolean{
        val currentVersion= VersionInfo.getInstance(context.packageManager.getPackageInfo(context.packageName, 0).versionName)
        return getUpdateMessage().version>currentVersion
    }
    fun updateWhenAvailable():Boolean{
        return if(checkUpdate()){
            getUpdatePackage().InstallApk(context)
            true
        }else false
    }
    fun getUpdatePackage():UpdatePackage{
        return if(updatePackage!=null)
            updatePackage!!
        else{
            updatePackage=getPkgPvder(getUpdateMessage().pkgPvderId)!!
                .DownloadPackage(context,getUpdateMessage(),getUpdateMessage().pkgPvderData)
            updatePackage!!
        }
    }
    fun getUpdateMessage():UpdateMessage{
        return if(updateMessage!=null)
            updateMessage!!
        else{
            updateMessage=getMsgPvder()!!.GetUpdateMessage(msgPvderData)
            updateMessage!!
        }
    }
}