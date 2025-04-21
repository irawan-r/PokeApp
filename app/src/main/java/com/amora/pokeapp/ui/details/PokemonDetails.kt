package com.amora.pokeapp.ui.details

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.amora.pokeapp.persistence.entity.PokemonCompleteDetails
import com.amora.pokeapp.persistence.entity.PokemonCompleteDetails.Companion.getImageUrl
import com.amora.pokeapp.repository.model.PokeMark
import com.amora.pokeapp.ui.utils.convertHeight
import com.amora.pokeapp.ui.utils.convertWeight
import com.amora.pokeapp.ui.utils.randomColor
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PokemonDetails(
    poke: PokeMark,
    viewModel: DetailsViewModel,
    pressOnBack: () -> Unit = {}
) {
    var index by remember { mutableStateOf(poke.id) }
    val swipeAnim = remember { Animatable(0f) }

    LaunchedEffect(poke, index) {
        viewModel.loadPosterByName(poke.copy(id = index))
    }

    val details by viewModel.posterDetailsFlow.collectAsState(initial = null)

    details?.let {
        PokemonDetailsBody(
            poster = it,
            pressOnBack = pressOnBack,
            index = index,
            onSwipeIndexChanged = { newInd ->
                index = newInd
            },
            initialOffset = swipeAnim.value
        )
    }
}

@Composable
fun StatAnimatedProgressBar(statName: String, statValue: Int, maxStatValue: Int = 100) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(statValue) {
        val calculated = statValue / maxStatValue.toFloat()
        progress = calculated
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "Stat Progress Animation"
    )

    val startColor = Color(0xFF2196F3)
    val midColor = Color(0xFF9C27B0)
    val endColor = Color(0xFFF44336)

    val currentGradient = remember(animatedProgress) {
        val color1 = lerp(startColor, midColor, animatedProgress.coerceIn(0f, 0.5f) * 2)
        val color2 = lerp(midColor, endColor, (animatedProgress - 0.5f).coerceAtLeast(0f) * 2)
        listOf(color1, color2)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = statName,
            color = MaterialTheme.colorScheme.background,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .background(brush = Brush.horizontalGradient(currentGradient)),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (animatedProgress > 0.05f) {
                    val progressValue = (animatedProgress * maxStatValue).toInt()
                    val animatedStatValue = minOf(progressValue, maxStatValue)
                    val realProgress = if (animatedStatValue == maxStatValue) {
                        "Max"
                    } else {
                        "$animatedStatValue"
                    }
                    Text(
                        text = realProgress,
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonDetailsBody(
    poster: PokemonCompleteDetails,
    pressOnBack: () -> Unit = {},
    index: Int,
    onSwipeIndexChanged: (Int) -> Unit,
    initialOffset: Float = 0f
) {
    val randomColorBackground = remember { mutableStateOf(randomColor()) }
    val randomColorSubTitle = remember { mutableStateOf(randomColor()) }

    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(initialOffset) }

    val gestureModifier = Modifier.pointerInput(index) {
        detectHorizontalDragGestures(
            onHorizontalDrag = { _, dragAmount ->
                scope.launch {
                    offsetX.snapTo(offsetX.value + dragAmount)
                }
            },
            onDragEnd = {
                scope.launch {
                    val threshold = 200
                    when {
                        offsetX.value < -threshold -> {
                            onSwipeIndexChanged(index.inc())
                            offsetX.snapTo(0f)
                        }
                        offsetX.value > threshold && index > 1 -> {
                            onSwipeIndexChanged(index.dec())
                            offsetX.snapTo(0f)
                        }
                        else -> {
                            offsetX.animateTo(0f, animationSpec = tween(300))
                        }
                    }
                }
            },
            onDragCancel = {
                scope.launch {
                    offsetX.animateTo(0f, animationSpec = tween(300))
                }
            }
        )
    }

    Box(
        modifier = gestureModifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
    ) {
        PokemonDetailsContent(
            modifier = Modifier.fillMaxSize(),
            poster = poster,
            randomColorBackground = randomColorBackground.value,
            randomColorSubTitle = randomColorSubTitle.value,
            pressOnBack = pressOnBack
        )
    }
}

@Composable
private fun PokemonDetailsContent(
    modifier: Modifier = Modifier,
    poster: PokemonCompleteDetails,
    randomColorBackground: Color,
    randomColorSubTitle: Color,
    pressOnBack: () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(backgroundColor)
    ) {
        val (background, arrow, title, species, physics, content) = createRefs()
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            randomColorBackground,
                            randomColorBackground,
                            MaterialTheme.colorScheme.background
                        )
                    ),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 50.dp,
                        bottomStart = 50.dp
                    )
                )
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }

        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(poster.getImageUrl())
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier.aspectRatio(1.2f)
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            tint = MaterialTheme.colorScheme.onSecondary,
            contentDescription = null,
            modifier = Modifier
                .constrainAs(arrow) {
                    top.linkTo(parent.top)
                }
                .padding(12.dp)
                .statusBarsPadding()
                .clickable { pressOnBack() }
        )

        Text(
            text = poster.pokemonDetails.name.toString(),
            modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(title) {
                    top.linkTo(background.bottom)
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = 16.dp,
                        endMargin = 16.dp
                    )
                }
                .padding(8.dp),
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Row(
            modifier = Modifier
                .constrainAs(species) {
                    top.linkTo(title.bottom)
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = 16.dp,
                        endMargin = 16.dp
                    )
                }
                .padding(16.dp)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            poster.types.forEach {
                Text(
                    text = it.type?.name.toString(),
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(
                            randomColorSubTitle, shape = RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomEnd = 10.dp,
                                bottomStart = 10.dp
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .constrainAs(physics) {
                    top.linkTo(species.bottom)
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = poster.pokemonDetails.weight.convertWeight(),
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Weight",
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = poster.pokemonDetails.height.convertHeight(),
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Height",
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(physics.bottom)
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                }
                .padding(horizontal = 32.dp)
                .padding(bottom = 16.dp)
        ) {

            Text(
                text = "Base Stats",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
            )

            poster.stats.forEach { item ->

                StatAnimatedProgressBar(
                    statName = item.stats?.name.orEmpty(),
                    statValue = item.baseStat ?: 0
                )
            }
        }
    }
}
