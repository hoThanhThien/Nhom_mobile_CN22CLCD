// app/src/main/java/com/mb_an/tour_booking/di/appModule.kt
package com.mb_an.tour_booking.di

import android.app.Activity
import com.mb_an.tour_booking.data.repository.*
import com.mb_an.tour_booking.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single { AuthRepository() }
    single { TourRepository() }
    single { BookingRepository() }
    single { CategoryRepository() }
    factory { (activity: Activity) -> GoogleSignInRepository(activity) }

    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }         // <–– TourRepository, CategoryRepository
    viewModel { BookingViewModel(get(), get()) }      // <–– BookingRepository, AuthRepository
    viewModel { (activity: Activity) ->
        GoogleSignInViewModel(get { parametersOf(activity) })
    }
}
