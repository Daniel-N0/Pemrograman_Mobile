package com.example.surveyshowcase

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

data class RespondentCard(
    val id: Int,
    val location: String,
    val rating: Int,
    val comment: String,
    val imageResId: Int
)

object SurveyTheme {
    val CyanColor = Color(0xFF00BCD4)
    val LightBlueColor = Color(0xFFC8E6F5)
    val PrimaryGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF00838F))
    )
    val CardBackground = Color.White
    val TextPrimary = Color(0xFF111111)
    val TextSecondary = Color(0xFF666666)
    val RatingFilled = Color(0xFFFFC107)
    val ModernFont = FontFamily.SansSerif
}

@Composable
fun SurveyShowcaseApp(
    onPlaySoundPool: () -> Unit,
    onPlayRestartSound: () -> Unit
) {
    MaterialTheme {
        StudentSatisfactionSurveyScreen(onPlaySoundPool, onPlayRestartSound)
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun StudentSatisfactionSurveyScreen(
    onPlaySoundPool: () -> Unit,
    onPlayRestartSound: () -> Unit
) {
    var isIntroView by remember { mutableStateOf(true) }

    val haptic = LocalHapticFeedback.current

    val respondentCards = listOf(
        RespondentCard(1, "Responden FEB", 5, "-", R.drawable.open_space_feb),
        RespondentCard(2, "Responden FKIP", 4, "-", R.drawable.open_space_fkip),
        RespondentCard(3, "Responden POLIBAN", 4, "Cukup bagus, tapi sayang sungainya kotor.", R.drawable.open_space_pb),
        RespondentCard(4, "Responden FISIP", 3, "Standar aja.", R.drawable.open_space_fisip),
        RespondentCard(5, "Responden FH", 3, "Terasa panas dan kurangnya tempat untuk berteduh.", R.drawable.open_space_fh)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurveyTheme.PrimaryGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HASIL SURVEI",
                fontFamily = SurveyTheme.ModernFont,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Kelompok 5",
                fontFamily = SurveyTheme.ModernFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = isIntroView,
                    transitionSpec = {
                        if (targetState) {
                            (slideInVertically(animationSpec = tween(600)) { height -> -height } + fadeIn(animationSpec = tween(600))).togetherWith(
                                slideOutVertically(animationSpec = tween(600)) { height -> height } + fadeOut(animationSpec = tween(600)))
                        } else {
                            (slideInVertically(animationSpec = tween(600)) { height -> height } + fadeIn(animationSpec = tween(600))).togetherWith(
                                slideOutVertically(animationSpec = tween(600)) { height -> -height } + fadeOut(animationSpec = tween(600)))
                        }
                    },
                    label = "IntroTransition"
                ) { targetIsIntro ->
                    if (targetIsIntro) {
                        IntroCardState(
                            onStartClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onPlaySoundPool()
                                isIntroView = false
                            }
                        )
                    } else {
                        RespondentCardPagerState(
                            cards = respondentCards,
                            onCardClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onPlaySoundPool()
                            },
                            onRestart = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onPlayRestartSound()
                                isIntroView = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IntroCardState(onStartClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.fillMaxWidth(0.75f).height(320.dp).offset(y = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(28.dp)
        ) {}
        Card(
            modifier = Modifier.fillMaxWidth(0.81f).height(340.dp).offset(y = 10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(28.dp)
        ) {}

        Card(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .wrapContentHeight()
                .shadow(16.dp, RoundedCornerShape(28.dp)),
            colors = CardDefaults.cardColors(containerColor = SurveyTheme.CardBackground),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.open_space),
                    contentDescription = "Foto Kelompok",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Seberapa puas Anda dengan Fasilitas Open Space?",
                        fontFamily = SurveyTheme.ModernFont,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurveyTheme.TextPrimary,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Button(
                        onClick = onStartClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurveyTheme.TextPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Mulai Review",
                            fontFamily = SurveyTheme.ModernFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RespondentCardPagerState(
    cards: List<RespondentCard>,
    onCardClick: () -> Unit,
    onRestart: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { cards.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 36.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val scale = lerp(
                            start = 0.82f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                        )
                        scaleX = scale
                        scaleY = scale

                        alpha = lerp(
                            start = 0.4f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                        )
                    }
                    .shadow(12.dp, RoundedCornerShape(28.dp))
                    .clickable {
                        val nextPage = pagerState.currentPage + 1
                        if (nextPage < cards.size) {
                            onCardClick()
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = nextPage,
                                    animationSpec = tween(durationMillis = 400)
                                )
                            }
                        }
                    },
                colors = CardDefaults.cardColors(containerColor = SurveyTheme.CardBackground),
                shape = RoundedCornerShape(28.dp)
            ) {
                RespondentCardContent(card = cards[page], pageOffset = pageOffset)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(cards.size) { iteration ->
                val isSelected = pagerState.currentPage == iteration
                val color = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f)
                val width = if (isSelected) 18.dp else 8.dp

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                        .size(width = width, height = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (pagerState.currentPage == cards.size - 1) {
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = SurveyTheme.TextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .shadow(6.dp, RoundedCornerShape(14.dp))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Ulang dari Awal",
                        fontFamily = SurveyTheme.ModernFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RespondentCardContent(card: RespondentCard, pageOffset: Float) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
        ) {
            Image(
                painter = painterResource(id = card.imageResId),
                contentDescription = "Foto ${card.location}",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = pageOffset * 250f
                    },
                contentScale = ContentScale.Crop
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = card.location.uppercase(),
                fontFamily = SurveyTheme.ModernFont,
                fontSize = 14.sp,
                color = SurveyTheme.CyanColor,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (index < card.rating) SurveyTheme.RatingFilled else Color(0xFFE0E0E0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF7F7F7))
                    .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "\"${card.comment}\"",
                    fontFamily = SurveyTheme.ModernFont,
                    fontSize = 14.sp,
                    color = SurveyTheme.TextPrimary,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}