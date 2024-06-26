package org.aquarngd.stackbricks

import android.icu.util.VersionInfo
import org.aquarngd.stackbricks.msgpvder.GithubApiMsgPvder
import org.aquarngd.stackbricks.msgpvder.WeiboCommentsMsgPvder

class MsgPvderManager {
    companion object {
        private val MsgPvderMatchDict = mapOf(
            WeiboCommentsMsgPvder.MsgPvderID to WeiboCommentsMsgPvder(),
            GithubApiMsgPvder.MsgPvderID to GithubApiMsgPvder()
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