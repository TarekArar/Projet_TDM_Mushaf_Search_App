package com.mazrou.boilerplate.util

abstract class CacheResponseHandler <ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent ?
){
    suspend fun getResult(): DataState<ViewState> {

        return when(response){

            is CacheResult.GenericError -> {
                handleError(response)
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    handleEmptyResponse(response)
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

    open suspend fun handleError(response: CacheResult.GenericError): DataState<ViewState> {
        return DataState.error(
            response = Response<Any?>(
                message = "${stateEvent}\n\nReason: ${response.errorMessage.toString()}",
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.Error()
            ),
            stateEvent = stateEvent
        )
    }
    open suspend fun handleEmptyResponse(response: CacheResult.Success<Data?>): DataState<ViewState> {
        return   DataState.error(
            response = Response(
                message = "${stateEvent}\n\nReason: Data is NULL.",
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.Error()
            ),
            stateEvent = stateEvent
        )
    }

}