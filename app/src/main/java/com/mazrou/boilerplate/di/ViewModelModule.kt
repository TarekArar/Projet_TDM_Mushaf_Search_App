package com.mazrou.boilerplate.di

import com.mazrou.boilerplate.ui.auth.AuthViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


val viewModelModule = Kodein.Module("viewModel") {
    bind<AuthViewModel>() with singleton {
        AuthViewModel(
            instance()
        )
    }
}