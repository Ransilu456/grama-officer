package com.keshan_ransilu.officer.ui.modulelist

import androidx.compose.animation.*
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
import com.keshan_ransilu.officer.data.registry.RegisterModule
import com.keshan_ransilu.officer.repository.RegisterRepository
import com.keshan_ransilu.officer.ui.components.IllustratedStateScreen
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleListScreen(
    moduleId: String,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onRecordClick: (String) -> Unit,
    onMenuClick: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RegisterRepository(context) }
    val module = remember(moduleId) { RegisterCatalog.find { it.id == moduleId } }
    val scope = rememberCoroutineScope()

    var records by remember { mutableStateOf(emptyList<JsonObject>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var recordToDelete by remember { mutableStateOf<JsonObject?>(null) }

    val reload = {
        scope.launch {
            records = repository.getAll(moduleId)
        }
    }

    LaunchedEffect(moduleId) {
        records = repository.getAll(moduleId)
    }

    val filteredRecords = remember(records, searchQuery, selectedCategory) {
        records.filter { record ->
            val matchesQuery = if (searchQuery.isBlank()) true else {
                record.values.any { it.jsonPrimitive.content.contains(searchQuery, ignoreCase = true) }
            }
            val matchesCategory = if (selectedCategory == null) true else {
                record.values.any { it.jsonPrimitive.content.equals(selectedCategory, ignoreCase = true) }
            }
            matchesQuery && matchesCategory
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Screen Header (without notification icon)
            ListScreenHeader(
                title = module?.titleEn ?: "Search Records",
                subtitle = module?.titleSi ?: "",
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onBackClick = onBackClick
            )

            // Flat Category Filter Chips (No shadows, no borders)
            val dropdownField = module?.fields?.firstOrNull { it.options.isNotEmpty() }
            if (dropdownField != null && dropdownField.options.isNotEmpty()) {
                ScrollableCategoryRow(
                    options = dropdownField.options,
                    selectedOption = selectedCategory,
                    onOptionSelect = {
                        selectedCategory = if (selectedCategory == it) null else it
                    }
                )
            } else {
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Records List (Flat Cards, No Shadows, No Borders)
            if (filteredRecords.isEmpty()) {
                EmptyListPlaceholder(
                    query = searchQuery,
                    onAddClick = onAddClick,
                    onClearQuery = { searchQuery = "" }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 6.dp,
                        bottom = 120.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredRecords, key = { it["id"]?.jsonPrimitive?.content ?: it.hashCode().toString() }) { record ->
                        RecordListItemCard(
                            record = record,
                            module = module,
                            onClick = {
                                val id = record["id"]?.jsonPrimitive?.content ?: ""
                                onRecordClick(id)
                            },
                            onDelete = { recordToDelete = record }
                        )
                    }
                }
            }
        }

        // Floating Action Button
        if (filteredRecords.isNotEmpty()) {
            FloatingActionButton(
                onClick = onAddClick,
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
                    contentDescription = "Add New Record",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Delete Confirmation Dialog
        recordToDelete?.let { record ->
            AlertDialog(
                onDismissRequest = { recordToDelete = null },
                title = { Text("Delete Record", fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to permanently delete this record?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val id = record["id"]?.jsonPrimitive?.content ?: ""
                            scope.launch {
                                repository.delete(moduleId, id)
                                reload()
                                recordToDelete = null
                            }
                        }
                    ) {
                        Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { recordToDelete = null }) {
                        Text("Cancel")
                    }
                },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White
            )
        }
    }
}

@Composable
fun ListScreenHeader(
    title: String,
    subtitle: String,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Top Action Row (Notification icon removed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (Half-rounded 12.dp)
                Surface(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.15f)),
                            onClick = onBackClick
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = ScreenBg
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Centered Title & Sinhala Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (subtitle.isNotBlank()) {
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Robust Search Bar with BasicTextField (No shadows, no borders)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = ScreenBg
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
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search in $title...",
                                color = TextSecondary,
                                fontSize = 14.sp,
                                maxLines = 1
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchChange,
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
                            onClick = { onSearchChange("") },
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
        }
    }
}

/**
 * Flat Category Chips without drop shadows or borders
 */
@Composable
fun ScrollableCategoryRow(
    options: List<String>,
    selectedOption: String?,
    onOptionSelect: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            val isAllSelected = selectedOption == null
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isAllSelected) HeaderBluePrimary else Color.White,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = if (isAllSelected) Color.White else HeaderBluePrimary.copy(alpha = 0.15f)),
                    onClick = { onOptionSelect("") }
                )
            ) {
                Text(
                    text = "All",
                    fontSize = 13.sp,
                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isAllSelected) Color.White else TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        items(options) { option ->
            val isSelected = option == selectedOption
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) HeaderBluePrimary else Color.White,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = if (isSelected) Color.White else HeaderBluePrimary.copy(alpha = 0.15f)),
                    onClick = { onOptionSelect(option) }
                )
            ) {
                Text(
                    text = option,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
fun RecordListItemCard(
    record: JsonObject,
    module: RegisterModule?,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val titleField = module?.fields?.firstOrNull {
        it.key.contains("name", ignoreCase = true) ||
                it.key.contains("item", ignoreCase = true) ||
                it.key.contains("refNo", ignoreCase = true) ||
                it.key.contains("purpose", ignoreCase = true) ||
                it.key.contains("landName", ignoreCase = true) ||
                it.key.contains("houseNo", ignoreCase = true) ||
                it.key.contains("childName", ignoreCase = true) ||
                it.key.contains("officerName", ignoreCase = true) ||
                it.key.contains("orgName", ignoreCase = true) ||
                it.key.contains("contactName", ignoreCase = true)
    }?.key ?: module?.fields?.getOrNull(0)?.key ?: "id"

    val rateField = module?.fields?.firstOrNull {
        it.key.contains("rate", ignoreCase = true) ||
                it.key.contains("amount", ignoreCase = true) ||
                it.key.contains("monthlyAid", ignoreCase = true) ||
                it.key.contains("received", ignoreCase = true)
    }?.key

    val subtitleField = module?.fields?.firstOrNull {
        it.key != titleField && (
                it.key.contains("specialty", ignoreCase = true) ||
                        it.key.contains("nic", ignoreCase = true) ||
                        it.key.contains("phone", ignoreCase = true) ||
                        it.key.contains("subject", ignoreCase = true) ||
                        it.key.contains("vehicle", ignoreCase = true) ||
                        it.key.contains("date", ignoreCase = true) ||
                        it.key.contains("address", ignoreCase = true) ||
                        it.key.contains("category", ignoreCase = true) ||
                        it.key.contains("extent", ignoreCase = true) ||
                        it.key.contains("fvp", ignoreCase = true) ||
                        it.key.contains("dispute", ignoreCase = true)
                )
    }?.key ?: module?.fields?.getOrNull(1)?.key ?: ""

    val titleValue = record[titleField]?.jsonPrimitive?.content ?: "Record Entry"
    val subtitleValue = record[subtitleField]?.jsonPrimitive?.content ?: ""
    val rateValue = rateField?.let { record[it]?.jsonPrimitive?.content }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.08f)),
                onClick = onClick
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Illustrated Custom Vector Icon
            Image(
                painter = painterResource(id = module?.iconRes ?: R.drawable.ic_round_person),
                contentDescription = null,
                modifier = Modifier.size(46.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Details Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titleValue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Rate / Subtitle
                if (!rateValue.isNullOrBlank()) {
                    Text(
                        text = if (rateValue.startsWith("$") || rateValue.endsWith("/ hour")) rateValue else "$$rateValue / hour",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF00897B),
                        maxLines = 1
                    )
                } else if (subtitleValue.isNotBlank()) {
                    Text(
                        text = subtitleValue,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Gold Medal Badge
            Image(
                painter = painterResource(id = R.drawable.ic_medal_badge),
                contentDescription = "Rating Medal",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun EmptyListPlaceholder(
    query: String,
    onAddClick: () -> Unit,
    onClearQuery: () -> Unit = {}
) {
    if (query.isNotBlank()) {
        com.keshan_ransilu.officer.ui.components.IllustratedStateScreen(
            title = "Result not found",
            subtitle = "Please try again with another keyword or use generic terms",
            iconRes = R.drawable.ic_state_search_empty,
            primaryActionText = "Search again",
            onPrimaryAction = onClearQuery
        )
    } else {
        com.keshan_ransilu.officer.ui.components.IllustratedStateScreen(
            title = "No records yet",
            subtitle = "There are no records in this official register. Tap below to create your first entry.",
            iconRes = R.drawable.ic_state_empty_box,
            primaryActionText = "Add New Record",
            onPrimaryAction = onAddClick
        )
    }
}
