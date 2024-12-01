package com.app.exploria.presentation.ui.features.profile.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.exploria.R
import com.app.exploria.presentation.ui.features.common.CustomHeaderTitle
import com.app.exploria.presentation.ui.navigation.Screen
import com.app.exploria.presentation.viewModel.MainViewModel
import com.app.exploria.presentation.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController, mainViewModel: MainViewModel
) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val userData by profileViewModel.userData.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()
    val user by mainViewModel.user.collectAsState()
    val userModel by mainViewModel.userModel.collectAsState()

    LaunchedEffect(user) {
        user?.let {
            println("Fetching user data for ID: ${it.id}")
            profileViewModel.getDataUser(it.id)
        } ?: run {
            println("User data is null")
        }
        userModel?.let {
            println("Fetching user data for ID: ${it.token}")
        } ?: run {
            println("User data is null")
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                CustomHeaderTitle(
                    onClick = { navController.popBackStack() },
                    title = "Profile"
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture and Name
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(130.dp)
                        .clip(CircleShape)
                )

                userData?.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }
            }

            SettingsListItem(title = "Ubah Profile")

            SettingsListItem(title = "FAQ")

            SettingsListItem(title = "Bantuan")

            SettingsListItem(title = "Keluar", onClick = {
                mainViewModel.logout()

                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Profile.route) { inclusive = true }
                    launchSingleTop = true
                }
            })

        }
    }
}

