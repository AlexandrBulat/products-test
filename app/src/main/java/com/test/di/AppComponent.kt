package com.test.di

import com.test.App
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AndroidSupportInjectionModule::class),
        (MainActivityBuilder::class), (ViewModelModule::class),
        (ServicesModule::class), (ObjectsModule::class),(MainFragmentBuilder::class)]
)
interface AppComponent {
    fun inject(app: App)
}