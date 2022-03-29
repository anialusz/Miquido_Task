package com.example.miquido.dagger

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory {
        return viewModelFactory
    }
}