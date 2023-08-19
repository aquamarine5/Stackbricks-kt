package com.aquarngd.stackbricks.demoapp

import android.icu.util.VersionInfo
import java.util.Dictionary

class MsgPvderManager {
    companion object{
        private val MsgPvderMatchDict= mapOf<String,IMsgPvder>(
            WeiboCmtsMsgPvder.MsgPvderID to WeiboCmtsMsgPvder()
        )
        fun ParseFromId(msgPvderId:String):IMsgPvder?{
            return MsgPvderMatchDict[msgPvderId]
        }
    }
}
data class UpdateMessage(
    val version:VersionInfo,
    val pkgPvderId:String,
    val pkgPvderData:String
)
interface IMsgPvder{
    fun GetUpdateMessage(msgPvderData:String):UpdateMessage
    val ID:String
}