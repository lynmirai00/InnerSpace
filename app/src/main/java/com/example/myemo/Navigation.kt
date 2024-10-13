package com.example.myemo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.myemo.login.Login
import com.example.myemo.mainpage.Account
import com.example.myemo.mainpage.Dashboard
import com.example.myemo.mainpage.Home
import com.example.myemo.signup.SignUp

sealed class Route(val path: String) {
    object Login : Route("Login")
    object SignUp : Route("SignUp")
    object Dashboard : Route("SignUp")
    object Account : Route("SignUp")
    data class Home(val email: String) : Route("Home?email=$email")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = "login_flow",
    ) {
        navigation(startDestination = Route.Login.path, route = "login_flow") {
            composable(route = Route.Login.path) {
                Login(
                    onLoginClick = { email ->
                        email?.let {
                            navHostController.navigate(Route.Home(it).path) {
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
                    onSignUpClick = { email ->
                        email?.let {
                            navHostController.navigate(Route.Home(it).path) {
                                popUpTo("login_flow") { inclusive = true }
                            }
                        }
                    },
                    onLoginClick = {
                        navHostController.navigateToSingleTop(Route.Login.path)
                    }
                )
            }
            composable(
                route = "Home?email={email}",
                arguments = listOf(navArgument("email") { defaultValue = "" })
            ) { navBackStackEntry ->
                val email = navBackStackEntry.arguments?.getString("email") ?: ""
                Home(
                    email = email,
                    onNavigateToDashboard = {
                        // Thực hiện điều hướng đến trang nhật ký ở đây
                        navHostController.navigateToSingleTop(Route.Dashboard.path)
                    },
                    onNavigateToAccount = {
                        // Thực hiện điều hướng đến trang cài đặt tài khoản ở đây
                        navHostController.navigateToSingleTop(Route.Account.path)
                    }
                )
            }
            composable(route = Route.Dashboard.path) {
                Dashboard(
                    onNavigateToHome = {
                        // Thực hiện điều hướng đến trang nhật ký ở đây
                        navHostController.navigate(Route.Home(it.toString()).path) {
                            popUpTo("login_flow") { inclusive = true }
                        }
                    },
                    onNavigateToAccount = {
                        // Thực hiện điều hướng đến trang cài đặt tài khoản ở đây
                        navHostController.navigateToSingleTop(Route.Account.path)
                    }
                )
            }
            composable(route = Route.Account.path) {
                Account(
                    onNavigateToHome = {
                        // Thực hiện điều hướng đến trang nhật ký ở đây
                        navHostController.navigate(Route.Home(it.toString()).path) {
                            popUpTo("login_flow") { inclusive = true }
                        }
                    },
                    onNavigateToDashboard = {
                        // Thực hiện điều hướng đến trang cài đặt tài khoản ở đây
                        navHostController.navigateToSingleTop(Route.Dashboard.path)
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
