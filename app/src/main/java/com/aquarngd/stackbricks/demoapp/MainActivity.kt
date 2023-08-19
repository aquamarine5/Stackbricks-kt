package com.aquarngd.stackbricks.demoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.aquarngd.stackbricks.demoapp.ui.theme.StackbricksDemoTheme
import io.sentry.Sentry
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sentry.close()
        setContent {
            StackbricksDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StackbricksDemo()

                }
            }
        }
    }
}

@Composable
fun ShowLogos() {

}
@Composable
fun Stackbricks(){
    var buttonText by remember { mutableStateOf("检查更新") }
    var tipsText by remember { mutableStateOf("检查更新") }
    val coroutineScope= rememberCoroutineScope()
    var c = LocalContext.current
    val stackbricksService = StackbricksService(c, WeiboCmtsMsgPvder.MsgPvderID, "4936409558027888")
    Button(
        onClick = {},
        colors= ButtonDefaults.buttonColors(containerColor = Color(81,196,211)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(36.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(0.dp,15.dp)
            ) {
                Icon(
                    ContextCompat.getDrawable(LocalContext.current, R.drawable.stackbricks_logo)!!
                        .toBitmap().asImageBitmap(),
                    contentDescription = "Stackbricks Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(100.dp)
                )
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(tipsText)
                }
            }
            Image(
                painter = painterResource(id = R.drawable.developedby_rngdcreation),
                contentDescription = "Developed by RenegadeCreation",
                alignment = Alignment.BottomEnd,
                modifier = Modifier
                    .scale(1f)
                    .padding(20.dp, 5.dp, 0.dp, 5.dp)
            )
        }

    }
}

@Composable
fun StackbricksDemo() {
    Column {
        StackbricksCompose( rememberCoroutineScope(),LocalContext.current, WeiboCmtsMsgPvder.MsgPvderID, "4936409558027888").DrawCompose()
        ShowLogos()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StackbricksPreview() {
    StackbricksDemoTheme {
        StackbricksDemo()
    }
}