package com.keshan_ransilu.officer.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.keshan_ransilu.officer.R
import com.keshan_ransilu.officer.data.registry.RegisterCatalog
import com.keshan_ransilu.officer.repository.OfficerAuthRepository
import com.keshan_ransilu.officer.ui.auth.LoginOfficerScreen
import com.keshan_ransilu.officer.ui.auth.RegisterOfficerScreen
import com.keshan_ransilu.officer.ui.auth.SplashScreen
import com.keshan_ransilu.officer.ui.auth.WelcomeStartScreen
import com.keshan_ransilu.officer.ui.home.HomeScreen
import com.keshan_ransilu.officer.ui.modulelist.ModuleListScreen
import com.keshan_ransilu.officer.ui.notifications.NotificationsScreen
import com.keshan_ransilu.officer.ui.profile.ProfileScreen
import com.keshan_ransilu.officer.ui.recordform.RecordFormScreen
import com.keshan_ransilu.officer.ui.search.GlobalSearchScreen
import com.keshan_ransilu.officer.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val authRepository = remember { OfficerAuthRepository(context) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var hasOfficerAccount by remember { mutableStateOf<Boolean?>(null) }
    var showModuleSelectorSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        hasOfficerAccount = authRepository.hasOfficerAccount()
    }

    val isAuthRoute = currentRoute in listOf("splash", "welcome", "register_officer", "login")
    val isFormRoute = currentRoute?.contains("form") == true

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = !isAuthRoute,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
                modifier = Modifier.width(320.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    DrawerProfileHeader(
                        onProfileClick = {
                            navController.navigate("profile") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        onClose = { scope.launch { drawerState.close() } }
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "ALL REGISTERS & SERVICES",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(RegisterCatalog) { module ->
                                val isSelected = currentRoute == "module/${module.id}"
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = true, color = HeaderBluePrimary.copy(alpha = 0.15f)),
                                            onClick = {
                                                navController.navigate("module/${module.id}") {
                                                    popUpTo("home") { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                                scope.launch { drawerState.close() }
                                            }
                                        ),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) HeaderBluePrimary.copy(alpha = 0.08f) else Color.Transparent
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = module.iconRes),
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp)
                                        )

                                        Spacer(Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                module.titleEn,
                                                color = if (isSelected) HeaderBluePrimary else TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                            Text(
                                                module.titleSi,
                                                color = TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }

                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            tint = if (isSelected) HeaderBluePrimary else Color(0xFFC0C7D5),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    DrawerFooter()
                }
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                if (!isAuthRoute && !isFormRoute && currentRoute != null) {
                    CustomBottomNavBar(
                        currentRoute = currentRoute,
                        onHomeClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        onNotificationClick = {
                            navController.navigate("notifications") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onFilterClick = {
                            showModuleSelectorSheet = true
                        },
                        onSearchClick = {
                            navController.navigate("global_search") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onProfileClick = {
                            navController.navigate("profile") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            },
            containerColor = ScreenBg,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScreenBg),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { 300 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(300))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -300 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -300 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(300))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { 300 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) {
                composable("splash") {
                    SplashScreen(
                        onNavigateNext = {
                            val dest = when {
                                hasOfficerAccount == false -> "welcome"
                                !authRepository.isLoggedIn() -> "login"
                                else -> "home"
                            }
                            navController.navigate(dest) {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    )
                }

                composable("welcome") {
                    WelcomeStartScreen(
                        hasAccount = hasOfficerAccount == true,
                        onGetStartedClick = {
                            navController.navigate("register_officer")
                        },
                        onLoginClick = {
                            navController.navigate("login")
                        }
                    )
                }

                composable("register_officer") {
                    RegisterOfficerScreen(
                        onBackClick = { navController.popBackStack() },
                        onRegistrationSuccess = {
                            hasOfficerAccount = true
                            navController.navigate("home") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        }
                    )
                }

                composable("login") {
                    LoginOfficerScreen(
                        onLoginSuccess = {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate("register_officer")
                        }
                    )
                }

                composable("home") {
                    HomeScreen(
                        onModuleClick = { moduleId ->
                            navController.navigate("module/$moduleId") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        },
                        onProfileClick = {
                            navController.navigate("profile") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable("notifications") {
                    NotificationsScreen(
                        onBackClick = { navController.popBackStack() },
                        onNavigateToModule = { modId ->
                            navController.navigate("module/$modId") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable("global_search") {
                    GlobalSearchScreen(
                        onBackClick = { navController.popBackStack() },
                        onRecordClick = { modId, recordId ->
                            navController.navigate("module/$modId/form?recordId=$recordId")
                        }
                    )
                }

                composable("profile") {
                    ProfileScreen(
                        onBackClick = { navController.popBackStack() },
                        onNavigateToModule = { modId ->
                            navController.navigate("module/$modId") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToNotifications = {
                            navController.navigate("notifications") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onLogout = {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }

                // Global Search Screen
                composable("search") {
                    com.keshan_ransilu.officer.ui.search.SearchScreen(
                        onBackClick = { navController.popBackStack() },
                        onRecordClick = { modId, recId ->
                            navController.navigate("module/$modId/form?recordId=$recId")
                        }
                    )
                }

                composable(
                    route = "module/{moduleId}",
                    arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                    ModuleListScreen(
                        moduleId = moduleId,
                        onBackClick = { navController.popBackStack() },
                        onAddClick = { navController.navigate("module/$moduleId/form") },
                        onRecordClick = { recordId ->
                            navController.navigate("module/$moduleId/form?recordId=$recordId")
                        },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }

                composable(
                    route = "module/{moduleId}/form?recordId={recordId}",
                    arguments = listOf(
                        navArgument("moduleId") { type = NavType.StringType },
                        navArgument("recordId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    ),
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { 600 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(300))
                    },
                    popExitTransition = {
                        slideOutVertically(
                            targetOffsetY = { 600 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(300))
                    }
                ) { backStackEntry ->
                    val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                    val recordId = backStackEntry.arguments?.getString("recordId")
                    RecordFormScreen(
                        moduleId = moduleId,
                        recordId = recordId,
                        onBackClick = { navController.popBackStack() },
                        onSaveSuccess = { navController.popBackStack() },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
            }

            if (showModuleSelectorSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showModuleSelectorSheet = false },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            "Quick Navigation",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            "Select a register or service to view",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 380.dp)
                        ) {
                            items(RegisterCatalog) { item ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = ScreenBg,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            showModuleSelectorSheet = false
                                            navController.navigate("module/${item.id}") {
                                                popUpTo("home") { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = item.iconRes),
                                            contentDescription = null,
                                            modifier = Modifier.size(34.dp)
                                        )
                                        Spacer(modifier = Modifier.width(14.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                item.titleEn,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                item.titleSi,
                                                color = TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            tint = TextSecondary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
