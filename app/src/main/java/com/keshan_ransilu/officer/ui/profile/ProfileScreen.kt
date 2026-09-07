package com.keshan_ransilu.officer.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.data.model.OfficerAccount
import com.keshan_ransilu.officer.repository.OfficerAuthRepository
import com.keshan_ransilu.officer.repository.RegisterRepository
import com.keshan_ransilu.officer.ui.home.HeaderBackgroundFaceted
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onNavigateToModule: (String) -> Unit,
    onNavigateToNotifications: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { RegisterRepository(context) }
    val authRepository = remember { OfficerAuthRepository(context) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }

    var personCount by remember { mutableIntStateOf(0) }
    var letterCount by remember { mutableIntStateOf(0) }
    var permitCount by remember { mutableIntStateOf(0) }
    var aswasumaCount by remember { mutableIntStateOf(0) }
    var officerAccount by remember { mutableStateOf<OfficerAccount?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditProfileSheet by remember { mutableStateOf(false) }

    // Edit profile state
    var editName by remember { mutableStateOf("") }
    var editDivision by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }
    var editPhone by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        officerAccount = authRepository.getOfficerAccount()
        personCount = repository.getAll("person").size
        letterCount = repository.getAll("letters").size
        permitCount = repository.getAll("permit_recommendations").size
        aswasumaCount = repository.getAll("cashbook").size
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HeaderBluePrimary)
    ) {
        // Faceted blue top background header
        HeaderBackgroundFaceted(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
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

                Text(
                    text = "My Profile",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = Color.White),
                            onClick = {
                                editName = officerAccount?.fullName.orEmpty()
                                editDivision = officerAccount?.division ?: "142 - Mahara Central"
                                editEmail = officerAccount?.email.orEmpty()
                                editPhone = officerAccount?.phone.orEmpty()
                                showEditProfileSheet = true
                            }
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.18f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Curved Main Body Sheet (Modern iOS Dribbble-inspired Profile layout)
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = ScreenBg,
                shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Avatar with warm circular accent ring (matching Image 1)
                    Box(
                        modifier = Modifier
                            .size(108.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(3.dp, Color(0xFFFF9E80), CircleShape)
                            .padding(6.dp),
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Officer Name & Designation
                    Text(
                        text = officerAccount?.fullName?.ifBlank { "Grama Niladhari" } ?: "Grama Niladhari",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "GN Officer · ${officerAccount?.division ?: "142 - Mahara Central"}",
                        fontSize = 13.sp,
                        color = HeaderBluePrimary,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Officer ID & Year Badges Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEFF2F8)
                        ) {
                            Text(
                                text = officerAccount?.officerId ?: "GN/WP/GM/$currentYear/0142",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8F8F0)
                        ) {
                            Text(
                                text = "Active · Year $currentYear",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00A844),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Quick Stats 2x2 Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProfileMiniStat(
                            title = "Persons",
                            count = personCount.toString(),
                            iconRes = R.drawable.ic_round_person,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToModule("person") }
                        )
                        ProfileMiniStat(
                            title = "Requests",
                            count = letterCount.toString(),
                            iconRes = R.drawable.ic_round_letters,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToModule("letters") }
                        )
                        ProfileMiniStat(
                            title = "Permits",
                            count = permitCount.toString(),
                            iconRes = R.drawable.ic_round_permits,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToModule("permit_recommendations") }
                        )
                        ProfileMiniStat(
                            title = "Cash Book",
                            count = aswasumaCount.toString(),
                            iconRes = R.drawable.ic_round_cashbook,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToModule("cashbook") }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Grouped List Action Cards (iOS Dribbble-inspired menu cards)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                            // Item 1: Personal Data & Jurisdiction
                            ProfileMenuItem(
                                iconRes = R.drawable.ic_dashboard_account,
                                title = "Personal Data & Jurisdiction",
                                subtitle = "${officerAccount?.email ?: "gn.division@gov.lk"} · ${officerAccount?.phone ?: "071-2345678"}",
                                onClick = {
                                    editName = officerAccount?.fullName.orEmpty()
                                    editDivision = officerAccount?.division ?: "142 - Mahara Central"
                                    editEmail = officerAccount?.email.orEmpty()
                                    editPhone = officerAccount?.phone.orEmpty()
                                    showEditProfileSheet = true
                                }
                            )

                            HorizontalDivider(color = DividerColor, thickness = 0.8.dp)

                            // Item 2: Office Public Hours
                            ProfileMenuItem(
                                iconRes = R.drawable.ic_dashboard_contact,
                                title = "Public Service Hours",
                                subtitle = "Monday - Friday : 8:30 AM - 4:30 PM",
                                onClick = {}
                            )

                            HorizontalDivider(color = DividerColor, thickness = 0.8.dp)

                            // Item 3: Notifications & Circulars
                            ProfileMenuItem(
                                iconRes = R.drawable.ic_notification_bell,
                                title = "Notifications & Circulars",
                                subtitle = "Official updates, directives and alerts",
                                onClick = onNavigateToNotifications
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card Group 2: Data Management & Logout
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                            // Backup Database
                            ProfileMenuItem(
                                iconRes = R.drawable.ic_dashboard_analytics,
                                title = "Data Backup & Export",
                                subtitle = "Backup complete local division records",
                                onClick = {
                                    scope.launch {
                                        val backupJson = repository.backupAllToJson()
                                        Toast.makeText(context, "Database backup complete (${backupJson.length} bytes)", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )

                            HorizontalDivider(color = DividerColor, thickness = 0.8.dp)

                            // Logout
                            ProfileMenuItem(
                                iconRes = R.drawable.ic_empty_connection,
                                title = "Logout from Session",
                                subtitle = "Securely end officer portal session",
                                titleColor = Color(0xFFE53935),
                                onClick = { showLogoutDialog = true }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }

        // Edit Profile Modal Bottom Sheet
        if (showEditProfileSheet) {
            ModalBottomSheet(
                onDismissRequest = { showEditProfileSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Edit Officer Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Update your official details and contact information",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_input_person), contentDescription = null, tint = HeaderBluePrimary, modifier = Modifier.size(20.dp)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = editDivision,
                        onValueChange = { editDivision = it },
                        label = { Text("GN Division") },
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_input_location), contentDescription = null, tint = HeaderBluePrimary, modifier = Modifier.size(20.dp)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Official Email") },
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_input_email), contentDescription = null, tint = HeaderBluePrimary, modifier = Modifier.size(20.dp)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Contact Phone") },
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_input_phone), contentDescription = null, tint = HeaderBluePrimary, modifier = Modifier.size(20.dp)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                val updated = authRepository.updateOfficerAccount(
                                    fullName = editName,
                                    division = editDivision,
                                    email = editEmail,
                                    phone = editPhone
                                )
                                if (updated != null) {
                                    officerAccount = updated
                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                }
                                showEditProfileSheet = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HeaderBluePrimary)
                    ) {
                        Text("Save Profile Changes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout from Officer Portal", fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to end your current officer session?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            authRepository.logout()
                            showLogoutDialog = false
                            onLogout()
                        }
                    ) {
                        Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White
            )
        }
    }
}

@Composable
fun ProfileMiniStat(
    title: String,
    count: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.12f)),
            onClick = onClick
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = count,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    iconRes: Int,
    title: String,
    subtitle: String,
    titleColor: Color = TextPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.08f)),
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF4F6FA)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFC0C7D5),
            modifier = Modifier.size(18.dp)
        )
    }
}
