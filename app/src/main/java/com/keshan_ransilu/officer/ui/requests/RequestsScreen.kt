package com.keshan_ransilu.officer.ui.requests

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.repository.RegisterRepository
import com.keshan_ransilu.officer.ui.components.EmptyStateCard
import com.keshan_ransilu.officer.ui.components.IllustratedStateScreen
import com.keshan_ransilu.officer.ui.home.HeaderBackgroundFaceted
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun RequestsScreen(
    onBackClick: () -> Unit,
    onAddRequestClick: () -> Unit,
    onRequestClick: (String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RegisterRepository(context) }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var records by remember { mutableStateOf(emptyList<JsonObject>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }

    val statusFilters = listOf("All", "Pending", "Approved", "In Progress", "Completed")

    LaunchedEffect(Unit) {
        records = repository.getAll("letters")
    }

    val filteredRecords = remember(records, searchQuery, selectedStatus) {
        records.filter { record ->
            val matchesQuery = if (searchQuery.isBlank()) true else {
                record.values.any { it.jsonPrimitive.content.contains(searchQuery, ignoreCase = true) }
            }
            val recordStatus = record["status"]?.jsonPrimitive?.content.orEmpty()
            val matchesStatus = if (selectedStatus == "All") true else {
                recordStatus.equals(selectedStatus, ignoreCase = true)
            }
            matchesQuery && matchesStatus
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
                    text = "Requests & Letters",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.18f)
                ) {
                    Text(
                        text = "${filteredRecords.size} Records",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
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
                    // Search Bar
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_letters),
                                contentDescription = null,
                                tint = HeaderBluePrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search requests by Ref No, Subject, or Name...",
                                        color = TextSecondary,
                                        fontSize = 13.sp,
                                        maxLines = 1
                                    )
                                }
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    cursorBrush = SolidColor(HeaderBluePrimary),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            if (searchQuery.isNotBlank()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Flat Status Filter Chips (No shadows)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(statusFilters) { status ->
                            val isSelected = status == selectedStatus
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) HeaderBluePrimary else Color.White,
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = if (isSelected) Color.White else HeaderBluePrimary.copy(alpha = 0.15f)),
                                    onClick = { selectedStatus = status }
                                )
                            ) {
                                Text(
                                    text = status,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    if (filteredRecords.isEmpty()) {
                        if (searchQuery.isNotBlank()) {
                            com.keshan_ransilu.officer.ui.components.IllustratedStateScreen(
                                title = "Result not found",
                                subtitle = "Please try again with another keyword or check the reference number",
                                iconRes = R.drawable.ic_state_search_empty,
                                primaryActionText = "Clear Search",
                                onPrimaryAction = { searchQuery = "" }
                            )
                        } else {
                            com.keshan_ransilu.officer.ui.components.IllustratedStateScreen(
                                title = "No requests yet",
                                subtitle = "When you have citizen requests or official letters, you'll see them here",
                                iconRes = R.drawable.ic_state_empty_box,
                                primaryActionText = "New Request",
                                onPrimaryAction = onAddRequestClick
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 120.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filteredRecords, key = { it["id"]?.jsonPrimitive?.content ?: it.hashCode().toString() }) { record ->
                                val refNo = record["refNo"]?.jsonPrimitive?.content.orEmpty().ifBlank { "Request Entry" }
                                val subject = record["subject"]?.jsonPrimitive?.content.orEmpty()
                                val status = record["status"]?.jsonPrimitive?.content.orEmpty().ifBlank { "Pending" }
                                val date = record["date"]?.jsonPrimitive?.content.orEmpty()
                                val id = record["id"]?.jsonPrimitive?.content.orEmpty()

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.08f)),
                                            onClick = { onRequestClick(id) }
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
                                            painter = painterResource(id = R.drawable.ic_round_letters),
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
                                                    text = refNo,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = when (status.lowercase()) {
                                                        "approved", "completed" -> Color(0xFFE8F5E9)
                                                        "rejected" -> Color(0xFFFFEBEE)
                                                        else -> Color(0xFFFFF3E0)
                                                    }
                                                ) {
                                                    Text(
                                                        text = status,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = when (status.lowercase()) {
                                                            "approved", "completed" -> Color(0xFF2E7D32)
                                                            "rejected" -> Color(0xFFC62828)
                                                            else -> Color(0xFFEF6C00)
                                                        },
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            if (subject.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = subject,
                                                    fontSize = 13.sp,
                                                    color = TextSecondary,
                                                    maxLines = 1
                                                )
                                            }

                                            if (date.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = date,
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

        // Add Request Action Button
        FloatingActionButton(
            onClick = onAddRequestClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 96.dp, end = 20.dp)
                .size(56.dp),
            containerColor = HeaderBluePrimary,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Request",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
