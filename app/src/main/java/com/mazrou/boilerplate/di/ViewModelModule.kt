package com.mazrou.boilerplate.di

import com.mazrou.boilerplate.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = Kodein.Module("viewModel") {
    bind<MainViewModel>() with singleton {
        MainViewModel(
            instance()
        )
    }
}