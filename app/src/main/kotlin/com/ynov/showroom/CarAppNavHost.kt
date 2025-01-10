package com.ynov.showroom

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun CarAppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "car_list"
    ) {
        carListScreen(navController = navController) { car ->
            navController.navigate("car_details/${car.make}/${car.model}/${car.year}")
        }

        composable(
            route = "car_details/{make}/{model}/{year}",
            arguments = listOf(
                navArgument("make") { type = NavType.StringType },
                navArgument("model") { type = NavType.StringType },
                navArgument("year") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val make = backStackEntry.arguments?.getString("make") ?: ""
            val model = backStackEntry.arguments?.getString("model") ?: ""
            val year = backStackEntry.arguments?.getInt("year") ?: 0

            CarDetailsScreen(
                make = make,
                model = model,
                year = year
            )
        }
    }
}