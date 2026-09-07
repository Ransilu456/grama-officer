package com.keshan_ransilu.officer.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1400)
        onNavigateNext()
    }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "splashAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Center Clean Emblem & Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(alphaAnim)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4F7FC))
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_splash_badge),
                        contentDescription = "Official Emblem",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Officer Portal",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "ග්‍රාම නිලධාරී පරිපාලන පද්ධතිය",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // Bottom Minimalist Progress & Watermark
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp)
                    .alpha(alphaAnim)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = HeaderBluePrimary,
                    strokeWidth = 2.2.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "© $currentYear Developed By E Marketing Paradice",
                    fontSize = 11.sp,
                    color = TextSecondary.copy(alpha = 0.6f)
                )
            }
        }
    }
}
