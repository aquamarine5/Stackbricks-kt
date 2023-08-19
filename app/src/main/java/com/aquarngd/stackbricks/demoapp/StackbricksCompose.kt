package com.aquarngd.stackbricks.demoapp

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class StackbricksCompose(
    val coroutineScope: CoroutineScope,
    val context: Context,
    msgPvderId: String,
    msgPvderData: String
) {
    val stackbricksService = StackbricksService(context, msgPvderId, msgPvderData)
    private val buttonColorMatchMap = mapOf(
        StackbricksStatus.STATUS_START to Color(81, 196, 211),
        StackbricksStatus.STATUS_CHECKING to Color(81, 196, 211),
        StackbricksStatus.STATUS_CLICKINSTALL to Color(236, 138, 164),
        StackbricksStatus.STATUS_ERROR to Color(238, 72, 102),
        StackbricksStatus.STATUS_DOWNLOADING to Color(248, 223, 112),
        StackbricksStatus.STATUS_NEWVERSION to Color(248, 223, 112),
        StackbricksStatus.STATUS_NEWEST to Color(127, 255, 212)
    )
    private val tipsTextMatchMap = mapOf(
        StackbricksStatus.STATUS_NEWEST to context.getString(R.string.tips_newest),
        StackbricksStatus.STATUS_NEWVERSION to context.getString(R.string.tips_newversion),
        StackbricksStatus.STATUS_START to context.getString(R.string.tips_checkupdate),
        StackbricksStatus.STATUS_ERROR to context.getString(R.string.tips_programerror),
        StackbricksStatus.STATUS_NETWORKERROR to context.getString(R.string.tips_networkerror),
        StackbricksStatus.STATUS_CLICKINSTALL to context.getString(R.string.tips_clickinstall),
        StackbricksStatus.STATUS_DOWNLOADING to context.getString(R.string.tips_downloading),
        StackbricksStatus.STATUS_CHECKING to context.getString(R.string.tips_checking)
    )

    private var mStatus = StackbricksStatus.STATUS_START
    var status: StackbricksStatus
        get() {
            return mStatus
        }
        set(value) {
            mStatus = value
            buttonColor = buttonColorMatchMap[mStatus]!!
            tipsText = tipsTextMatchMap[mStatus]!!
        }

    var buttonColor by mutableStateOf(buttonColorMatchMap[mStatus]!!)
    var tipsText by mutableStateOf(tipsTextMatchMap[mStatus]!!)
    suspend fun onClickEvent() {
        when (mStatus) {
            StackbricksStatus.STATUS_START, StackbricksStatus.STATUS_NEWEST -> {
                status = StackbricksStatus.STATUS_CHECKING
                val result = stackbricksService.checkUpdate()
                if (result) {
                    status = StackbricksStatus.STATUS_NEWVERSION
                } else {
                    status = StackbricksStatus.STATUS_NEWEST
                }
            }
            StackbricksStatus.STATUS_NEWVERSION->{
                status=StackbricksStatus.STATUS_DOWNLOADING
                stackbricksService.getUpdatePackage()
                status=StackbricksStatus.STATUS_CLICKINSTALL
            }
            StackbricksStatus.STATUS_CLICKINSTALL->{
                stackbricksService.getUpdatePackage().InstallApk(context)
                status=StackbricksStatus.STATUS_NEWEST
            }
            else -> {}
        }
    }

    @Composable
    fun DrawCompose() {

        Button(
            onClick = {
                coroutineScope.launch {
                    onClickEvent()
                }
            },
            colors = ButtonDefaults.buttonColors(buttonColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(36.dp)
        ) {
            Row {
                Icon(
                    ContextCompat.getDrawable(LocalContext.current, R.drawable.stackbricks_logo)!!
                        .toBitmap().asImageBitmap(), "logo"
                )
                Column() {
                    Text(tipsText, textAlign = TextAlign.Left)
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun preview() {
    StackbricksCompose(
        rememberCoroutineScope(),
        LocalContext.current, WeiboCmtsMsgPvder.MsgPvderID, "4936409558027888"
    ).DrawCompose()

}