package com.test.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.viewModels.MainActivityViewModelImpl
import com.test.viewModels.MainViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModelImpl::class)
    abstract fun provideMainActivityViewViewModel(mainActivityViewModel: MainActivityViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModelImpl::class)
    abstract fun provideMainViewViewModel(mainViewModel: MainViewModelImpl): ViewModel
}