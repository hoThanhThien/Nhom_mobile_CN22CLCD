// app/src/main/java/com/mb_an/tour_booking/presentation/navigation/BottomBar.kt
//package com.mb_an.tour_booking.presentation.navigation
//
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.navigation.NavController
//import androidx.navigation.compose.currentBackStackEntryAsState

//@Composable
//fun BottomBar(navController: NavController) {
//    val items = listOf(
//        BottomNavItem.Home,
//        BottomNavItem.Bookings,
//        BottomNavItem.Profile
//    )
//
//    // Lấy route hiện tại
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = backStackEntry?.destination?.route
//
//    NavigationBar {
//        items.forEach { item ->
//            NavigationBarItem(
//                selected    = currentRoute == item.route,
//                icon        = { Icon(item.icon, contentDescription = item.label) },
//                label       = { Text(item.label) },
//                onClick     = {
//                    navController.navigate(item.route) {
//                        // Trả về màn đầu tiên của graph nếu đã ở trên cùng
//                        popUpTo(navController.graph.startDestinationId)
//                        launchSingleTop = true
//                    }
//                }
//            )
//        }
//    }
//}
package com.mb_an.tour_booking.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Bookings,
        BottomNavItem.Profile
    )

    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected    = currentRoute == item.route,
                icon        = { Icon(item.icon, contentDescription = item.label) },
                label       = { Text(item.label) },
                onClick     = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

