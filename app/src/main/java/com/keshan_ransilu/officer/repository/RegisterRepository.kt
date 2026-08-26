package com.keshan_ransilu.officer.repository

import android.content.Context
import com.keshan_ransilu.officer.data.registry.FieldType
import com.keshan_ransilu.officer.data.registry.RegisterCatalog
import com.keshan_ransilu.officer.data.registry.RegisterModule
import com.keshan_ransilu.officer.data.storage.JsonStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import java.io.File
import java.util.UUID

class RegisterRepository(
    private val context: Context,
    private val store: JsonStore = JsonStore(context)
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

        val recordId = if (existingId.isNullOrBlank()) {
            val id = UUID.randomUUID().toString()
            jsonMap["id"] = JsonPrimitive(id)
            jsonMap["createdAt"] = JsonPrimitive(now)
            jsonMap["updatedAt"] = JsonPrimitive(now)
            records.add(0, JsonObject(jsonMap))
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
            existingId
        }

        store.writeAll(moduleId, records)
        recordId
    }

    suspend fun delete(moduleId: String, id: String): Boolean = withContext(Dispatchers.IO) {
        val records = getAll(moduleId).toMutableList()
        val initialSize = records.size
        records.removeAll { it["id"]?.jsonPrimitive?.contentOrNull == id }
        if (records.size != initialSize) {
            store.writeAll(moduleId, records)
            true
        } else {
            false
        }
    }

    suspend fun searchAllModules(query: String): Map<String, List<JsonObject>> = withContext(Dispatchers.IO) {
        val results = mutableMapOf<String, List<JsonObject>>()
        val cleanQuery = query.trim()
        if (cleanQuery.isBlank()) return@withContext results

        RegisterCatalog.forEach { module ->
            val records = getAll(module.id)
            val matching = records.filter { record ->
                record.values.any { it.jsonPrimitive.content.contains(cleanQuery, ignoreCase = true) }
            }
            if (matching.isNotEmpty()) {
                results[module.id] = matching
            }
        }
        results
    }

    suspend fun getRegisterCounts(): Map<String, Int> = withContext(Dispatchers.IO) {
        val counts = mutableMapOf<String, Int>()
        RegisterCatalog.forEach { module ->
            counts[module.id] = getAll(module.id).size
        }
        counts
    }

    suspend fun clearAllSampleData() = withContext(Dispatchers.IO) {
        val registersDir = File(context.filesDir, "registers")
        if (registersDir.exists()) {
            registersDir.listFiles()?.forEach { file ->
                file.delete()
            }
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

    fun validate(module: RegisterModule, data: Map<String, String>): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        module.fields.forEach { field ->
            val value = data[field.key]?.trim().orEmpty()
            if (field.required && value.isBlank()) {
                errors[field.key] = "${field.label.substringBefore('/')} is required"
                return@forEach
            }

            if (value.isNotBlank()) {
                field.validation?.let { validation ->
                    validation.regex?.let { pattern ->
                        if (!Regex(pattern).matches(value)) {
                            errors[field.key] = validation.errorMessage ?: "Invalid format"
                        }
                    }
                    if (field.type == FieldType.NUMBER) {
                        val num = value.toDoubleOrNull()
                        if (num == null) {
                            errors[field.key] = "Please enter a valid number"
                        } else {
                            if (validation.min != null && num < validation.min) {
                                errors[field.key] = "Minimum value is ${validation.min}"
                            }
                            if (validation.max != null && num > validation.max) {
                                errors[field.key] = "Maximum value is ${validation.max}"
                            }
                        }
                    }
                }
            }
        }
        return errors
    }
}
