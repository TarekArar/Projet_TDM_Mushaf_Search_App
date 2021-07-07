package com.mazrou.boilerplate.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.mazrou.boilerplate.R
import com.mazrou.boilerplate.ui.BaseActivity
import com.mazrou.boilerplate.ui.main.state.MainStateEvent
import com.mazrou.boilerplate.util.StateEvent
import com.mazrou.boilerplate.util.StateMessageCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


const val GET_ALL_AYAT_STATE_EVENT_NAME = "get all ayat from network"
const val GET_ALL_SURAH_STATE_EVENT_NAME = "get all surah from network"
const val GET_ALL_WORLD_STATE_EVENT_NAME = "get all world from network"
const val GET_ALL_RACINE_STATE_EVENT_NAME = "get all racine from network"
const val FIRST_RUN = "first run of the application"
@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() , KodeinAware {

    override val kodein: Kodein by  closestKodein()

    val viewModel: MainViewModel  by instance()
    lateinit var sharedPref: SharedPreferences

    /**
     * this contains all state events on this activity
     *      if true the stateEvent is running
     *      if false the stateEvent is done
     *      if null the stateEvent is never run before
     * */
    private val _stateEvents = HashMap<String,StateEvent>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getPreferences(Context.MODE_PRIVATE)



        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        sharedPref.getBoolean(FIRST_RUN , false).let {firstRun ->
            if (!firstRun){
                getAllSurah()
                sharedPref.edit().putBoolean(FIRST_RUN , true ).apply()
            }
        }
    }
    override fun displayProgressBar(isLoading: Boolean, useDialog: Boolean) {
        if(isLoading){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.GONE
        }
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(this, Observer{ viewState ->

        })

        viewModel.numActiveJobs.observe(this, Observer { jobCounter ->

            Log.e(TAG , "number of job active : $jobCounter")
            displayProgressBar(viewModel.areAnyJobsActive())
             _stateEvents[GET_ALL_SURAH_STATE_EVENT_NAME]?.let {
                 if (!viewModel.isJobAlreadyActive(it)
                 ) {
                     getAllAyat()
                 }
             }
            _stateEvents[GET_ALL_AYAT_STATE_EVENT_NAME]?.let {
                if (!viewModel.isJobAlreadyActive(it)
            //   && _stateEvents[GET_ALL_AYAT_STATE_EVENT_NAME] != null
                ){
                    getAllRacine()
                }
            }
            _stateEvents[GET_ALL_RACINE_STATE_EVENT_NAME]?.let {
                if (!viewModel.isJobAlreadyActive(it)
                //    && _stateEvents[GET_ALL_AYAT_STATE_EVENT_NAME] != null
                ){
                    getAllWorld()
                }
            }
        })

        viewModel.stateMessage.observe(this, Observer { stateMessage ->
            stateMessage?.let {
                onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }


      fun getAllAyat(){
        _stateEvents[GET_ALL_AYAT_STATE_EVENT_NAME]?.let {
            if (viewModel.isJobAlreadyActive(it)){
                return
            }
        }?: run{
            _stateEvents[GET_ALL_AYAT_STATE_EVENT_NAME] = MainStateEvent.GetAyatFromNetwork()
            viewModel.setStateEvent( _stateEvents[GET_ALL_AYAT_STATE_EVENT_NAME]!!)
        }


    }

      fun getAllSurah(){
        _stateEvents[GET_ALL_SURAH_STATE_EVENT_NAME]?.let {
            if (viewModel.isJobAlreadyActive(it)){
                return
            }
        }?: run{
            _stateEvents[GET_ALL_SURAH_STATE_EVENT_NAME] = MainStateEvent.GetSurahFromNetwork()
            viewModel.setStateEvent( _stateEvents[GET_ALL_SURAH_STATE_EVENT_NAME]!!)
        }

    }

      fun getAllWorld(){
        _stateEvents[GET_ALL_WORLD_STATE_EVENT_NAME]?.let {
            if (viewModel.isJobAlreadyActive(it)){
                return
            }
        }?: run{
            _stateEvents[GET_ALL_WORLD_STATE_EVENT_NAME] = MainStateEvent.GetWorldFromNetwork()
            viewModel.setStateEvent( _stateEvents[GET_ALL_WORLD_STATE_EVENT_NAME]!!)
        }


    } fun getAllRacine(){
        _stateEvents[GET_ALL_RACINE_STATE_EVENT_NAME]?.let {
            if (viewModel.isJobAlreadyActive(it)){
                return
            }
        }?: run{
            _stateEvents[GET_ALL_RACINE_STATE_EVENT_NAME] = MainStateEvent.GetRacineFromNetwork()
            viewModel.setStateEvent( _stateEvents[GET_ALL_RACINE_STATE_EVENT_NAME]!!)
        }

    }
}