package com.keshan_ransilu.officer.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ripple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.data.model.OfficerAccount
import com.keshan_ransilu.officer.repository.OfficerAuthRepository
import com.keshan_ransilu.officer.ui.theme.*
import java.util.Calendar

/**
 * Premium redesigned Drawer Header showcasing Officer credentials, Division, and verified status.
 */
@Composable
fun DrawerProfileHeader(
    onProfileClick: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { OfficerAuthRepository(context) }
    var officerAccount by remember { mutableStateOf<OfficerAccount?>(null) }

    LaunchedEffect(Unit) {
        officerAccount = authRepository.getOfficerAccount()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        // Top Action Bar with Close Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = HeaderBluePrimary.copy(alpha = 0.08f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = HeaderBluePrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "OFFICIAL GN PORTAL",
                            color = HeaderBluePrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.15f)),
                        onClick = onClose
                    ),
                shape = CircleShape,
                color = Color(0xFFF2F5FA)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close drawer",
                        tint = TextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Redesigned Officer Identity Card (Clickable to Profile)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, Color(0xFFE5EBF5), RoundedCornerShape(18.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.1f)),
                    onClick = onProfileClick
                ),
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFFF8FAFD)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Officer Avatar Badge with crisp border
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.5.dp, HeaderBluePrimary.copy(alpha = 0.25f), CircleShape)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar_header),
                        contentDescription = "Officer Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = officerAccount?.fullName?.ifBlank { "Grama Niladhari" } ?: "Grama Niladhari",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = officerAccount?.officerId ?: "GN/WP/GM/0142",
                        color = HeaderBluePrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = officerAccount?.division ?: "142 - Mahara Central",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = Color(0xFFEDF1F7), thickness = 1.dp)
    }
}

/**
 * Modern footer shown at the bottom of the navigation drawer.
 */
@Composable
fun DrawerFooter() {
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            color = Color(0xFFEDF1F7),
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = "Officer App · GN Division System",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp
        )
        Text(
            text = "© $currentYear Government of Sri Lanka",
            color = TextSecondary.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
    }
}
