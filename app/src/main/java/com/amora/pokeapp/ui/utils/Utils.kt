package com.amora.pokeapp.ui.utils

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun Boolean?.orFalse() = this ?: false

fun Int?.convertWeight(): String {
    val weight = this?.toDouble()?.div(10)
    return "$weight KG"
}

fun Int?.convertHeight(): String {
    val height = this?.toDouble()?.div(10)
    return "$height M"
}

fun randomColor(): Color {
    val random = Random
    return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))
}