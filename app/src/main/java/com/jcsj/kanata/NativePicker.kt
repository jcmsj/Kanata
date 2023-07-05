package com.jcsj.kanata

import android.widget.NumberPicker
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun NativePicker(
    maxValue: Int,
    value: MutableState<Int>,
    minValue: Int = 0,
    textColor: Color = colorScheme.primary,
    textSize: TextUnit = typography.displayLarge.fontSize
) {

    AndroidView(factory = {
        NumberPicker(it).apply {
            this.maxValue = maxValue
            this.value = value.value
            this.minValue = minValue
            setTextColor(textColor.toArgb())
            setTextSize(textSize.value)
            setOnValueChangedListener { _, _, newVal ->
                value.value = newVal
            }
        }
    }, update = {
        it.apply {
            this.value = value.value
            this.maxValue = maxValue
            this.minValue = minValue
        }
    })
}