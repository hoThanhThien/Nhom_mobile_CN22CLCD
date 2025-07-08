package com.mb_an.tour_booking.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home     : BottomNavItem("home",     Icons.Default.Home,      "Home")
    object Bookings : BottomNavItem("bookings", Icons.Default.Done,      "Đã đặt")
    object Profile  : BottomNavItem("profile",  Icons.Default.Person,    "Tài khoản")
}