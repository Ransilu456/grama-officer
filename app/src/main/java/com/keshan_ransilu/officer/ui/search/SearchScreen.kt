package com.keshan_ransilu.officer.ui.search

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.data.registry.RegisterCatalog
import com.keshan_ransilu.officer.repository.RegisterRepository
import com.keshan_ransilu.officer.ui.components.EmptyStateCard
import com.keshan_ransilu.officer.ui.home.HeaderBackgroundFaceted
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

data class SearchResultItem(
    val moduleId: String,
    val moduleTitle: String,
    val iconRes: Int,
    val recordId: String,
    val primaryText: String,
    val secondaryText: String,
    val badgeText: String
)

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onRecordClick: (String, String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RegisterRepository(context) }
    val focusManager = LocalFocusManager.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedModuleFilter by remember { mutableStateOf("All") }
    var allRecordsMap by remember { mutableStateOf(mapOf<String, List<JsonObject>>()) }

    LaunchedEffect(Unit) {
        val map = mutableMapOf<String, List<JsonObject>>()
        RegisterCatalog.forEach { mod ->
            map[mod.id] = repository.getAll(mod.id)
        }
        allRecordsMap = map
    }

    val searchResults = remember(searchQuery, selectedModuleFilter, allRecordsMap) {
        if (searchQuery.trim().isBlank()) {
            emptyList()
        } else {
            val query = searchQuery.trim()
            val list = mutableListOf<SearchResultItem>()
            allRecordsMap.forEach { (modId, records) ->
                if (selectedModuleFilter == "All" || selectedModuleFilter == modId) {
                    val module = RegisterCatalog.find { it.id == modId }
                    records.forEach { record ->
                        val matches = record.values.any {
                            it.jsonPrimitive.content.contains(query, ignoreCase = true)
                        }
                        if (matches) {
                            val id = record["id"]?.jsonPrimitive?.content ?: ""
                            val titleField = module?.fields?.firstOrNull {
                                it.key.contains("name", true) || it.key.contains("item", true) || it.key.contains("refNo", true) || it.key.contains("subject", true)
                            }?.key ?: module?.fields?.firstOrNull()?.key ?: "id"
                            val primaryText = record[titleField]?.jsonPrimitive?.content ?: "Entry #$id"
                            val subField = module?.fields?.firstOrNull { it.key != titleField }?.key
                            val secondaryText = subField?.let { record[it]?.jsonPrimitive?.content }.orEmpty()
                            val statusText = record["status"]?.jsonPrimitive?.content ?: module?.titleEn ?: ""

                            list.add(
                                SearchResultItem(
                                    moduleId = modId,
                                    moduleTitle = module?.titleEn ?: modId,
                                    iconRes = module?.iconRes ?: R.drawable.ic_round_person,
                                    recordId = id,
                                    primaryText = primaryText,
                                    secondaryText = secondaryText,
                                    badgeText = statusText
                                )
                            )
                        }
                    }
                }
            }
            list
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
                    text = "Global Search",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.18f)
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "${searchResults.size} Found" else "Search",
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
                        .padding(top = 18.dp)
                ) {
                    // Aesthetic Search Bar
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
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
                                        text = "Search all citizens, permits, letters...",
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
                                Surface(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .clickable { searchQuery = "" },
                                    shape = CircleShape,
                                    color = Color(0xFFEFF2F8)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
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
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Register Filter Chips (No shadows)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            val isAll = selectedModuleFilter == "All"
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isAll) HeaderBluePrimary else Color.White,
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = if (isAll) Color.White else HeaderBluePrimary.copy(alpha = 0.15f)),
                                    onClick = { selectedModuleFilter = "All" }
                                )
                            ) {
                                Text(
                                    text = "All Registers",
                                    fontSize = 13.sp,
                                    fontWeight = if (isAll) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isAll) Color.White else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }

                        items(RegisterCatalog) { module ->
                            val isSelected = selectedModuleFilter == module.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) HeaderBluePrimary else Color.White,
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = if (isSelected) Color.White else HeaderBluePrimary.copy(alpha = 0.15f)),
                                    onClick = { selectedModuleFilter = module.id }
                                )
                            ) {
                                Text(
                                    text = module.titleEn,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    // Content View
                    if (searchQuery.isBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyStateCard(
                                iconRes = R.drawable.ic_empty_records,
                                title = "Search across division",
                                description = "Type a citizen name, NIC, vehicle number, or reference number to search all records",
                                actionText = null,
                                onActionClick = null
                            )
                        }
                    } else if (searchResults.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyStateCard(
                                iconRes = R.drawable.ic_empty_result,
                                title = "Result not found",
                                description = "Please try again with another keywords or maybe use generic term",
                                actionText = "Search again",
                                onActionClick = { searchQuery = "" }
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 40.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(searchResults, key = { "${it.moduleId}_${it.recordId}" }) { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.08f)),
                                            onClick = { onRecordClick(item.moduleId, item.recordId) }
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
                                                    text = item.primaryText,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = HeaderBluePrimary.copy(alpha = 0.08f)
                                                ) {
                                                    Text(
                                                        text = item.moduleTitle,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = HeaderBluePrimary,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            if (item.secondaryText.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = item.secondaryText,
                                                    fontSize = 13.sp,
                                                    color = TextSecondary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
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
}
