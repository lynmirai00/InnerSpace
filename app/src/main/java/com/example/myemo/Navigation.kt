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
import com.example.myemo.mainpage.Home
import com.example.myemo.signup.SignUp
sealed class Route(val path: String) {
    object Login : Route("Login")
    object SignUp : Route("SignUp")
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
                        if (email != null) {
                            navHostController.navigate(
                                Route.Home(email).path
                            ) {
                                popUpTo("login_flow") {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    onSignUpClick = {
                        navHostController.navigateToSingleTop(
                            Route.SignUp.path
                        )
                    }
                )
            }
            composable(route = Route.SignUp.path) {
                SignUp(
                    onSignUpClick = { email ->
                        if (email != null) {
                            navHostController.navigate(
                                Route.Home(email).path
                            ) {
                                popUpTo("login_flow") {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    onLoginClick = {
                        navHostController.navigateToSingleTop(
                            Route.Login.path
                        )
                    },
                )
            }
        }
        composable(
            route = "Home?email={email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { navBackStackEntry ->
            val email = navBackStackEntry.arguments?.getString("email") ?: ""
            Home(navController = navHostController, email = email)
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