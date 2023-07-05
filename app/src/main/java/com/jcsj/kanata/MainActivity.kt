package com.jcsj.kanata

import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
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
    val time = remember {mutableStateOf(5L)}
    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomAppBar() {
                Controls(time.value)
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Row() {
                Body(time)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Body(time:MutableState<Long>) {
    var text by remember {
        mutableStateOf("")
    }
    TextField(
        value = text,
        modifier = Modifier.fillMaxWidth(),
        label = {
                Text("Timeout:")
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        onValueChange = {
            text = it
            if (it.length > 1 && it.isDigitsOnly() ) {
                time.value = it.toLong()
            }
        }
    )

}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Controls(time:Long) {
    val ctx = LocalContext.current
    val permissions = rememberMultiplePermissionsState(
        listOf(
            BLUETOOTH,
            BLUETOOTH_ADMIN,
        )
    )
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            if (permissions.allPermissionsGranted) {
                schedBTOff(ctx)(time)
                val toast = Toast.makeText(ctx, "Scheduling BT off in $time minute/s", Toast.LENGTH_LONG)
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KanataTheme {
        Controls(5)
    }
}