package com.keshan_ransilu.officer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    if (status.isBlank()) return

    val cleanStatus = status.substringBefore('(').trim()

    val (bgColor, textColor, borderColor, icon) = when {
        // Green category - Completed / Active / Approved / Settled
        status.contains("Active", ignoreCase = true) ||
        status.contains("Completed", ignoreCase = true) ||
        status.contains("Settled", ignoreCase = true) ||
        status.contains("Recommended", ignoreCase = true) ||
        status.contains("Signed", ignoreCase = true) ||
        status.contains("Resolved", ignoreCase = true) ||
        status.contains("Approved", ignoreCase = true) ||
        status.contains("Married", ignoreCase = true) -> {
            Tuple4(
                Color(0xFFE8F5E9),
                Color(0xFF2E7D32),
                Color(0xFFA5D6A7),
                Icons.Default.CheckCircle
            )
        }

        // ORANGE
        status.contains("Pending", ignoreCase = true) ||
        status.contains("Under", ignoreCase = true) ||
        status.contains("In Progress", ignoreCase = true) ||
        status.contains("Planned", ignoreCase = true) ||
        status.contains("Review", ignoreCase = true) ||
        status.contains("Single", ignoreCase = true) -> {
            Tuple4(
                Color(0xFFFFF3E0),
                Color(0xFFE65100),
                Color(0xFFFFCC80),
                Icons.Default.Schedule
            )
        }

        // RED
        status.contains("Reject", ignoreCase = true) ||
        status.contains("Suspend", ignoreCase = true) ||
        status.contains("Cancel", ignoreCase = true) ||
        status.contains("Widowed", ignoreCase = true) ||
        status.contains("Death", ignoreCase = true) -> {
            Tuple4(
                Color(0xFFFFEBEE),
                Color(0xFFC62828),
                Color(0xFFEF9A9A),
                Icons.Default.Cancel
            )
        }

        // BLUE
        status.contains("Referred", ignoreCase = true) ||
        status.contains("Transfer", ignoreCase = true) ||
        status.contains("Archived", ignoreCase = true) ||
        status.contains("Arrival", ignoreCase = true) ||
        status.contains("Birth", ignoreCase = true) -> {
            Tuple4(
                Color(0xFFE3F2FD),
                Color(0xFF1565C0),
                Color(0xFF90CAF9),
                Icons.Default.Info
            )
        }

        else -> {
            Tuple4(
                Color(0xFFECEFF1),
                Color(0xFF455A64),
                Color(0xFFCFD8DC),
                Icons.Default.FiberManualRecord
            )
        }
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = cleanStatus,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun DisasterTypeBadge(
    disasterType: String,
    modifier: Modifier = Modifier
) {
    if (disasterType.isBlank()) return

    val cleanType = disasterType.substringBefore('(').trim()

    val (bgColor, textColor, borderColor, icon) = when {
        disasterType.contains("Flood", ignoreCase = true) || disasterType.contains("ගංවතුර") -> {
            Tuple4(
                Color(0xFFE1F5FE),
                Color(0xFF0277BD),
                Color(0xFF81D4FA),
                Icons.Default.WaterDamage
            )
        }
        disasterType.contains("Fire", ignoreCase = true) || disasterType.contains("ගිනි") -> {
            Tuple4(
                Color(0xFFFFEBEE),
                Color(0xFFC62828),
                Color(0xFFFFCDD2),
                Icons.Default.LocalFireDepartment
            )
        }
        disasterType.contains("Wind", ignoreCase = true) || disasterType.contains("සුළි") || disasterType.contains("Storm", ignoreCase = true) -> {
            Tuple4(
                Color(0xFFE0F2F1),
                Color(0xFF00695C),
                Color(0xFF80CBC4),
                Icons.Default.Air
            )
        }
        disasterType.contains("Drought", ignoreCase = true) || disasterType.contains("නියඟ") -> {
            Tuple4(
                Color(0xFFFFF8E1),
                Color(0xFFE65100),
                Color(0xFFFFE082),
                Icons.Default.WbSunny
            )
        }
        disasterType.contains("Landslide", ignoreCase = true) || disasterType.contains("නාය") -> {
            Tuple4(
                Color(0xFFEFEBE9),
                Color(0xFF4E342E),
                Color(0xFFBCAAA4),
                Icons.Default.Landscape
            )
        }
        disasterType.contains("Wildlife", ignoreCase = true) || disasterType.contains("සතුන්") -> {
            Tuple4(
                Color(0xFFE8F5E9),
                Color(0xFF2E7D32),
                Color(0xFFA5D6A7),
                Icons.Default.Pets
            )
        }
        else -> {
            Tuple4(
                Color(0xFFECEFF1),
                Color(0xFF455A64),
                Color(0xFFCFD8DC),
                Icons.Default.Warning
            )
        }
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = cleanType,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

private data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
