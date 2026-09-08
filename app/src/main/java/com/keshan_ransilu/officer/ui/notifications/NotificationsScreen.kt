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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.repository.NotificationModel
import com.keshan_ransilu.officer.repository.NotificationRepository
import com.keshan_ransilu.officer.ui.components.EmptyStateCard
import com.keshan_ransilu.officer.ui.components.IllustratedStateScreen
import com.keshan_ransilu.officer.ui.home.HeaderBackgroundFaceted
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onNavigateToModule: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { NotificationRepository(context) }
    val scope = rememberCoroutineScope()

    var notifications by remember { mutableStateOf<List<NotificationModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Unread", "Registers")

    fun loadData() {
        scope.launch {
            isLoading = true
            notifications = repository.getAll()
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadData()
    }

    val filteredNotifications = remember(selectedCategory, notifications) {
        when (selectedCategory) {
            "All" -> notifications
            "Unread" -> notifications.filter { it.isUnread }
            else -> notifications.filter { it.category.contains(selectedCategory, ignoreCase = true) }
        }
    }

    val unreadCount = remember(notifications) { notifications.count { it.isUnread } }

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

                if (unreadCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = Color.White),
                            onClick = {
                                scope.launch {
                                    repository.markAllAsRead()
                                    loadData()
                                }
                            }
                        )
                    ) {
                        Text(
                            text = "$unreadCount New",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
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
                    // Category Filter Pills
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(categories) { category ->
                                val isSelected = category == selectedCategory
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) HeaderBluePrimary else Color.White,
                                    modifier = Modifier.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(
                                            bounded = true,
                                            color = if (isSelected) Color.White else HeaderBluePrimary.copy(alpha = 0.15f)
                                        ),
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

                        if (notifications.isNotEmpty()) {
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        repository.clearAll()
                                        loadData()
                                    }
                                }
                            ) {
                                Text(
                                    text = "Clear All",
                                    fontSize = 12.sp,
                                    color = Color(0xFFD32F2F),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = HeaderBluePrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    } else if (filteredNotifications.isEmpty()) {
                        IllustratedStateScreen(
                            title = "No Notifications",
                            subtitle = "Whenever records or certificates are registered or updated, official alerts will appear here.",
                            iconRes = R.drawable.ic_state_empty_box,
                            primaryActionText = "Refresh",
                            onPrimaryAction = { loadData() }
                        )
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
                                            onClick = {
                                                scope.launch {
                                                    repository.markAsRead(item.id)
                                                    loadData()
                                                }
                                                item.targetModuleId?.let { target ->
                                                    onNavigateToModule(target)
                                                }
                                            }
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
                                                    fontSize = 14.sp,
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