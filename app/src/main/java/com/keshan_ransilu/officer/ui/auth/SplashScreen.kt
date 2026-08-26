package com.keshan_ransilu.officer.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit
) {
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val transitionState = remember { MutableTransitionState(false) }

    LaunchedEffect(Unit) {
        transitionState.targetState = true
        delay(1600)
        onNavigateNext()
    }

    val transition = rememberTransition(transitionState, label = "splashTransition")
    val alphaAnim by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 800, easing = FastOutSlowInEasing) },
        label = "alpha"
    ) { state -> if (state) 1f else 0f }

    val scaleAnim by transition.animateFloat(
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow) },
        label = "scale"
    ) { state -> if (state) 1f else 0.82f }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEEF4FF),
                        Color.White,
                        Color(0xFFF0F4FF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Center Minimalist Emblem & Official Branding
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .scale(scaleAnim)
                    .alpha(alphaAnim)
            ) {
                // Official Emblem Card with soft blue accent border
                Box(
                    modifier = Modifier
                        .size(112.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.5.dp, Color(0xFFD4E2FF), CircleShape)
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_splash_badge),
                        contentDescription = "Official Emblem",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                Text(
                    text = "Officer Portal",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center
                )

            }

            // Bottom Progress & Real-time Watermark
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
                    .alpha(alphaAnim)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = HeaderBluePrimary,
                    strokeWidth = 2.5.dp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "© $currentYear Developed By E Marketing Paradice",
                    fontSize = 10.sp,
                    color = TextSecondary.copy(alpha = 0.65f)
                )
            }
        }
    }
}
