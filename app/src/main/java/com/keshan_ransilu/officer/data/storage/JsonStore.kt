package com.keshan_ransilu.officer.data.storage

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class JsonStore(private val context: Context) {

    val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true 
    }

    @PublishedApi
    internal fun fileFor(moduleId: String): File =
        File(context.filesDir, "registers/$moduleId.json").apply {
            parentFile?.mkdirs()
        }

    inline fun <reified T> readAll(moduleId: String): List<T> {
        val file = fileFor(moduleId)
        if (!file.exists()) return emptyList()
        return try {
            json.decodeFromString<List<T>>(file.readText())
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() 
        }
    }

    inline fun <reified T> writeAll(moduleId: String, records: List<T>) {
        val file = fileFor(moduleId)
        val tmp = File(file.parentFile, "${file.name}.tmp")
        try {
            tmp.writeText(json.encodeToString(records))
            tmp.renameTo(file)
            backupCopy(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun backupCopy(file: File) {
        try {
            val bak = File(file.parentFile, "${file.name}.bak")
            file.copyTo(bak, overwrite = true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
