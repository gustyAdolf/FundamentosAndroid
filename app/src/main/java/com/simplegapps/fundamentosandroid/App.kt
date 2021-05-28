package com.simplegapps.fundamentosandroid

import android.app.Application
import com.simplegapps.fundamentosandroid.di.DIProvider

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        DIProvider.init(this)
    }
}