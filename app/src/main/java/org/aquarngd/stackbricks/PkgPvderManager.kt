package org.aquarngd.stackbricks

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import org.aquarngd.stackbricks.GhproxyPkgPvder
import org.aquarngd.stackbricks.UpdateMessage
import java.io.File


class PkgPvderManager {
    companion object {
        private val MsgPvderMatchDict = mapOf<String, IPkgPvder>(
            GhproxyPkgPvder.PkgPvderID to GhproxyPkgPvder()
        )

        fun ParseFromId(msgPvderId: String): IPkgPvder? {
            return MsgPvderMatchDict[msgPvderId]
        }
    }
}

data class UpdatePackage(val apkFile: File) {
    fun InstallApk(context: Context) {
        Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".file_provider",
                    apkFile
                ), "application/vnd.android.package-archive"
            )
            context.startActivity(this)
        }
    }
}

interface IPkgPvder {
    val ID: String
    suspend fun DownloadPackage(
        context: Context,
        updateMessage: UpdateMessage,
        pkgPvderData: String
    ): UpdatePackage
}