package org.aquarngd.stackbricks

import android.icu.util.VersionInfo
import org.aquarngd.stackbricks.msgpvder.WeiboCommentsMsgPvder

class MsgPvderManager {
    companion object {
        private val MsgPvderMatchDict = mapOf<String, IMsgPvder>(
            WeiboCommentsMsgPvder.MsgPvderID to WeiboCommentsMsgPvder()
        )

        fun parseFromId(msgPvderId: String): IMsgPvder? {
            return MsgPvderMatchDict[msgPvderId]
        }
    }
}

data class UpdateMessage(
    val version: VersionInfo,
    val pkgPvderId: String,
    val pkgPvderData: String
)

interface IMsgPvder {
    suspend fun getUpdateMessage(msgPvderData: String): ExceptionalResult<UpdateMessage>
    val id: String
}