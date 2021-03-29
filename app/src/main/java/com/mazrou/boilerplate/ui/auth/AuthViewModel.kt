package com.mazrou.boilerplate.ui.auth

import com.mazrou.boilerplate.model.AuthToken
import com.mazrou.boilerplate.repository.auth.AuthRepository
import com.mazrou.boilerplate.ui.BaseViewModel
import com.mazrou.boilerplate.ui.auth.state.AuthStateEvent
import com.mazrou.boilerplate.ui.auth.state.AuthViewState
import com.mazrou.boilerplate.ui.auth.state.LoginFields
import com.mazrou.boilerplate.ui.auth.state.RegistrationFields
import com.mazrou.boilerplate.util.*
import com.mazrou.boilerplate.util.Constants.Companion.INVALID_STATE_EVENT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
@FlowPreview
class AuthViewModel(
   private val repository: AuthRepository
)  : BaseViewModel<AuthViewState>() {


    override fun handleNewData(data: AuthViewState) {
        data.authToken?.let { authToken ->
            setAuthToken(authToken)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<AuthViewState>> = when(stateEvent){

            is AuthStateEvent.LoginAttemptEvent -> {
                repository.attemptLogin(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    password = stateEvent.password
                )
            }

            is AuthStateEvent.RegisterAttemptEvent -> {
                repository.attemptRegistration(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    username = stateEvent.username,
                    password = stateEvent.password,
                    confirmPassword = stateEvent.confirm_password
                )
            }

            is AuthStateEvent.CheckPreviousAuthEvent -> {
                repository.checkPreviousAuthUser(stateEvent)
            }

            else -> {
                flow{
                    emit(
                        DataState.error<AuthViewState>(
                            response = Response(
                                message = INVALID_STATE_EVENT,
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Error()
                            ),
                            stateEvent = stateEvent
                        )
                    )
                }
            }
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields){
        val update = getCurrentViewStateOrNew()
        if(update.registrationFields == registrationFields){
            return
        }
        update.registrationFields = registrationFields
        setViewState(update)
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if(update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        setViewState(update)
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken){
            return
        }
        update.authToken = authToken
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}