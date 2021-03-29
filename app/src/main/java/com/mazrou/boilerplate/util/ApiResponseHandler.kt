package com.mazrou.boilerplate.util

import com.mazrou.boilerplate.util.Constants.Companion.NETWORK_ERROR


abstract class ApiResponseHandler <ViewState, Data >(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent
){

    suspend fun getResult(): DataState<ViewState> {

        return when(response){
            is ApiResult.GenericError -> {
                handleError(response)
            }

            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response<Any?>(
                        message = NETWORK_ERROR,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent}\n\nReason: Data is NULL.",
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }


    open fun handleError(response: ApiResult.GenericError): DataState<ViewState> {
        return DataState.error(
            response = Response(
                message = response.errorMessage.toString(),
                uiComponentType = UIComponentType.Dialog(),
                messageType = MessageType.Error()
            ),
            stateEvent = stateEvent
        )
    }
    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

}