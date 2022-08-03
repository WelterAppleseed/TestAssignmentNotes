package com.example.testassignmentnotes

import android.app.Application
import com.example.testassignmentnotes.di.DependencyManager


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyManager.init(this)
    }
}