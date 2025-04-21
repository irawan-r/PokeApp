package com.amora.pokeapp.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent

@Composable
fun NetworkImage(
	url: String?,
	modifier: Modifier = Modifier,
	circularRevealEnabled: Boolean = false,
	contentScale: ContentScale = ContentScale.Fit
) {
	CoilImage(
		imageModel = { url },
		modifier = modifier,
		imageOptions = ImageOptions(contentScale = contentScale),
		loading = {
			Box(modifier = modifier.matchParentSize()) {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center),
					color = MaterialTheme.colorScheme.primary
				)
			}
		},
		component = rememberImageComponent {
			if (circularRevealEnabled) {
				+CircularRevealPlugin()
			}
			+CrossfadePlugin()
		},
		failure = {
			Column(
				modifier = modifier,
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Image request failed",
					style = MaterialTheme.typography.labelMedium
				)
			}
		}
	)
}