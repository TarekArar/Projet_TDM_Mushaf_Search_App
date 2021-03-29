package com.mazrou.boilerplate.repository.auth

import android.content.SharedPreferences
import android.util.Log
import com.mazrou.boilerplate.model.AccountProperties
import com.mazrou.boilerplate.model.AuthToken
import com.mazrou.boilerplate.network.WebService
import com.mazrou.boilerplate.network.network_response.LoginResponse
import com.mazrou.boilerplate.network.network_response.RegistrationResponse
import com.mazrou.boilerplate.perssistance.AccountPropertiesDao
import com.mazrou.boilerplate.perssistance.AuthTokenDao
import com.mazrou.boilerplate.repository.buildError
import com.mazrou.boilerplate.repository.safeApiCall
import com.mazrou.boilerplate.repository.safeCacheCall
import com.mazrou.boilerplate.session.SessionManager
import com.mazrou.boilerplate.ui.auth.state.AuthViewState
import com.mazrou.boilerplate.ui.auth.state.LoginFields
import com.mazrou.boilerplate.ui.auth.state.RegistrationFields
import com.mazrou.boilerplate.util.*
import com.mazrou.boilerplate.util.Constants.Companion.ERROR_SAVE_ACCOUNT_PROPERTIES
import com.mazrou.boilerplate.util.Constants.Companion.ERROR_SAVE_AUTH_TOKEN
import com.mazrou.boilerplate.util.Constants.Companion.GENERIC_AUTH_ERROR
import com.mazrou.boilerplate.util.Constants.Companion.INVALID_CREDENTIALS
import com.mazrou.boilerplate.util.Constants.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val webService: WebService,
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager,
    private val sharedPreferences: SharedPreferences,

) : AuthRepository {
    private val TAG: String = "AppDebug"

    override fun attemptLogin(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>> = flow {

        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if (loginFieldErrors == LoginFields.LoginError.none()) {
            val apiResult = safeApiCall(IO) {
                webService.login(email, password)
            }
            emit(
                object : ApiResponseHandler<AuthViewState, LoginResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: LoginResponse): DataState<AuthViewState> {

                        Log.d(TAG, "handleSuccess ")

                        // Incorrect login credentials counts as a 200 response from server, so need to handle that
                        if (resultObj.response.equals(GENERIC_AUTH_ERROR)) {
                            return DataState.error(
                                response = Response(
                                    INVALID_CREDENTIALS,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        accountPropertiesDao.insertOrIgnore(
                            AccountProperties(
                                pk = resultObj.pk,
                                username = resultObj.username,
                                email = ""
                            )
                        )

                        // will return -1 if failure
                        val authToken = AuthToken(
                            account_pk = resultObj.pk,
                            token = resultObj.token
                        )
                        val result = authTokenDao.insert(authToken)
                        if (result < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_AUTH_TOKEN,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        saveAuthenticatedUserToPrefs(email)

                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = null
                        )
                    }

                }.getResult()
            )
        } else {
            Log.d(TAG, "emitting error: $loginFieldErrors")
            emit(
                buildError<AuthViewState>(
                    loginFieldErrors,
                    UIComponentType.Dialog(),
                    stateEvent
                )
            )
        }
    }

    override fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>> = flow {
        val registrationFieldErrors =
            RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if (registrationFieldErrors == RegistrationFields.RegistrationError.none()) {

            val apiResult = safeApiCall(IO) {
                webService.register(
                    email,
                    username,
                    password,
                    confirmPassword
                )
            }
            emit(
                object : ApiResponseHandler<AuthViewState, RegistrationResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: RegistrationResponse): DataState<AuthViewState> {
                        if (resultObj.response.equals(GENERIC_AUTH_ERROR)) {
                            return DataState.error(
                                response = Response(
                                    resultObj.errorMessage,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        val result1 = accountPropertiesDao.insertAndReplace(
                            AccountProperties(
                                resultObj.pk,
                                resultObj.email,
                                resultObj.username
                            )
                        )
                        // will return -1 if failure
                        if (result1 < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_ACCOUNT_PROPERTIES,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        // will return -1 if failure
                        val authToken = AuthToken(
                            resultObj.pk,
                            resultObj.token
                        )
                        val result2 = authTokenDao.insert(authToken)
                        if (result2 < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_AUTH_TOKEN,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        saveAuthenticatedUserToPrefs(email)
                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = null
                        )
                    }
                }.getResult()
            )

        } else {
            emit(
                buildError<AuthViewState>(
                    registrationFieldErrors,
                    UIComponentType.Dialog(),
                    stateEvent
                )
            )
        }

    }


    override fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>> = flow {
        Log.d(TAG, "checkPreviousAuthUser: ")
        val previousAuthUserEmail: String? =
            sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)

        if (previousAuthUserEmail.isNullOrBlank()) {
            Log.d(TAG, "checkPreviousAuthUser: No previously authenticated user found.")
            emit(returnNoTokenFound(stateEvent))
        } else {
            val apiResult = safeCacheCall(IO) {
                accountPropertiesDao.searchByEmail(previousAuthUserEmail)
            }
            emit(
                object : CacheResponseHandler<AuthViewState, AccountProperties>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: AccountProperties): DataState<AuthViewState> {

                        if (resultObj.pk > -1) {
                            authTokenDao.searchByPk(resultObj.pk).let { authToken ->
                                if (authToken != null) {
                                    if (authToken.token != null) {
                                        return DataState.data(
                                            data = AuthViewState(
                                                authToken = authToken
                                            ),
                                            response = null,
                                            stateEvent = stateEvent
                                        )
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "createCacheRequestAndReturn: AuthToken not found...")
                        return DataState.error(
                            response = Response(
                                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                UIComponentType.None(),
                                MessageType.Error()
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.getResult()
            )
        }
    }

    override fun saveAuthenticatedUserToPrefs(email: String) {
        sharedPreferences.edit().putString(PreferenceKeys.PREVIOUS_AUTH_USER, email).apply()
    }

    override fun returnNoTokenFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState> {

        return DataState.error(
            response = Response(
                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                UIComponentType.None(),
                MessageType.Error()
            ),
            stateEvent = stateEvent
        )
    }

}