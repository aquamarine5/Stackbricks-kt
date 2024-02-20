package org.aquarngd.stackbricks

import android.icu.util.VersionInfo

class MsgPvderManager {
    companion object {
        private val MsgPvderMatchDict = mapOf<String, IMsgPvder>(
            WeiboCommentsMsgPvder.MsgPvderID to WeiboCommentsMsgPvder()
        )

        fun ParseFromId(msgPvderId: String): IMsgPvder? {
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
    suspend fun GetUpdateMessage(msgPvderData: String): UpdateMessage
    val ID: String
}