package com.mazrou.boilerplate

import android.app.Application
import com.mazrou.boilerplate.di.viewModelModule
import com.mazrou.boilerplate.network.WebService
import com.mazrou.boilerplate.perssistance.AppDataBase
import com.mazrou.boilerplate.repository.main.Repository
import com.mazrou.boilerplate.repository.main.RepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
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

        // repositories
        bind<Repository>() with singleton {
                RepositoryImpl(
                    quranDao = instance<AppDataBase>().getQuranDao(),
                    webService = instance()
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

