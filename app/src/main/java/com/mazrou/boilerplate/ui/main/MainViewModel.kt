package com.mazrou.boilerplate.ui.main


import com.mazrou.boilerplate.model.database.RacineModel
import com.mazrou.boilerplate.model.ui.Racine
import com.mazrou.boilerplate.repository.main.Repository
import com.mazrou.boilerplate.ui.BaseViewModel
import com.mazrou.boilerplate.ui.main.state.MainStateEvent.*
import com.mazrou.boilerplate.ui.main.state.MainViewState
import com.mazrou.boilerplate.util.*
import com.mazrou.boilerplate.util.Constants.Companion.INVALID_STATE_EVENT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel(
   private val repository: Repository
)  : BaseViewModel<MainViewState>() {

    override fun handleNewData(data: MainViewState) {
        data.let {
            it.racineModelList?.let {
                setRacine(it)
            }
        }
    }

    private fun setRacine(newData : List<Racine>){
        val update = getCurrentViewStateOrNew()
        if (update.racineModelList == newData){
            return
        }
        update.racineModelList = newData
        setViewState(update)
    }
    fun clearRacineSearch(){
        setRacine(emptyList())
    }
    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<MainViewState>> = when(stateEvent){
            is GetAyatFromNetwork -> {
                repository.getAllAyat(stateEvent = stateEvent)
            }
            is GetSurahFromNetwork -> {
                repository.getAllSurah(stateEvent = stateEvent)
            }
            is GetWorldFromNetwork -> {
                repository.getAllWorlds(stateEvent =stateEvent)
            }
            is GetRacineFromNetwork -> {
                repository.getAllRacine(stateEvent =stateEvent)
            }
            is SearchByRacine ->{
                repository.searchByRacine(
                    stateEvent = stateEvent ,
                    query = stateEvent.query
                )
            }

            else -> {
                flow{
                    emit(
                        DataState.error<MainViewState>(
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
    override fun initNewViewState(): MainViewState {
        return MainViewState()
    }
    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}