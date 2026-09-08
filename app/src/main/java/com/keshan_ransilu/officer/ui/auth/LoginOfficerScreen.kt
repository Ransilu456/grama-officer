package com.keshan_ransilu.officer.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.data.model.OfficerAccount
import com.keshan_ransilu.officer.repository.OfficerAuthRepository
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun LoginOfficerScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { OfficerAuthRepository(context) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }

    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var savedAccount by remember { mutableStateOf<OfficerAccount?>(null) }

    LaunchedEffect(Unit) {
        savedAccount = authRepository.getOfficerAccount()
        savedAccount?.let {
            if (identifier.isBlank()) identifier = it.officerId
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 28.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Official Shield Emblem
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF4F7FC))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_splash_badge),
                    contentDescription = "Official Emblem",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Officer Login",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "ග්‍රාම නිලධාරී පරිපාලන පද්ධතිය",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            if (savedAccount != null && savedAccount?.division?.isNotBlank() == true) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = HeaderBluePrimary.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "Division: ${savedAccount!!.division}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HeaderBluePrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Error alert
            if (errorMessage != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = errorMessage!!,
                            color = Color(0xFFD32F2F),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Input fields
            OutlinedTextField(
                value = identifier,
                onValueChange = {
                    identifier = it
                    errorMessage = null
                },
                label = { Text("Officer ID / Email") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_input_id),
                        contentDescription = null,
                        tint = HeaderBluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HeaderBluePrimary,
                    unfocusedBorderColor = Color(0xFFD0D7E2)
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_input_lock),
                        contentDescription = null,
                        tint = HeaderBluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HeaderBluePrimary,
                    unfocusedBorderColor = Color(0xFFD0D7E2)
                )
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (identifier.isBlank() || password.isBlank()) {
                        errorMessage = "Please enter both identifier and password"
                        return@Button
                    }
                    scope.launch {
                        isSubmitting = true
                        val success = authRepository.login(identifier.trim(), password.trim())
                        isSubmitting = false
                        if (success) {
                            onLoginSuccess()
                        } else {
                            errorMessage = "Invalid credentials. Please try again."
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
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
                        text = "Login to Portal",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            TextButton(
                onClick = onNavigateToRegister
            ) {
                Text(
                    text = "Register New Officer Account",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HeaderBluePrimary
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "© $currentYear Developed By E Marketing Paradice",
                fontSize = 11.sp,
                color = TextSecondary.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}
