package com.mazrou.boilerplate.repository.auth

import com.mazrou.boilerplate.ui.auth.state.AuthViewState
import com.mazrou.boilerplate.util.DataState
import com.mazrou.boilerplate.util.StateEvent
import kotlinx.coroutines.flow.Flow

interface AuthRepository  {


    fun attemptLogin(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>>

    fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>>

    fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>>

    fun saveAuthenticatedUserToPrefs(email: String)

    fun returnNoTokenFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState>
}