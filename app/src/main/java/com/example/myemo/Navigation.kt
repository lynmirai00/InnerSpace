package com.example.myemo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.myemo.login.Login
import com.example.myemo.mainpage.account.Account
import com.example.myemo.mainpage.dashboard.Dashboard
import com.example.myemo.mainpage.home.Home
import com.example.myemo.signup.SignUp
import com.google.firebase.auth.FirebaseAuth

sealed class Route(val path: String) {
    data object Login : Route("Login")
    data object SignUp : Route("SignUp")
    data object Dashboard : Route("Dashboard")
    data object Account : Route("Account")
    data object Home : Route("Home")
}

@ExperimentalMaterial3Api
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyNavigation(navHostController: NavHostController) {
    val startDestination: String
    val route: String
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        startDestination = Route.Home.path
        route = "home_flow"
        Log.d("MyNavigation", "Current user: ${currentUser.email}")
    } else {
        startDestination = Route.Login.path
        route = "login_flow"
    }
    NavHost(
        navController = navHostController,
        startDestination = route,
    ) {
        navigation(startDestination = startDestination, route = route) {
            composable(route = Route.Login.path) {
                Login(
                    onLoginClick = { email ->
                        email?.let {
                            navHostController.navigate(Route.Home.path) {
                                popUpTo("login_flow") { inclusive = true }
                            }
                        }
                    },
                    onSignUpClick = {
                        navHostController.navigateToSingleTop(Route.SignUp.path)
                    }
                )
            }
            composable(route = Route.SignUp.path) {
                SignUp(
                    onSignUpClick = {
                        navHostController.navigate(Route.Home.path) {
                            popUpTo("login_flow") { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navHostController.navigateToSingleTop(Route.Login.path)
                    }
                )
            }
            composable(route = Route.Home.path) {
                Home(
                    onNavigateToDashboard = {
                        navHostController.navigateToSingleTop(Route.Dashboard.path)
                    },
                    onNavigateToAccount = {
                        navHostController.navigateToSingleTop(Route.Account.path)
                    }
                )
            }
            composable(route = Route.Dashboard.path) {
                Dashboard(
                    onNavigateToHome = {
                        navHostController.navigateToSingleTop(Route.Home.path)
                    },
                    onNavigateToAccount = {
                        navHostController.navigateToSingleTop(Route.Account.path)
                    }
                )
            }
            composable(route = Route.Account.path) {
                Account(
                    onNavigateToHome = {
                        navHostController.navigateToSingleTop(Route.Home.path)
                    },
                    onNavigateToDashboard = {
                        navHostController.navigateToSingleTop(Route.Dashboard.path)
                    },
                    onLogout = {
                        FirebaseAuth.getInstance().signOut() // Đăng xuất người dùng
                        navHostController.navigate(Route.Login.path) {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

fun NavController.navigateToSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

