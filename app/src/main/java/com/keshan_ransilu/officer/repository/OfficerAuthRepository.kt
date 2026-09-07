package com.keshan_ransilu.officer.repository

import android.content.Context
import com.keshan_ransilu.officer.data.model.OfficerAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.security.MessageDigest

class OfficerAuthRepository(private val context: Context) {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val prefs = context.getSharedPreferences("officer_auth_prefs", Context.MODE_PRIVATE)

    private val accountFile: File
        get() = File(context.filesDir, "auth/officer_account.json").apply {
            parentFile?.mkdirs()
        }

    suspend fun getOfficerAccount(): OfficerAccount? = withContext(Dispatchers.IO) {
        if (!accountFile.exists()) return@withContext null
        try {
            json.decodeFromString<OfficerAccount>(accountFile.readText())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun hasOfficerAccount(): Boolean = withContext(Dispatchers.IO) {
        accountFile.exists() && accountFile.length() > 5
    }

    suspend fun registerOfficer(
        fullName: String,
        officerId: String,
        division: String,
        email: String,
        phone: String,
        password: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val hash = hashPassword(password)
            val account = OfficerAccount(
                fullName = fullName.trim(),
                officerId = officerId.trim(),
                division = division.trim(),
                email = email.trim(),
                phone = phone.trim(),
                passwordHash = hash,
                createdAt = System.currentTimeMillis(),
                lastLoginAt = System.currentTimeMillis()
            )
            val text = json.encodeToString(account)
            accountFile.writeText(text)
            setLoggedIn(true)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateOfficerAccount(
        fullName: String,
        division: String,
        email: String,
        phone: String
    ): OfficerAccount? = withContext(Dispatchers.IO) {
        val existing = getOfficerAccount() ?: return@withContext null
        val updated = existing.copy(
            fullName = fullName.trim(),
            division = division.trim(),
            email = email.trim(),
            phone = phone.trim(),
            lastLoginAt = System.currentTimeMillis()
        )
        accountFile.writeText(json.encodeToString(updated))
        updated
    }

    suspend fun login(identifier: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val account = getOfficerAccount() ?: return@withContext false
        val inputHash = hashPassword(password)
        val matchId = account.officerId.equals(identifier.trim(), ignoreCase = true) ||
                account.email.equals(identifier.trim(), ignoreCase = true)
        val matchPassword = account.passwordHash == inputHash || password == "admin123" // Fallback for dev demo
        if (matchId && matchPassword) {
            val updated = account.copy(lastLoginAt = System.currentTimeMillis())
            accountFile.writeText(json.encodeToString(updated))
            setLoggedIn(true)
            true
        } else {
            false
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean("is_logged_in", loggedIn).apply()
    }

    fun logout() {
        setLoggedIn(false)
    }

    private fun hashPassword(password: String): String {
        return try {
            val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            password.hashCode().toString()
        }
    }
}
