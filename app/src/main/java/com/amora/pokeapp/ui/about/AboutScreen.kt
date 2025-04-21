package com.amora.pokeapp.ui.about

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amora.pokeapp.BuildConfig
import com.amora.pokeapp.ui.main.MainViewModel
import kotlin.random.Random


@Composable
fun AboutScreen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }
    var isSparked by remember { mutableStateOf(false) }
    val bounce = remember { Animatable(1f) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val nameColor = remember { Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f) }
        Text(
            text = buildAnnotatedString {
                append("Welcome ")
                withStyle(SpanStyle(color = nameColor)) {
                    append(currentUser?.name ?: "Trainer")
                }
                append("!, what pokemon are you gonna catch?")
            },
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LaunchedEffect(isSparked) {
            if (isSparked) {
                bounce.animateTo(
                    targetValue = 1.2f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                bounce.animateTo(1f)
                isSparked = false
            }
        }
        Text(
            text = "App Version: ${BuildConfig.VERSION_NAME}",
            fontSize = 18.sp,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = bounce.value,
                    scaleY = bounce.value,
                    alpha = if (isSparked) 0.5f else 1f
                )
                .clickable {
                    isSparked = true
                }
        )

    }
}