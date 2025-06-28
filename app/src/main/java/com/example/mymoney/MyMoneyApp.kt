package com.example.mymoney

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Класс приложения, точка входа для Hilt Dependency Injection.
 */
@HiltAndroidApp
class MyMoneyApp(): Application()