package com.yeditepe.mindup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yeditepe.mindup.navigation.MyAppNavigation
import com.yeditepe.mindup.ui.theme.MindUPTheme
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.yeditepe.mindup.viewmodel.MoodViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import android.content.pm.PackageManager
import android.os.Build
import com.yeditepe.mindup.notification.NotificationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Bildirim izinlerini kontrol et
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != 
                PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
        
        // Hatırlatıcıyı başlat
        NotificationHelper(this).scheduleReminder()
        
        val authViewModel: AuthViewModel by viewModels()
        val moodViewModel: MoodViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MoodViewModel(application) as T
                }
            }
        }
        
        setContent {
            MindUPTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                MyAppNavigation(
                    modifier = Modifier,
                    authViewModel = authViewModel,
                    moodViewModel = moodViewModel,
                    drawerState = drawerState
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && 
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            NotificationHelper(this).scheduleReminder()
        }
    }
}

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel : AuthViewModel by viewModels()
        setContent {
            FirebaseAuthDemoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(modifier =  Modifier.padding(innerPadding),authViewModel = authViewModel)
                }
            }
        }
    }
}
*/

