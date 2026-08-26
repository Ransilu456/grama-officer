package com.keshan_ransilu.officer.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.data.registry.DashboardGridItems
import com.keshan_ransilu.officer.data.registry.DashboardItem
import com.keshan_ransilu.officer.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    onModuleClick: (String) -> Unit,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit = onMenuClick
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HeaderBluePrimary)
    ) {
        // Geometric Faceted Polygonal Background Header
        HeaderBackgroundFaceted(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar & Dashboard Title Header
            HomeTopHeader(
                onMenuClick = onMenuClick,
                onProfileClick = onProfileClick
            )

            // Bottom White Curved Sheet
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp),
                        spotColor = Color(0x33000000)
                    ),
                color = ScreenBg,
                shape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 26.dp,
                        bottom = 120.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(DashboardGridItems, key = { it.id }) { item ->
                        DashboardCard(
                            item = item,
                            onClick = { onModuleClick(item.targetModuleId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTopHeader(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val dateStr = remember {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        formatter.format(Date())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Top Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stylized 3-line hamburger menu
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu_hamburger),
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Circular illustrated avatar badge
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true),
                        onClick = onProfileClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_avatar_header),
                    contentDescription = "Profile",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Title & Update Subtitle
        Text(
            text = "Dashboard",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Last Update $dateStr",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun DashboardCard(
    item: DashboardItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.96f)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = Color(0x18001970),
                ambientColor = Color(0x0C001970)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.15f)),
                onClick = onClick
            ),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Illustrated Custom Vector Icon
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.title,
                modifier = Modifier
                    .size(68.dp)
                    .padding(2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Main English Label
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8E9AA8),
                fontSize = 14.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Sinhala Subtitle
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color(0xFFB0B7C3),
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun HeaderBackgroundFaceted(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Deep blue gradient base
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    HeaderBlueDark,
                    HeaderBluePrimary,
                    HeaderBlueLight
                )
            )
        )

        // Faceted Triangle 1 (Top Left to Center)
        val path1 = Path().apply {
            moveTo(0f, 0f)
            lineTo(width * 0.7f, 0f)
            lineTo(width * 0.35f, height * 0.85f)
            close()
        }
        drawPath(
            path = path1,
            color = Color.White.copy(alpha = 0.04f)
        )

        // Faceted Triangle 2 (Top Right Angled)
        val path2 = Path().apply {
            moveTo(width * 0.45f, 0f)
            lineTo(width, 0f)
            lineTo(width, height * 0.9f)
            lineTo(width * 0.6f, height * 0.5f)
            close()
        }
        drawPath(
            path = path2,
            color = Color.White.copy(alpha = 0.06f)
        )

        // Subtle bottom triangle
        val path3 = Path().apply {
            moveTo(0f, height * 0.4f)
            lineTo(width * 0.4f, height)
            lineTo(0f, height)
            close()
        }
        drawPath(
            path = path3,
            color = Color.Black.copy(alpha = 0.08f)
        )
    }
}
