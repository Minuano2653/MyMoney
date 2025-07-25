package com.example.mymoney

import android.app.Application
import com.example.mymoney.di.AppComponent
import com.example.mymoney.di.DaggerAppComponent

/**
 * Класс приложения, точка входа для Dependency Injection.
 */
class MyMoneyApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }
}
