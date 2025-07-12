// app/src/main/java/com/mb_an/tour_booking/presentation/navigation/AppNavGraph.kt
package com.mb_an.tour_booking.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.mb_an.tour_booking.presentation.viewmodel.AuthViewModel
import com.mb_an.tour_booking.presentation.viewmodel.HomeViewModel
import com.mb_an.tour_booking.presentation.viewmodel.BookingViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    // lấy AuthViewModel để biết có đang login hay không
    val authVm: AuthViewModel = getViewModel()
    val isLoggedIn by authVm.isLoggedIn.collectAsState()

    Scaffold(bottomBar = { BottomBar(navController) }) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = BottomNavItem.Home.route,     // → Bỏ Splash, vào thẳng Home
            modifier         = Modifier.padding(innerPadding)
        ) {
            // 1. Home
            composable(BottomNavItem.Home.route) {
                val homeVm: HomeViewModel = getViewModel()
                HomeScreen(
                    categories      = homeVm.categories,
                    tours           = homeVm.tourList,
                    onCategoryClick = { /*…*/ },
                    onTourClick     = { tour ->
                        navController.navigate("${DetailScreen.Route}/${tour.id}")
                    }
                )
            }
            composable(BottomNavItem.Bookings.route) {
                // 1. Lấy ViewModel
                val bookingVm: BookingViewModel = getViewModel()
                val authVm   : AuthViewModel    = getViewModel()

                // 2. Quan sát trạng thái đăng nhập
                val isLoggedIn by authVm.isLoggedIn.collectAsState()

                // 3. Mỗi khi isLoggedIn chuyển sang true thì tải lại danh sách booking
                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        bookingVm.loadBookings()
                    }
                }

                // 4. Hiển thị UI danh sách đã đặt
                BookingsScreen(
                    bookings     = bookingVm.bookingList,
                    onCancel     = { bookingVm.cancelBooking(it) },
                    onViewDetail = { booking ->
                        // Điều hướng sang màn Detail của tour
                        navController.navigate("${DetailScreen.Route}/${booking.tourId}")
                    }
                )
            }
            // 3. Profile
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(onLogout = {
                    authVm.logout()
                    // sau logout, quay về Home (đã là startDestination)
                    navController.navigate(BottomNavItem.Home.route) {
                        popUpTo(0)
                    }
                })
            }

            // 4. Login / Register / Forgot (chỉ mở khi cần)
            composable(Screen.Login) {
                LoginScreen(
                    onLoginSuccess       = {
                        // quay về DetailScreen vừa truy cập (nếu có) hoặc Home
                        navController.popBackStack()
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register) },
                    onNavigateToForgot   = { navController.navigate(Screen.Forgot) }
                )
            }
            composable(Screen.Register) {
                RegisterScreen(
                    onRegisterSuccess = { navController.popBackStack() },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Screen.Forgot) {
                ForgotPasswordScreen(onNavigateToLogin = { navController.popBackStack() })
            }

            // 5. DetailScreen (chỉ show khi bấm tour)
            composable(
                route     = "${DetailScreen.Route}/{${DetailScreen.ArgId}}",
                arguments = listOf(navArgument(DetailScreen.ArgId) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                // reuse HomeViewModel
                val homeEntry = remember(navController) {
                    navController.getBackStackEntry(BottomNavItem.Home.route)
                }
                val homeVm: HomeViewModel       = getViewModel(viewModelStoreOwner = homeEntry)
                val bookingVm: BookingViewModel = getViewModel()

                val tourId = backStackEntry.arguments?.getString(DetailScreen.ArgId).orEmpty()
                val tour   = homeVm.tourList.firstOrNull { it.id == tourId }

                if (tour != null) {
                    BookingDetailScreen(
                        tour          = tour,
                        bookingState  = bookingVm.bookingState,

                        // onBook giờ phải nhận 3 tham số
                        onBook = { startDate, endDate, guests ->
                            if (!isLoggedIn) {
                                navController.navigate(Screen.Login)
                            } else {
                                // gọi use-case bookTour với start/end/guests
                                bookingVm.bookTour(tour, startDate, endDate, guests)
                            }
                        },

                        onBookingSuccess = {
                            bookingVm.resetState()
                            // ví dụ: về tab Bookings sau khi đặt thành công
                            navController.navigate(BottomNavItem.Bookings.route) {
                                launchSingleTop = true
                            }
                        },

                        onBookingError = { errorMsg ->
                            bookingVm.resetState()
                            // TODO: show Snackbar hoặc Toast với errorMsg
                        },

                        onResetState = {
                            bookingVm.resetState()
                        }
                    )
                } else {
                    // nếu không tìm thấy tour thì về Home
                    LaunchedEffect(Unit) {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}
