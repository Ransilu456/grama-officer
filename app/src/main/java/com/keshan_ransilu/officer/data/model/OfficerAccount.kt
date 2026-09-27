package com.keshan_ransilu.officer.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class OfficerAccount(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val officerId: String,          // "GN/WP/GM/0142"
    val division: String,           //  "142 - Kurunegala"
    val province: String = "Western Province",
    val email: String,
    val phone: String,
    val passwordHash: String,       // secure hash
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)
