package com.test.di

import android.app.Activity
import com.test.view.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Subcomponent
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}

@Module(subcomponents = [(MainActivitySubcomponent::class)])
abstract class MainActivityBuilder {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract
    fun bindMainActivityInjectorFactory(builder: MainActivitySubcomponent.Builder):
            AndroidInjector.Factory<out Activity>
}

