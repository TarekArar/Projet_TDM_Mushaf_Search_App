package com.mazrou.boilerplate

import android.app.Application
import android.content.Context
import com.mazrou.boilerplate.di.ViewModelFactory
import com.mazrou.boilerplate.di.bindViewModel
import com.mazrou.boilerplate.di.viewModelModule
import com.mazrou.boilerplate.network.WebService
import com.mazrou.boilerplate.perssistance.AppDataBase
import com.mazrou.boilerplate.repository.auth.AuthRepository
import com.mazrou.boilerplate.repository.auth.AuthRepositoryImpl
import com.mazrou.boilerplate.session.SessionManager
import com.mazrou.boilerplate.ui.auth.AuthViewModel
import com.mazrou.boilerplate.util.PreferenceKeys.Companion.APP_PREFERENCES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ApplicationClass : Application(), KodeinAware {

    @ExperimentalCoroutinesApi
    @FlowPreview
    @InternalCoroutinesApi
    override val kodein: Kodein by Kodein.lazy {

        import(androidXModule(this@ApplicationClass))

        // web service
        bind<WebService>() with singleton { WebService.invoke() }

        // database
        bind<AppDataBase>() with singleton { AppDataBase(context = instance()) }

        // session manager
        bind<SessionManager>() with  singleton { SessionManager(
            authTokenDao = instance<AppDataBase>().getAuthTokenDao(),
        ) }


        // repositories
        bind<AuthRepository>() with singleton {
            (
                AuthRepositoryImpl(
                    sessionManager = instance(),
                    authTokenDao = instance<AppDataBase>().getAuthTokenDao(),
                    accountPropertiesDao = instance<AppDataBase>().getAccountPropertiesDao(),
                    webService = instance(),
                    sharedPreferences = instance<Context>().getSharedPreferences(
                        APP_PREFERENCES, Context.MODE_PRIVATE
                    )
                )
                )
        }

        // viewModels
//        bindViewModel<AuthViewModel>() with provider {
//            AuthViewModel(
//                repository = instance()
//            )
//        }
        //bind() from singleton { ViewModelFactory(kodein) }
        import(viewModelModule)

    }
}

