package com.example.miquido.dagger

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelFactoryModule::class,
        AppModule::class
    ]
)
interface AppComponent {

    val viewModelFactory: ViewModelProvider.Factory

    object Manager {
        private var instance: AppComponent? = null

        fun init(): AppComponent {
            if (instance == null) {
                instance = DaggerAppComponent.builder().build()
            }
            return instance!!
        }
    }

}