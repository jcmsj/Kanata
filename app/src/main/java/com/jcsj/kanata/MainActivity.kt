package com.jcsj.kanata

import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jcsj.kanata.ui.theme.KanataTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KanataTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainLayout()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(title = {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        )
        {
            Text("Kanata")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout() {
    val hours = rememberSaveable {
        mutableStateOf(0)
    }
    val minutes = rememberSaveable {
        mutableStateOf(5)
    }
    val time = rememberSaveable {
        mutableStateOf(
            (hours.value * 60 + minutes.value).toLong()
        )
    }
    LaunchedEffect(hours.value, minutes.value) {
        time.value = (hours.value * 60 + minutes.value).toLong()
    }

    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomAppBar() {
                BotBar {
                    Controls(time.value)
                }
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxHeight()
        ) {
            Body(hours, minutes)
        }
    }
}

@Composable
fun Body(hours: MutableState<Int>, minutes: MutableState<Int>) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .padding(top=40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("HH")
            NativePicker(24, hours, textSize = 65.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("MM")
            NativePicker(60, minutes, textSize = 65.sp)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BotBar(content: @Composable() (RowScope.() -> Unit)) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
        content = content
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Controls(time: Long) {
    val ctx = LocalContext.current
    val permissions = rememberMultiplePermissionsState(
        listOf(
            BLUETOOTH,
            BLUETOOTH_ADMIN,
        )
    )

    IconButton(onClick = {
        if (permissions.allPermissionsGranted) {
            schedBTOff(ctx)(time)
            val toast = Toast.makeText(
                ctx,
                "Scheduling BT off in $time minute/s",
                Toast.LENGTH_LONG
            )
            toast.show()
        } else {
            permissions.launchMultiplePermissionRequest()
        }
    }) {
        Icon(
            Icons.Filled.Bluetooth,
            contentDescription = "bluetooth"
        )
    }
}

