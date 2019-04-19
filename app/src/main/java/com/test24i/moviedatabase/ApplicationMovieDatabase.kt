package com.test24i.moviedatabase

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.test24i.moviedatabase.utils.isTablet
import timber.log.Timber

class ApplicationMovieDatabase : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        registerActivityLifecycleCallbacks(object : ActivityLifecycleAdapter() {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activity?.requestedOrientation = if (isTablet()) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        })
    }

    abstract class ActivityLifecycleAdapter : ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
        }
        override fun onActivityResumed(activity: Activity?) {
        }
        override fun onActivityStarted(activity: Activity?) {
        }
        override fun onActivityDestroyed(activity: Activity?) {
        }
        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }
        override fun onActivityStopped(activity: Activity?) {
        }
        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }

    }
}
