package com.keshan_ransilu.officer.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
            .background(Color.White)
            .imePadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
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
                    color = Color(0xFFF4F7FC)
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
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4F7FC))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_splash_badge),
                        contentDescription = "Emblem",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Full Name
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name (නම)") },
                    isError = errors.containsKey("fullName"),
                    supportingText = errors["fullName"]?.let { { Text(it, color = Color(0xFFD32F2F)) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HeaderBluePrimary,
                        unfocusedBorderColor = Color(0xFFD0D7E2)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Officer ID
                OutlinedTextField(
                    value = officerId,
                    onValueChange = { officerId = it },
                    label = { Text("Officer ID / Badge (e.g. GN/WP/GM/0142)") },
                    isError = errors.containsKey("officerId"),
                    supportingText = errors["officerId"]?.let { { Text(it, color = Color(0xFFD32F2F)) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HeaderBluePrimary,
                        unfocusedBorderColor = Color(0xFFD0D7E2)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // GN Division
                OutlinedTextField(
                    value = division,
                    onValueChange = { division = it },
                    label = { Text("GN Division (e.g. 142 - Mahara Central)") },
                    isError = errors.containsKey("division"),
                    supportingText = errors["division"]?.let { { Text(it, color = Color(0xFFD32F2F)) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HeaderBluePrimary,
                        unfocusedBorderColor = Color(0xFFD0D7E2)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    isError = errors.containsKey("email"),
                    supportingText = errors["email"]?.let { { Text(it, color = Color(0xFFD32F2F)) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HeaderBluePrimary,
                        unfocusedBorderColor = Color(0xFFD0D7E2)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Phone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Official Phone") },
                    isError = errors.containsKey("phone"),
                    supportingText = errors["phone"]?.let { { Text(it, color = Color(0xFFD32F2F)) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HeaderBluePrimary,
                        unfocusedBorderColor = Color(0xFFD0D7E2)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Create Password / PIN") },
                    isError = errors.containsKey("password"),
                    supportingText = errors["password"]?.let { { Text(it, color = Color(0xFFD32F2F)) } },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HeaderBluePrimary,
                        unfocusedBorderColor = Color(0xFFD0D7E2)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password / PIN") },
                    isError = errors.containsKey("confirmPassword"),
                    supportingText = errors["confirmPassword"]?.let { { Text(it, color = Color(0xFFD32F2F)) } },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HeaderBluePrimary,
                        unfocusedBorderColor = Color(0xFFD0D7E2)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (validate()) {
                            scope.launch {
                                isSubmitting = true
                                val registered = authRepository.registerOfficer(
                                    fullName = fullName.trim(),
                                    officerId = officerId.trim(),
                                    division = division.trim(),
                                    email = email.trim(),
                                    phone = phone.trim(),
                                    password = password.trim()
                                )
                                isSubmitting = false
                                if (registered) {
                                    onRegistrationSuccess()
                                }
                            }
                        }
                    },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HeaderBluePrimary)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Complete Registration",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
