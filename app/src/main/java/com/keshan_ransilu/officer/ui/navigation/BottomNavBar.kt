package com.keshan_ransilu.officer.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ripple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.ui.theme.*

@Composable
fun CustomBottomNavBar(
    currentRoute: String? = "home",
    onHomeClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onFilterClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val isHome = currentRoute == "home"
    val isNotifications = currentRoute == "notifications"
    val isSearch = currentRoute == "global_search"
    val isProfile = currentRoute == "profile"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
        ) {
            Column(modifier = Modifier.navigationBarsPadding()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(
                        iconRes = R.drawable.ic_round_nav_home,
                        contentDescription = "Dashboard",
                        isSelected = isHome,
                        onClick = onHomeClick
                    )

                    BottomNavItem(
                        icon = Icons.Default.Search,
                        contentDescription = "Search",
                        isSelected = isSearch,
                        onClick = onSearchClick
                    )

                    Spacer(modifier = Modifier.width(52.dp))

                    BottomNavItem(
                        iconRes = R.drawable.ic_notification_bell,
                        contentDescription = "Notifications",
                        isSelected = isNotifications,
                        onClick = onNotificationClick
                    )

                    BottomNavItem(
                        iconRes = R.drawable.ic_round_nav_profile,
                        contentDescription = "Officer Profile",
                        isSelected = isProfile,
                        onClick = onProfileClick
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .offset(y = (-20).dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(3.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF00E5FF),
                                Color(0xFF00B0FF)
                            )
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = Color.White),
                        onClick = onFilterClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_filter_fab),
                    contentDescription = "Quick Modules Switcher",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    iconRes: Int? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = HeaderBluePrimary
    val inactiveColor = Color(0xFF9AA4B2)
    val tintColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else inactiveColor,
        animationSpec = tween(durationMillis = 200),
        label = "navItemColor"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) HeaderBluePrimary.copy(alpha = 0.08f) else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "navItemBg"
    )

    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.15f)),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                tint = tintColor,
                modifier = Modifier.size(24.dp)
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tintColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
