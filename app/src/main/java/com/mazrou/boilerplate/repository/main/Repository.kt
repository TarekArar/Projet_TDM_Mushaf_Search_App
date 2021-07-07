package com.mazrou.boilerplate.repository.main

import com.mazrou.boilerplate.ui.main.state.MainViewState
import com.mazrou.boilerplate.util.DataState
import com.mazrou.boilerplate.util.StateEvent
import kotlinx.coroutines.flow.Flow

interface Repository  {

    fun getAllSurah(
        stateEvent: StateEvent,
    ): Flow<DataState<MainViewState>>

    fun getAllAyat(
        stateEvent: StateEvent,
    ): Flow<DataState<MainViewState>>

    fun getAllWorlds(
        stateEvent: StateEvent
    ): Flow<DataState<MainViewState>>

    fun getAllRacine(
        stateEvent: StateEvent
    ): Flow<DataState<MainViewState>>

    fun searchByRacine(
        stateEvent: StateEvent ,
        query : String
    ): Flow<DataState<MainViewState>>

}