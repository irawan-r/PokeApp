package com.amora.pokeapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale

@Composable
private fun rememberPosterPainter(url: String?): Painter {
    return rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .scale(Scale.FIT)
            .diskCachePolicy(coil.request.CachePolicy.ENABLED)
            .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
            .networkCachePolicy(coil.request.CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    )
}

@Composable
fun NetworkImage(url: String?, modifier: Modifier = Modifier) {
    val painter = rememberPosterPainter(url)
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Inside,
        modifier = modifier
    )
}