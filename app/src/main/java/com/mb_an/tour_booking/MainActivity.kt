package com.mb_an.tour_booking
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mb_an.tour_booking.presentation.navigation.AppNavGraph

import com.mb_an.tour_booking.ui.theme.TourBookingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TourBookingTheme { // ✅ Material 3 Theme
                AppNavGraph()  // ✅ Điều hướng toàn bộ UI
            }


                        }
                    }
                }
