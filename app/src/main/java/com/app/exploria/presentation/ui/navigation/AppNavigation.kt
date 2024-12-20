package com.app.exploria.presentation.ui.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.exploria.presentation.ui.features.detail.composables.DetailScreen
import com.app.exploria.presentation.ui.features.detail.composables.ReviewFormScreen
import com.app.exploria.presentation.ui.features.favorite.composables.FavoriteScreen
import com.app.exploria.presentation.ui.features.guider.composables.GuideListScreen
import com.app.exploria.presentation.ui.features.guider.composables.GuiderDetailScreen
import com.app.exploria.presentation.ui.features.home.composables.HomeScreen
import com.app.exploria.presentation.ui.features.planning.composables.CreatePlanningScreen
import com.app.exploria.presentation.ui.features.planning.composables.PlanningScreen
import com.app.exploria.presentation.ui.features.planning.composables.SecondPlanningScreen
import com.app.exploria.presentation.ui.features.planning.composables.SelectDestinationScreen
import com.app.exploria.presentation.ui.features.profile.composables.ProfileForm
import com.app.exploria.presentation.ui.features.profile.composables.ProfileScreen
import com.app.exploria.presentation.ui.features.register.composables.RegisterScreen
import com.app.exploria.presentation.ui.features.search.composables.SearchScreen
import com.app.exploria.presentation.ui.features.splash.composables.SplashScreen
import com.app.exploria.presentation.ui.features.survey.composables.SurveyScreen
import com.app.exploria.presentation.viewModel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val userState by mainViewModel.userModel.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.loadUser()
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen()
            LaunchedEffect(Unit) {
                delay(2000)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
        composable(Screen.Home.route) { HomeScreen(navController, userState) }
        composable(Screen.Plan.route) { PlanningScreen(navController) }
        composable(Screen.Favorite.route) { FavoriteScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController, mainViewModel) }
        composable(Screen.Register.route) { RegisterScreen(navController, mainViewModel) }
        composable(Screen.Survey.route) { SurveyScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController, mainViewModel) }
        composable(
            route = Screen.SecondPlan.route,
            arguments = listOf(navArgument("planId") { type = NavType.StringType })
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            SecondPlanningScreen(navController = navController, planId = planId)
        }
        composable(
            route = "select_destination_screen/{planId}/{destinationId}",
            arguments = listOf(
                navArgument("planId") { type = NavType.StringType },
                navArgument("destinationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            val destinationId = backStackEntry.arguments?.getString("destinationId")?.toIntOrNull()
            SelectDestinationScreen(
                navController = navController,
                planId = planId,
                destinationId = destinationId
            )
        }

        composable(
            route = "select_destination_screen/{planId}",
            arguments = listOf(
                navArgument("planId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            SelectDestinationScreen(
                navController = navController,
                planId = planId,
                destinationId = null
            )
        }

        composable(Screen.CreatePlan.route) { CreatePlanningScreen(navController, userState) }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val detailId = navBackStackEntry.arguments?.getString("id")
            DetailScreen(detailId, navController, userState)
        }
        composable(
            route = Screen.DetailGuide.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val detailId = navBackStackEntry.arguments?.getString("id")
            GuiderDetailScreen(detailId, navController)
        }
        composable(Screen.Search.route) { SearchScreen(navController) }
        composable(Screen.Guide.route) { GuideListScreen(navController) }
        composable(Screen.ProfileForm.route) { ProfileForm(navController, mainViewModel) }
        composable(
            route = Screen.ReviewForm.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val destinationId = navBackStackEntry.arguments?.getString("id")
            ReviewFormScreen(destinationId, navController)
        }
    }
}
