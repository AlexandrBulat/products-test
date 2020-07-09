package com.test.di

import androidx.fragment.app.Fragment
import com.test.view.DetailView
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Subcomponent
interface DetailComponent : AndroidInjector<DetailView> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<DetailView>()
}

@Module(subcomponents = [(DetailComponent::class)])
abstract class DetailFragmentBuilder {
    @Binds
    @IntoMap
    @FragmentKey(DetailView::class)
    abstract
    fun bindScenesInjectorFactory(builder: DetailComponent.Builder):
            AndroidInjector.Factory<out Fragment>
}

