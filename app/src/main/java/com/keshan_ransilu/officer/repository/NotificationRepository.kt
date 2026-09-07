package com.keshan_ransilu.officer.repository

import android.content.Context
import com.keshan_ransilu.officer.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Serializable
data class NotificationModel(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subtitle: String,
    val timestamp: String,
    val category: String = "Registers",
    val iconRes: Int = R.drawable.ic_notification_bell,
    val isUnread: Boolean = true,
    val targetModuleId: String? = null,
    val createdAtMillis: Long = System.currentTimeMillis()
)

class NotificationRepository(private val context: Context) {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val file: File
        get() = File(context.filesDir, "notifications.json")

    suspend fun getAll(): List<NotificationModel> = withContext(Dispatchers.IO) {
        if (!file.exists()) return@withContext emptyList()
        try {
            json.decodeFromString<List<NotificationModel>>(file.readText())
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addNotification(
        title: String,
        subtitle: String,
        category: String = "Registers",
        iconRes: Int = R.drawable.ic_notification_bell,
        targetModuleId: String? = null
    ) = withContext(Dispatchers.IO) {
        val currentList = getAll().toMutableList()
        val timeFormat = SimpleDateFormat("hh:mm a, dd MMM", Locale.ENGLISH)
        val newNotification = NotificationModel(
            title = title,
            subtitle = subtitle,
            timestamp = timeFormat.format(Date()),
            category = category,
            iconRes = iconRes,
            isUnread = true,
            targetModuleId = targetModuleId
        )
        currentList.add(0, newNotification)
        saveList(currentList)
    }

    suspend fun markAsRead(id: String) = withContext(Dispatchers.IO) {
        val currentList = getAll().map {
            if (it.id == id) it.copy(isUnread = false) else it
        }
        saveList(currentList)
    }

    suspend fun markAllAsRead() = withContext(Dispatchers.IO) {
        val currentList = getAll().map { it.copy(isUnread = false) }
        saveList(currentList)
    }

    suspend fun delete(id: String) = withContext(Dispatchers.IO) {
        val currentList = getAll().filter { it.id != id }
        saveList(currentList)
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        if (file.exists()) {
            file.delete()
        }
    }

    private fun saveList(list: List<NotificationModel>) {
        try {
            file.writeText(json.encodeToString(list))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
