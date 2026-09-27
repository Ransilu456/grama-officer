package com.keshan_ransilu.officer.repository

import android.content.Context
import com.keshan_ransilu.officer.data.registry.FieldType
import com.keshan_ransilu.officer.data.registry.RegisterCatalog
import com.keshan_ransilu.officer.data.registry.RegisterModule
import com.keshan_ransilu.officer.data.storage.JsonStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import java.util.UUID

class RegisterRepository(
    private val context: Context,
    private val store: JsonStore = JsonStore(context),
    private val notificationRepo: NotificationRepository = NotificationRepository(context)
) {

    suspend fun getAll(moduleId: String): List<JsonObject> = withContext(Dispatchers.IO) {
        store.readAll<JsonObject>(moduleId)
    }

    suspend fun getById(moduleId: String, id: String): JsonObject? = withContext(Dispatchers.IO) {
        val records = getAll(moduleId)
        records.find { it["id"]?.jsonPrimitive?.contentOrNull == id }
    }

    suspend fun save(moduleId: String, recordMap: Map<String, String>, existingId: String? = null): String = withContext(Dispatchers.IO) {
        val records = getAll(moduleId).toMutableList()
        val jsonMap: MutableMap<String, JsonElement> = recordMap.mapValues { JsonPrimitive(it.value) }.toMutableMap()
        val now = System.currentTimeMillis().toString()

        val module = RegisterCatalog.find { it.id == moduleId }
        val moduleName = module?.titleSi ?: moduleId
        val primaryName = recordMap.values.firstOrNull { it.isNotBlank() } ?: "Record"

        val recordId = if (existingId.isNullOrBlank()) {
            val id = UUID.randomUUID().toString()
            jsonMap["id"] = JsonPrimitive(id)
            jsonMap["createdAt"] = JsonPrimitive(now)
            jsonMap["updatedAt"] = JsonPrimitive(now)
            records.add(0, JsonObject(jsonMap))

            // Trigger real notification for record addition
            notificationRepo.addNotification(
                title = "New Entry Added: $moduleName",
                subtitle = "$primaryName successfully saved to registry.",
                category = "Registers",
                iconRes = module?.iconRes ?: com.keshan_ransilu.officer.R.drawable.ic_notification_bell,
                targetModuleId = moduleId
            )

            id
        } else {
            val index = records.indexOfFirst { it["id"]?.jsonPrimitive?.contentOrNull == existingId }
            jsonMap["id"] = JsonPrimitive(existingId)
            jsonMap["updatedAt"] = JsonPrimitive(now)
            if (index != -1) {
                jsonMap["createdAt"] = records[index]["createdAt"] ?: JsonPrimitive(now)
                records[index] = JsonObject(jsonMap)
            } else {
                jsonMap["createdAt"] = JsonPrimitive(now)
                records.add(0, JsonObject(jsonMap))
            }

            // Trigger real notification for record update
            notificationRepo.addNotification(
                title = "Entry Updated: $moduleName",
                subtitle = "Changes to $primaryName were saved.",
                category = "Registers",
                iconRes = module?.iconRes ?: com.keshan_ransilu.officer.R.drawable.ic_notification_bell,
                targetModuleId = moduleId
            )

            existingId
        }

        store.writeAll(moduleId, records)
        recordId
    }

    suspend fun delete(moduleId: String, id: String): Boolean = withContext(Dispatchers.IO) {
        val records = getAll(moduleId).toMutableList()
        val initialSize = records.size
        val recordToDelete = records.find { it["id"]?.jsonPrimitive?.contentOrNull == id }
        val recordTitle = recordToDelete?.values?.firstOrNull { it.jsonPrimitive.content.isNotBlank() }?.jsonPrimitive?.content ?: "Record"
        
        records.removeAll { it["id"]?.jsonPrimitive?.contentOrNull == id }
        if (records.size != initialSize) {
            store.writeAll(moduleId, records)
            val module = RegisterCatalog.find { it.id == moduleId }
            notificationRepo.addNotification(
                title = "Entry Deleted",
                subtitle = "$recordTitle was removed from ${module?.titleSi ?: moduleId}.",
                category = "Registers",
                iconRes = module?.iconRes ?: com.keshan_ransilu.officer.R.drawable.ic_notification_bell,
                targetModuleId = moduleId
            )
            true
        } else {
            false
        }
    }

    suspend fun backupAllToJson(): String = withContext(Dispatchers.IO) {
        val fullDatabase = mutableMapOf<String, JsonElement>()
        RegisterCatalog.forEach { module ->
            val records = getAll(module.id)
            fullDatabase[module.id] = JsonArray(records)
        }
        val root = JsonObject(fullDatabase)
        val json = Json { prettyPrint = true }
        json.encodeToString(JsonObject.serializer(), root)
    }

    suspend fun validate(
        module: RegisterModule,
        data: Map<String, String>,
        existingId: String? = null
    ): Map<String, String> = withContext(Dispatchers.IO) {
        val errors = mutableMapOf<String, String>()
        val existingRecords by lazy { store.readAll<JsonObject>(module.id) }

        module.fields.forEach { field ->
            val rawValue = data[field.key]?.trim().orEmpty()
            val fieldNameSi = field.label.substringBefore('/').trim()

            // 1. Strict Required Check
            if (field.required && rawValue.isBlank()) {
                errors[field.key] = "$fieldNameSi අනිවාර්ය වේ (Required)"
                return@forEach
            }

            // 2. Strict Type & Format Validation
            if (rawValue.isNotBlank()) {
                when (field.type) {
                    FieldType.NUMBER -> {
                        val num = rawValue.toDoubleOrNull()
                        if (num == null) {
                            errors[field.key] = "කරුණාකර වලංගු අංකයක් ඇතුළත් කරන්න (Invalid Number)"
                        } else {
                            if (field.validation?.min != null && num < field.validation.min) {
                                errors[field.key] = "අවම අගය ${field.validation.min} විය යුතුය"
                            }
                            if (field.validation?.max != null && num > field.validation.max) {
                                errors[field.key] = "උපරිම අගය ${field.validation.max} විය යුතුය"
                            }
                        }
                    }
                    FieldType.NIC -> {
                        val nicRegex = Regex("^([0-9]{9}[xXvV]|[0-9]{12})$")
                        if (!nicRegex.matches(rawValue)) {
                            errors[field.key] = "අවලංගු හැඳුනුම්පත් අංකයකි (Invalid NIC)"
                        }
                    }
                    FieldType.PHONE -> {
                        val phoneRegex = Regex("^0[0-9]{9}$")
                        if (!phoneRegex.matches(rawValue)) {
                            errors[field.key] = "අවලංගු දුරකථන අංකයකි (e.g. 0712345678)"
                        }
                    }
                    FieldType.DATE -> {
                        val dateRegex = Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")
                        if (!dateRegex.matches(rawValue)) {
                            errors[field.key] = "දිනය YYYY-MM-DD ලෙස තිබිය යුතුය"
                        }
                    }
                    else -> {}
                }

                // 3. Strict Custom Regex Validation
                field.validation?.regex?.let { pattern ->
                    if (!errors.containsKey(field.key) && !Regex(pattern).matches(rawValue)) {
                        errors[field.key] = field.validation.errorMessage ?: "වැරදි ආකෘතියකි (Invalid format)"
                    }
                }

                // 4. Strict Uniqueness Check
                if (field.isUnique && !errors.containsKey(field.key)) {
                    val duplicate = existingRecords.any { rec ->
                        val recId = rec["id"]?.jsonPrimitive?.content
                        val recVal = rec[field.key]?.jsonPrimitive?.content?.trim()
                        recId != existingId && recVal.equals(rawValue, ignoreCase = true)
                    }
                    if (duplicate) {
                        errors[field.key] = "$fieldNameSi '$rawValue' දැනටමත් ලියාපදිංචි කර ඇත (Duplicate)"
                    }
                }
            }
        }
        errors
    }
}
