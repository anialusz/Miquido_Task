package com.example.miquido.dagger

import androidx.lifecycle.ViewModel
import com.example.miquido.viewModel.AddToDoItemViewModel
import com.example.miquido.viewModel.ToDoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(ToDoListViewModel::class)
    abstract fun bindToDoListViewModel(viewModel: ToDoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddToDoItemViewModel::class)
    abstract fun bindAddToDoItemViewModel(viewModel: AddToDoItemViewModel): ViewModel
}