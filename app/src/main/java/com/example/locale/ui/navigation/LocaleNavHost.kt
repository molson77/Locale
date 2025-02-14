package com.example.locale.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.locale.ui.screens.BusinessDetailsScreen
import com.example.locale.ui.screens.BusinessScreen
import com.example.locale.ui.screens.HomeScreen
import com.example.locale.ui.viewmodels.BusinessDetailsViewModel
import com.example.locale.ui.viewmodels.BusinessViewModel

@Composable
fun LocaleNavHolder(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    LocaleNavHost(
        navController = navController,
        modifier = modifier
    )
}

@Composable
fun LocaleNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { entry ->
            HomeScreen(
                onCityClicked = {
                    navController.navigate("business/${it.name}")
                }
            )
        }
        composable(
            route = "business/{location}",
            arguments = listOf(
                navArgument("location") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val location = entry.arguments?.getString("location") ?: ""
            BusinessScreen(
                location = location,
                onBusinessClicked = {
                    navController.navigate("business_details/${it.id}")
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "business_details/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val id = entry.arguments?.getString("id") ?: ""
            BusinessDetailsScreen(
                id = id,
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
}


