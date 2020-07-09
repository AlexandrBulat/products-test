package com.test

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import com.test.di.DaggerAppComponent
import com.test.di.ObjectsModule
import com.test.di.ServicesModule
import dagger.android.*
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject


class App : Application(), HasActivityInjector, HasServiceInjector, HasBroadcastReceiverInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var dispatchingBroadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()
        initDi()
        RxJavaPlugins.reset()
    }

    private fun initDi() {
        DaggerAppComponent.builder()
            .objectsModule(ObjectsModule())
            .servicesModule(ServicesModule(this))
            .build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity>? = dispatchingActivityInjector

    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector

    override fun broadcastReceiverInjector() = dispatchingBroadcastReceiverInjector
}