package com.defey.labpuzzles.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.defey.labpuzzles.OnboardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen() {

    val viewModel: OnboardingViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Text(state.name)
}