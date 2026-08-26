package com.keshan_ransilu.officer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.keshan_ransilu.officer.ui.navigation.NavGraph
import com.keshan_ransilu.officer.ui.theme.OfficerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            OfficerTheme {
                NavGraph()
            }
        }
    }
}
