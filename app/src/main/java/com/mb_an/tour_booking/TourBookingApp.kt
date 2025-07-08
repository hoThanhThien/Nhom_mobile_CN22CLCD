package com.mb_an.tour_booking

import android.app.Application
import com.google.firebase.FirebaseApp
import com.mb_an.tour_booking.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TourBookingApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // ✅ Init Firebase
        FirebaseApp.initializeApp(this)

        // ✅ Init Dependency Injection (Koin)
        startKoin {
            androidContext(this@TourBookingApp)
            modules(appModule)
        }
    }
}
