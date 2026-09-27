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
import com.keshan_ransilu.officer.ui.components.DisasterTypeBadge
import com.keshan_ransilu.officer.ui.components.IllustratedStateScreen
import com.keshan_ransilu.officer.ui.components.StatusBadge
import com.keshan_ransilu.officer.ui.home.HeaderBackgroundFaceted
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

data class GlobalSearchResult(
    val moduleId: String,
    val moduleTitle: String,
    val iconRes: Int,
    val recordId: String,
    val title: String,
    val subtitle: String,
    val statusBadge: String? = null,
    val disasterBadge: String? = null
)

@Composable
fun GlobalSearchScreen(
    onBackClick: () -> Unit = {},
    onRecordClick: (String, String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RegisterRepository(context) }
    val focusManager = LocalFocusManager.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedModuleFilter by remember { mutableStateOf("All") }
    var allRecordsMap by remember { mutableStateOf(mapOf<String, List<JsonObject>>()) }
    var houseLookup by remember { mutableStateOf(mapOf<String, String>()) }
    var personLookup by remember { mutableStateOf(mapOf<String, String>()) }

    LaunchedEffect(Unit) {
        val map = mutableMapOf<String, List<JsonObject>>()
        RegisterCatalog.forEach { mod ->
            map[mod.id] = repository.getAll(mod.id)
        }
        allRecordsMap = map

        val houses = map["house"].orEmpty()
        houseLookup = houses.associate { 
            (it["id"]?.jsonPrimitive?.content ?: "") to (it["houseNo"]?.jsonPrimitive?.content ?: "")
        }

        val persons = map["person"].orEmpty()
        personLookup = persons.associate { 
            (it["id"]?.jsonPrimitive?.content ?: "") to (it["fullName"]?.jsonPrimitive?.content ?: "")
        }
    }

    val searchResults = remember(searchQuery, selectedModuleFilter, allRecordsMap, houseLookup, personLookup) {
        val query = searchQuery.trim()
        if (query.isBlank()) {
            emptyList()
        } else {
            val list = mutableListOf<GlobalSearchResult>()
            allRecordsMap.forEach { (modId, records) ->
                if (selectedModuleFilter == "All" || selectedModuleFilter == modId) {
                    val module = RegisterCatalog.find { it.id == modId }
                    val moduleMatches = module?.titleSi?.contains(query, ignoreCase = true) == true ||
                                        module?.titleEn?.contains(query, ignoreCase = true) == true

                    records.forEach { record ->
                        val directMatch = record.values.any {
                            it.jsonPrimitive.content.contains(query, ignoreCase = true)
                        }

                        val relHouseNo = record["houseId"]?.jsonPrimitive?.content?.let { houseLookup[it] }
                        val houseMatch = relHouseNo != null && relHouseNo.contains(query, ignoreCase = true)

                        val relPersonName = record["householderId"]?.jsonPrimitive?.content?.let { personLookup[it] }
                        val personMatch = relPersonName != null && relPersonName.contains(query, ignoreCase = true)

                        if (directMatch || houseMatch || personMatch || moduleMatches) {
                            val id = record["id"]?.jsonPrimitive?.content ?: ""

                            // Intelligent Title resolution
                            val title = when (modId) {
                                "person" -> record["fullName"]?.jsonPrimitive?.content?.ifBlank { null }
                                    ?: record["nic"]?.jsonPrimitive?.content
                                    ?: "පුද්ගලයා #$id"
                                "house" -> "නිවස #${record["houseNo"]?.jsonPrimitive?.content ?: id}"
                                "cashbook" -> record["purpose"]?.jsonPrimitive?.content
                                    ?.let { "$it (${record["receiptNo"]?.jsonPrimitive?.content ?: ""})" }
                                    ?: record["payerPayeeNameAddress"]?.jsonPrimitive?.content ?: "මුදල් සටහන"
                                "subject_files" -> record["fileName"]?.jsonPrimitive?.content ?: record["fileNo"]?.jsonPrimitive?.content ?: "ලිපිගොනුව"
                                "vouchers_forms" -> "${record["formType"]?.jsonPrimitive?.content ?: "වවුචරය"} #${record["serialNo"]?.jsonPrimitive?.content ?: ""}"
                                "letters" -> "${record["senderOrReceiver"]?.jsonPrimitive?.content ?: ""} - ${record["letterRefNoAndDate"]?.jsonPrimitive?.content ?: ""}"
                                "land_gov" -> record["landName"]?.jsonPrimitive?.content ?: "ඉඩම"
                                "permit_recommendations" -> "${record["applicantName"]?.jsonPrimitive?.content ?: "අයදුම්කරු"} (${record["permitType"]?.jsonPrimitive?.content ?: ""})"
                                "gov_allowances" -> "${record["beneficiaryName"]?.jsonPrimitive?.content ?: "ප්‍රතිලාභී"} (${record["allowanceType"]?.jsonPrimitive?.content ?: ""})"
                                "disaster_relief" -> "${record["affectedPersonName"]?.jsonPrimitive?.content ?: "විපතට පත් පුද්ගලයා"} (${record["disasterType"]?.jsonPrimitive?.content ?: ""})"
                                "judicial_mediation" -> "${record["complainantName"]?.jsonPrimitive?.content ?: "පැමිණිලිකරු"} vs ${record["respondentName"]?.jsonPrimitive?.content ?: "වගඋත්තරකරු"}"
                                "ayurveda_health" -> record["patientOrProgramName"]?.jsonPrimitive?.content ?: "සෞඛ්‍ය වැඩසටහන"
                                "development_projects" -> record["projectName"]?.jsonPrimitive?.content ?: "ව්‍යාපෘතිය"
                                "residency_changes" -> "${record["fullName"]?.jsonPrimitive?.content ?: "පුද්ගලයා"} (${record["changeType"]?.jsonPrimitive?.content ?: ""})"
                                "affidavits" -> "${record["applicantName"]?.jsonPrimitive?.content ?: "අයදුම්කරු"} - ${record["affidavitPurpose"]?.jsonPrimitive?.content ?: ""}"
                                "business_registrations" -> record["businessNameAndAddress"]?.jsonPrimitive?.content ?: "ව්‍යාපාරය"
                                "birth_death_reports" -> "${record["subjectName"]?.jsonPrimitive?.content ?: "පුද්ගලයා"} (${record["reportType"]?.jsonPrimitive?.content ?: ""})"
                                "pension_registry" -> "${record["pensionerName"]?.jsonPrimitive?.content ?: "විශ්‍රාමිකයා"} (${record["pensionNo"]?.jsonPrimitive?.content ?: ""})"
                                "maternity_nutrition" -> record["motherName"]?.jsonPrimitive?.content ?: "මව"
                                "officer_visits" -> record["officerNameAndDesignation"]?.jsonPrimitive?.content ?: "නිලධාරී"
                                "voluntary_orgs" -> record["orgNameStartedDate"]?.jsonPrimitive?.content ?: "සංවිධානය"
                                "misc_info" -> record["subject"]?.jsonPrimitive?.content ?: record["categoryTitle"]?.jsonPrimitive?.content ?: "තොරතුරු"
                                "contact" -> "${record["contactName"]?.jsonPrimitive?.content ?: "සබඳතාව"} (${record["phone"]?.jsonPrimitive?.content ?: ""})"
                                else -> record.values.firstOrNull { it.jsonPrimitive.content.isNotBlank() }?.jsonPrimitive?.content ?: "සටහන #$id"
                            }

                            // Subtitle resolution
                            val subtitle = when {
                                modId == "person" && !record["nic"]?.jsonPrimitive?.content.isNullOrBlank() -> {
                                    val nicText = "ජා.හැ.අ: ${record["nic"]?.jsonPrimitive?.content}"
                                    val addr = record["address"]?.jsonPrimitive?.content
                                    if (!addr.isNullOrBlank()) "$nicText • $addr" else nicText
                                }
                                !record["address"]?.jsonPrimitive?.content.isNullOrBlank() -> record["address"]?.jsonPrimitive?.content.orEmpty()
                                !record["nic"]?.jsonPrimitive?.content.isNullOrBlank() -> "ජා.හැ.අ: ${record["nic"]?.jsonPrimitive?.content}"
                                !record["phone"]?.jsonPrimitive?.content.isNullOrBlank() -> "දුරකථන: ${record["phone"]?.jsonPrimitive?.content}"
                                !record["location"]?.jsonPrimitive?.content.isNullOrBlank() -> record["location"]?.jsonPrimitive?.content.orEmpty()
                                !record["date"]?.jsonPrimitive?.content.isNullOrBlank() -> "දිනය: ${record["date"]?.jsonPrimitive?.content}"
                                !record["incidentDate"]?.jsonPrimitive?.content.isNullOrBlank() -> "දිනය: ${record["incidentDate"]?.jsonPrimitive?.content}"
                                !record["eventDate"]?.jsonPrimitive?.content.isNullOrBlank() -> "දිනය: ${record["eventDate"]?.jsonPrimitive?.content}"
                                !record["remarks"]?.jsonPrimitive?.content.isNullOrBlank() -> record["remarks"]?.jsonPrimitive?.content.orEmpty()
                                else -> ""
                            }

                            val status = record["status"]?.jsonPrimitive?.content
                                ?: record["recommendationStatus"]?.jsonPrimitive?.content
                                ?: record["progressStatus"]?.jsonPrimitive?.content
                                ?: record["applicantSignatureStatus"]?.jsonPrimitive?.content
                                ?: record["signatureStatus"]?.jsonPrimitive?.content
                                ?: record["maritalStatus"]?.jsonPrimitive?.content

                            val disasterType = record["disasterType"]?.jsonPrimitive?.content

                            list.add(
                                GlobalSearchResult(
                                    moduleId = modId,
                                    moduleTitle = module?.titleSi ?: modId,
                                    iconRes = module?.iconRes ?: R.drawable.ic_round_person,
                                    recordId = id,
                                    title = title,
                                    subtitle = subtitle,
                                    statusBadge = status,
                                    disasterBadge = disasterType
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
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

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "වසමේ තොරතුරු සෙවීම",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (searchQuery.isNotBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${searchResults.size} හමුවිය",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
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
                    // Search Bar
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
                                        text = "පුද්ගලයින්, නිවාස, මුදල් පොත, ලිපි, ආපදා ආදී සියල්ල සොයන්න...",
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
                                    text = "සියලු ලේඛන",
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
                                    text = module.titleSi,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    // Content View with Error & Empty Component handling
                    if (searchQuery.isBlank()) {
                        IllustratedStateScreen(
                            title = "වසමේ තොරතුරු සෙවීම",
                            subtitle = "පුද්ගලයින්, ජා.හැ.අ, නිවාස අංකය, ලිපි හෝ මුදල් ගනුදෙනු ක්ෂණිකව සොයන්න",
                            iconRes = R.drawable.ic_state_search_empty,
                            primaryActionText = null,
                            onPrimaryAction = null,
                            modifier = Modifier.weight(1f)
                        )
                    } else if (searchResults.isEmpty()) {
                        IllustratedStateScreen(
                            title = "කිසිදු තොරතුරක් හමු නොවීය",
                            subtitle = "වෙනත් නමක්, අංකයක් හෝ වචනයක් යොදා නැවත සොයා බලන්න",
                            iconRes = R.drawable.ic_state_hourglass,
                            primaryActionText = "සෙවුම ඉවත් කරන්න",
                            onPrimaryAction = { searchQuery = "" },
                            modifier = Modifier.weight(1f)
                        )
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
                                                    text = item.title,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
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

                                            if (item.subtitle.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = item.subtitle,
                                                    fontSize = 13.sp,
                                                    color = TextSecondary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            if (!item.disasterBadge.isNullOrBlank() || !item.statusBadge.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    if (!item.disasterBadge.isNullOrBlank()) {
                                                        DisasterTypeBadge(disasterType = item.disasterBadge)
                                                    }
                                                    if (!item.statusBadge.isNullOrBlank()) {
                                                        StatusBadge(status = item.statusBadge)
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
    }
}
