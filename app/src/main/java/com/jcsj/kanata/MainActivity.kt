package com.jcsj.kanata

import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.content.pm.PermissionInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.LteMobiledata
import androidx.compose.material.icons.filled.Wifi
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomAppBar() {
                Controls()
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Row() {
                Text(
                    text = "TODO"
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Controls() {
    val ctx = LocalContext.current

    val permissions = rememberMultiplePermissionsState(
        listOf(
            BLUETOOTH,
            BLUETOOTH_ADMIN,
            BLUETOOTH_CONNECT
        )
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            if (permissions.allPermissionsGranted) {
                val t = 1L
                scheduleBluetoothTurnOff(ctx, t)
                val toast = Toast.makeText(ctx, "Scheduling BT off in $t minute/s", Toast.LENGTH_LONG)
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
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Filled.Wifi,
                contentDescription = "wifi"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Filled.LteMobiledata,
                contentDescription = "Mobile Data"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KanataTheme {
        Controls()
    }
}