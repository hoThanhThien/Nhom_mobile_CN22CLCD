// app/src/main/java/com/mb_an/tour_booking/presentation/navigation/AppNavGraph.kt
package com.mb_an.tour_booking.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import org.koin.androidx.compose.getViewModel
import com.mb_an.tour_booking.presentation.screens.home.HomeScreen
import com.mb_an.tour_booking.presentation.screens.bookings.BookingsScreen
import com.mb_an.tour_booking.presentation.screens.booking.BookingDetailScreen
import com.mb_an.tour_booking.presentation.screens.profile.ProfileScreen
import com.mb_an.tour_booking.presentation.screens.auth.LoginScreen
import com.mb_an.tour_booking.presentation.screens.auth.RegisterScreen
import com.mb_an.tour_booking.presentation.screens.auth.ForgotPasswordScreen
import com.mb_an.tour_booking.presentation.screens.booking.BookingDetailScreen
import com.mb_an.tour_booking.presentation.screens.splash.SplashScreen
import com.mb_an.tour_booking.presentation.viewmodel.HomeViewModel
import com.mb_an.tour_booking.presentation.viewmodel.BookingViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    Scaffold(bottomBar = { BottomBar(navController) }) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Splash,
            modifier         = Modifier.padding(innerPadding)
        ) {
            // 1. Splash
            composable(Screen.Splash) {
                SplashScreen { isLoggedIn ->
                    val dest = if (isLoggedIn) BottomNavItem.Home.route else Screen.Login
                    navController.navigate(dest) {
                        popUpTo(Screen.Splash) { inclusive = true }
                    }
                }
            }

            // 2. Authentication
            composable(Screen.Login) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(Screen.Login) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register) },
                    onNavigateToForgot = { navController.navigate(Screen.Forgot) }
                )
            }
            composable(Screen.Register) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(Screen.Register) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Screen.Forgot) {
                ForgotPasswordScreen(onNavigateToLogin = { navController.popBackStack() })
            }

            // 3. Main tabs
            composable(BottomNavItem.Home.route) {
                val homeVm: HomeViewModel = getViewModel()
                HomeScreen(
                    categories = homeVm.categories,
                    tours = homeVm.tourList,
                    onCategoryClick = { /*…*/ },
                    onTourClick = { tour ->
                        navController.navigate("${DetailScreen.Route}/${tour.id}")
                    }
                )
            }
            composable(BottomNavItem.Bookings.route) {
                val bookingVm: BookingViewModel = getViewModel()
                BookingsScreen(
                    bookings = bookingVm.bookingList,
                    onCancel = { bookingVm.cancelBooking(it) }
                )
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(onLogout = {
                    navController.navigate(Screen.Login) { popUpTo(0) }
                })
            }

            // 4. Chi tiết Tour (không hiển thị trong BottomBar)
            composable(
                route = "${DetailScreen.Route}/{${DetailScreen.ArgId}}",
                arguments = listOf(navArgument(DetailScreen.ArgId) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->

                // 1) Nhớ NavBackStackEntry của Home bằng remember với key NavController + route
                val homeEntry = remember(navController, BottomNavItem.Home.route) {
                    navController.getBackStackEntry(BottomNavItem.Home.route)
                }

                // 2) Lấy đúng HomeViewModel đã load tours từ HomeScreen
                val homeVm: HomeViewModel = getViewModel(viewModelStoreOwner = homeEntry)

                // 3) Lấy BookingViewModel bình thường
                val bookingVm: BookingViewModel = getViewModel()

                // 4) Parse tourId từ args
                val tourId = backStackEntry.arguments?.getString(DetailScreen.ArgId).orEmpty()
                val tour = homeVm.tourList.firstOrNull { it.id == tourId }

                if (tour != null) {
                    BookingDetailScreen(
                        tour = tour,
                        bookingState = bookingVm.bookingState,
                        onBook = { bookingVm.bookTour(tour) },
                        onBookingSuccess = { bookingVm.resetState() },
                        onBookingError = { bookingVm.resetState() },
                        onResetState = { bookingVm.resetState() }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        // fallback về Home
                        navController.navigate(BottomNavItem.Home.route) { popUpTo(0) }
                    }
                }
            }
        }
    }
    }