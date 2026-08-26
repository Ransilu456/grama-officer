package com.keshan_ransilu.officer.ui.recordform

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.data.registry.FieldSpec
import com.keshan_ransilu.officer.data.registry.FieldType
import com.keshan_ransilu.officer.data.registry.RegisterCatalog
import com.keshan_ransilu.officer.data.registry.RegisterModule
import com.keshan_ransilu.officer.repository.RegisterRepository
import com.keshan_ransilu.officer.ui.home.HeaderBackgroundFaceted
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordFormScreen(
    moduleId: String,
    recordId: String? = null,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    onMenuClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { RegisterRepository(context) }
    val module = remember(moduleId) { RegisterCatalog.find { it.id == moduleId } } ?: return
    val scope = rememberCoroutineScope()

    val formState = remember { mutableStateMapOf<String, String>() }
    val errors = remember { mutableStateMapOf<String, String>() }
    val relationOptions = remember { mutableStateMapOf<String, List<Pair<String, String>>>() }
    var isSaving by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Prepopulate data
    LaunchedEffect(moduleId, recordId) {
        if (!recordId.isNullOrBlank()) {
            val record = repository.getById(moduleId, recordId)
            record?.forEach { (key, value) ->
                formState[key] = value.jsonPrimitive.content
            }
        } else {
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            module.fields.filter { it.type == FieldType.DATE }.forEach { field ->
                if (formState[field.key].isNullOrBlank()) {
                    formState[field.key] = todayStr
                }
            }
        }

        // Load relational lists
        module.fields.filter { it.type == FieldType.RELATION }.forEach { field ->
            field.relationModuleId?.let { relModId ->
                val relRecords = repository.getAll(relModId)
                val displayField = field.relationDisplayField ?: "id"
                val items = relRecords.mapNotNull { relRec ->
                    val id = relRec["id"]?.jsonPrimitive?.content ?: return@mapNotNull null
                    val display = relRec[displayField]?.jsonPrimitive?.content ?: id
                    id to display
                }
                relationOptions[field.key] = items
            }
        }
    }

    val handleSave = {
        val validationErrors = repository.validate(module, formState)
        errors.clear()
        errors.putAll(validationErrors)

        if (errors.isEmpty()) {
            isSaving = true
            scope.launch {
                repository.save(moduleId, formState.toMap(), recordId)
                isSaving = false
                onSaveSuccess()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HeaderBluePrimary)
            .imePadding()
    ) {
        HeaderBackgroundFaceted(modifier = Modifier.fillMaxWidth().height(200.dp))

        Column(modifier = Modifier.fillMaxSize()) {
            FormScreenHeader(
                isEdit = !recordId.isNullOrBlank(),
                module = module,
                onBackClick = onBackClick,
                onSaveClick = handleSave,
                isSaving = isSaving
            )

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = ScreenBg,
                shape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = module.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(module.titleEn, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(module.titleSi, fontSize = 12.sp, color = TextSecondary)
                        }
                    }

                    HorizontalDivider(color = DividerColor, thickness = 1.dp)

                    module.fields.forEach { field ->
                        ModernFormField(
                            field = field,
                            value = formState[field.key].orEmpty(),
                            error = errors[field.key],
                            relationList = relationOptions[field.key] ?: emptyList(),
                            onValueChange = { newValue ->
                                formState[field.key] = newValue
                                errors.remove(field.key)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = handleSave,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HeaderBluePrimary),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (recordId.isNullOrBlank()) "Save Entry" else "Update Entry", fontWeight = FontWeight.Bold)
                        }
                    }

                    if (!recordId.isNullOrBlank()) {
                        var showDeleteDialog by remember { mutableStateOf(false) }

                        OutlinedButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935))
                        ) {
                            Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp), tint = Color(0xFFE53935))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete Record", fontWeight = FontWeight.Bold, color = Color(0xFFE53935))
                        }

                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text("Delete Entry", fontWeight = FontWeight.Bold) },
                                text = { Text("Are you sure you want to permanently delete this official record?") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                repository.delete(moduleId, recordId)
                                                showDeleteDialog = false
                                                onSaveSuccess()
                                            }
                                        }
                                    ) {
                                        Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDeleteDialog = false }) {
                                        Text("Cancel")
                                    }
                                },
                                shape = RoundedCornerShape(20.dp),
                                containerColor = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun FormScreenHeader(
    isEdit: Boolean,
    module: RegisterModule,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    isSaving: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp).clip(CircleShape).clickable { onBackClick() },
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }

            Surface(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable(enabled = !isSaving) { onSaveClick() },
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, "Save", tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(if (isEdit) "Edit Record" else "Add New Record", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("${module.titleEn} (${module.titleSi})", fontSize = 13.sp, color = Color.White.copy(alpha = 0.75f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernFormField(
    field: FieldSpec,
    value: String,
    error: String?,
    relationList: List<Pair<String, String>>,
    onValueChange: (String) -> Unit
) {
    val isError = !error.isNullOrBlank()

    val fieldIcon: ImageVector = when {
        field.key.contains("name", true) || field.key.contains("person", true) -> Icons.Default.Person
        field.key.contains("nic", true) || field.key.contains("idNo", true) -> Icons.Default.Badge
        field.key.contains("phone", true) -> Icons.Default.Phone
        field.key.contains("date", true) || field.key.contains("dob", true) -> Icons.Default.CalendarToday
        field.key.contains("address", true) || field.key.contains("location", true) -> Icons.Default.LocationOn
        field.key.contains("rate", true) || field.key.contains("amount", true) -> Icons.Default.AttachMoney
        else -> Icons.Default.EditNote
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        when (field.type) {
            FieldType.DROPDOWN, FieldType.RELATION -> {
                var expanded by remember { mutableStateOf(false) }
                val displayValue = if (field.type == FieldType.RELATION) {
                    relationList.find { it.first == value }?.second ?: value
                } else value

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = displayValue,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(field.label, fontSize = 13.sp) },
                        leadingIcon = { Icon(fieldIcon, null, tint = HeaderBluePrimary, modifier = Modifier.size(22.dp)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(18.dp),
                        isError = isError,
                        colors = modernFieldColors()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        val options = if (field.type == FieldType.RELATION) relationList else field.options.map { it to it }
                        if (options.isEmpty()) {
                            DropdownMenuItem(text = { Text("No items found") }, onClick = { expanded = false })
                        } else {
                            options.forEach { (id, label) ->
                                DropdownMenuItem(
                                    text = { Text(label, fontWeight = FontWeight.Medium) },
                                    onClick = { onValueChange(id); expanded = false }
                                )
                            }
                        }
                    }
                }
            }

            FieldType.MULTILINE -> {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(field.label, fontSize = 13.sp) },
                    leadingIcon = { Icon(fieldIcon, null, tint = HeaderBluePrimary, modifier = Modifier.size(22.dp)) },
                    modifier = Modifier.fillMaxWidth().height(115.dp),
                    shape = RoundedCornerShape(18.dp),
                    isError = isError,
                    colors = modernFieldColors()
                )
            }

            else -> {
                val keyboardType = when (field.type) {
                    FieldType.NUMBER -> KeyboardType.Number
                    FieldType.PHONE -> KeyboardType.Phone
                    else -> KeyboardType.Text
                }
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(field.label, fontSize = 13.sp) },
                    leadingIcon = { Icon(fieldIcon, null, tint = HeaderBluePrimary, modifier = Modifier.size(22.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    isError = isError,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
                    colors = modernFieldColors()
                )
            }
        }

        if (isError) {
            Text(error!!, color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(start = 12.dp, top = 4.dp))
        }
    }
}

@Composable
fun modernFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = HeaderBluePrimary,
    unfocusedBorderColor = Color(0xFFD4DAE8),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = HeaderBluePrimary,
    focusedLabelColor = HeaderBluePrimary
)
