package com.plearn.ytechassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.plearn.ytechassignment.ui.AppScreen
import com.plearn.ytechassignment.ui.theme.YTechAssignmentTheme
import com.plearn.ytechassignment.viewnodel.GridViewModel

class MainActivity : ComponentActivity() {

    private val gridViewModel = GridViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YTechAssignmentTheme {
                AppScreen(gridViewModel)
            }
        }
    }
}
