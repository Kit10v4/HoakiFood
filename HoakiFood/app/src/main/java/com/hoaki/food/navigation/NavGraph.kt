package com.hoaki.food.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hoaki.food.ui.screens.*
import com.hoaki.food.ui.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Logo.route
    ) {
        composable(Screen.Logo.route) {
            LogoScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Logo.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Screen.Login.route}?registered={registered}",
            arguments = listOf(navArgument("registered") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { backStackEntry ->
            val registered = backStackEntry.arguments?.getBoolean("registered") ?: false
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                showRegistrationSuccess = registered
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen1(
                onRegisterSuccess = {
                    navController.navigate("${Screen.Login.route}?registered=true") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen1(
                onFoodClick = { foodId ->
                    navController.navigate(Screen.FoodDetail.createRoute(foodId))
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route) { launchSingleTop = true }
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                        popUpTo(Screen.Home.route)
                    }
                },
                onFabClick = {
                    navController.navigate(Screen.Cart.route) { launchSingleTop = true }
                },
                onFavoritesClick = { // Added
                    navController.navigate(Screen.Favorites.route) { 
                        launchSingleTop = true 
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(
            route = Screen.FoodDetail.route,
            arguments = listOf(navArgument("foodId") { type = NavType.LongType })
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getLong("foodId") ?: 0L
            FoodDetailScreen(
                foodId = foodId,
                onBackClick = { navController.popBackStack() },
                onCartClick = { navController.navigate(Screen.Cart.route) }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onBackClick = { navController.popBackStack() },
                onCheckoutClick = {
                    navController.navigate(Screen.Checkout.route)
                }
            )
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onBackClick = { navController.popBackStack() },
                onOrderSuccess = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onManageAddressClick = {
                    navController.navigate(Screen.AddressList.route)
                }
            )
        }

        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(
                onBackClick = { navController.popBackStack() },
                onOrderClick = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                }
            )
        }

        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
            OrderDetailScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onOrderHistoryClick = {
                    navController.navigate(Screen.OrderHistory.route)
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route) { 
                        launchSingleTop = true 
                        popUpTo(Screen.Home.route)
                    }
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onHomeClick = {
                    navController.navigate(Screen.Home.route) { 
                        launchSingleTop = true
                        popUpTo(Screen.Home.route) { inclusive = true } 
                    }
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route) { launchSingleTop = true }
                },
                onFabClick = { // Added
                    navController.navigate(Screen.Cart.route) { launchSingleTop = true }
                },
                onAddressClick = {
                    navController.navigate(Screen.AddressList.route)
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onFoodClick = { foodId ->
                    navController.navigate(Screen.FoodDetail.createRoute(foodId))
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() },
                onFoodClick = { foodId ->
                    navController.navigate(Screen.FoodDetail.createRoute(foodId))
                }
            )
        }

        composable(Screen.AddressList.route) {
            AddressListScreen(
                onBackClick = { navController.popBackStack() },
                onAddAddressClick = {
                    navController.navigate(Screen.AddAddress.route)
                },
                onEditAddressClick = { addressId ->
                    navController.navigate(Screen.EditAddress.createRoute(addressId))
                }
            )
        }

        composable(Screen.AddAddress.route) {
            AddAddressScreen(
                addressId = null,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditAddress.route,
            arguments = listOf(navArgument("addressId") { type = NavType.LongType })
        ) { backStackEntry ->
            val addressId = backStackEntry.arguments?.getLong("addressId") ?: 0L
            AddAddressScreen(
                addressId = addressId,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}