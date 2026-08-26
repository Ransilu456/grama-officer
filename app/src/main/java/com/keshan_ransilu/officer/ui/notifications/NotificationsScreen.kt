package com.keshan_ransilu.officer.ui.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ripple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.ui.home.HeaderBackgroundFaceted
import com.keshan_ransilu.officer.ui.theme.*

data class NotificationItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val timestamp: String,
    val category: String,
    val iconRes: Int,
    val isUnread: Boolean = false
)

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onNavigateToModule: (String) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Unread", "Registers", "Circulars")

    val allNotifications = remember {
        listOf(
            NotificationItem(
                id = "1",
                title = "New Citizen Registration",
                subtitle = "Kasun Perera registered in Division 142 (Persons)",
                timestamp = "10 mins ago",
                category = "Registers",
                iconRes = R.drawable.ic_round_person,
                isUnread = true
            ),
            NotificationItem(
                id = "2",
                title = "Transport Permit Application",
                subtitle = "Sand & Timber permit request pending approval",
                timestamp = "1 hour ago",
                category = "Registers",
                iconRes = R.drawable.ic_round_permits,
                isUnread = true
            ),
            NotificationItem(
                id = "3",
                title = "Aswasuma Phase 2 Circular",
                subtitle = "Divisional Secretariat guideline for beneficiary review",
                timestamp = "Today, 9:30 AM",
                category = "Circulars",
                iconRes = R.drawable.ic_round_aswasuma,
                isUnread = false
            ),
            NotificationItem(
                id = "4",
                title = "Official Inward Letter",
                subtitle = "Land valuation inquiry ref #GN/L/2026/089",
                timestamp = "Yesterday",
                category = "Registers",
                iconRes = R.drawable.ic_round_letters,
                isUnread = false
            ),
            NotificationItem(
                id = "5",
                title = "Senior Citizen ID Issued",
                subtitle = "Elder ID #SC/142/045 ready for collection",
                timestamp = "2 days ago",
                category = "Registers",
                iconRes = R.drawable.ic_round_senior,
                isUnread = false
            )
        )
    }

    val filteredNotifications = remember(selectedCategory) {
        when (selectedCategory) {
            "All" -> allNotifications
            "Unread" -> allNotifications.filter { it.isUnread }
            else -> allNotifications.filter { it.category == selectedCategory }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HeaderBluePrimary)
    ) {
        HeaderBackgroundFaceted(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = Color.White),
                            onClick = onBackClick
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.18f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Text(
                    text = "Notifications",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.18f)
                ) {
                    Text(
                        text = "${allNotifications.count { it.isUnread }} New",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            // Curved Main Body Sheet
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = ScreenBg,
                shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    // Category Filter Pills (No shadows)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            val isSelected = category == selectedCategory
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) HeaderBluePrimary else Color.White,
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = if (isSelected) Color.White else HeaderBluePrimary.copy(alpha = 0.15f)),
                                    onClick = { selectedCategory = category }
                                )
                            ) {
                                Text(
                                    text = category,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    // Notification Items List (Flat Cards, No Shadows, No Borders)
                    if (filteredNotifications.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            com.keshan_ransilu.officer.ui.components.EmptyStateCard(
                                iconRes = R.drawable.ic_empty_notifications,
                                title = "No notifications yet",
                                description = "When you get notifications or circular updates, they'll show up here",
                                actionText = "Refresh",
                                onActionClick = { selectedCategory = "All" }
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 120.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                        items(filteredNotifications, key = { it.id }) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.08f)),
                                        onClick = {}
                                    ),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Illustrated Icon
                                    Image(
                                        painter = painterResource(id = item.iconRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(42.dp)
                                    )

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = item.title,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            if (item.isUnread) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFFFF5252))
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(3.dp))

                                        Text(
                                            text = item.subtitle,
                                            fontSize = 12.sp,
                                            color = TextSecondary,
                                            lineHeight = 16.sp
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = item.timestamp,
                                            fontSize = 11.sp,
                                            color = TextSecondary.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}