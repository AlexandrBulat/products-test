package com.test.di

import androidx.fragment.app.Fragment
import com.test.view.MainView
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Subcomponent
interface MainComponent : AndroidInjector<MainView> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainView>()
}

@Module(subcomponents = [(MainComponent::class)])
abstract class MainFragmentBuilder {
    @Binds
    @IntoMap
    @FragmentKey(MainView::class)
    abstract
    fun bindScenesInjectorFactory(builder: MainComponent.Builder):
            AndroidInjector.Factory<out Fragment>
}

