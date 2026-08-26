package com.keshan_ransilu.officer.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.repository.OfficerAuthRepository
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterOfficerScreen(
    onBackClick: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { OfficerAuthRepository(context) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }

    var fullName by remember { mutableStateOf("") }
    var officerId by remember { mutableStateOf("") }
    var division by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf(mapOf<String, String>()) }
    var isSubmitting by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        val newErrors = mutableMapOf<String, String>()
        if (fullName.isBlank()) newErrors["fullName"] = "Full name is required"
        if (officerId.isBlank()) newErrors["officerId"] = "Officer ID is required"
        if (division.isBlank()) newErrors["division"] = "GN Division is required"
        if (email.isBlank() || !email.contains("@")) newErrors["email"] = "Valid email is required"
        if (phone.isBlank() || phone.length < 9) newErrors["phone"] = "Valid phone number is required"
        if (password.length < 4) newErrors["password"] = "Password must be at least 4 characters"
        if (password != confirmPassword) newErrors["confirmPassword"] = "Passwords do not match"

        errors = newErrors
        return newErrors.isEmpty()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF2F6FF),
                        Color.White,
                        Color(0xFFEEF4FF)
                    )
                )
            )
            .imePadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.15f)),
                            onClick = onBackClick
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "Officer Registration",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Official Emblem Badge
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color(0xFFD6E2FB), CircleShape)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_splash_badge),
                        contentDescription = "Emblem",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Register GN Officer Account",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Enter your official appointment details",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Registration Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        AuthTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = "Full Name",
                            iconRes = R.drawable.ic_input_person,
                            error = errors["fullName"]
                        )

                        AuthTextField(
                            value = officerId,
                            onValueChange = { officerId = it },
                            label = "Officer ID (e.g. GN/WP/GM/0142)",
                            iconRes = R.drawable.ic_input_id,
                            error = errors["officerId"]
                        )

                        AuthTextField(
                            value = division,
                            onValueChange = { division = it },
                            label = "GN Division (e.g. 142 - Mahara Central)",
                            iconRes = R.drawable.ic_input_location,
                            error = errors["division"]
                        )

                        AuthTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Official Email",
                            iconRes = R.drawable.ic_input_email,
                            keyboardType = KeyboardType.Email,
                            error = errors["email"]
                        )

                        AuthTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Contact Phone",
                            iconRes = R.drawable.ic_input_phone,
                            keyboardType = KeyboardType.Phone,
                            error = errors["phone"]
                        )

                        AuthTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password / PIN",
                            iconRes = R.drawable.ic_input_lock,
                            isPassword = true,
                            error = errors["password"]
                        )

                        AuthTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Confirm Password",
                            iconRes = R.drawable.ic_input_lock,
                            isPassword = true,
                            error = errors["confirmPassword"]
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                if (validate()) {
                                    isSubmitting = true
                                    scope.launch {
                                        authRepository.registerOfficer(
                                            fullName = fullName,
                                            officerId = officerId,
                                            division = division,
                                            email = email,
                                            phone = phone,
                                            password = password
                                        )
                                        isSubmitting = false
                                        onRegistrationSuccess()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HeaderBluePrimary),
                            enabled = !isSubmitting
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = Color.White,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text("Complete Registration", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "© $currentYear Government of Sri Lanka",
                    fontSize = 11.sp,
                    color = TextSecondary.copy(alpha = 0.65f)
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
