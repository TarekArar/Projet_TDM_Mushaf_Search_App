package com.mazrou.boilerplate.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer

import com.mazrou.boilerplate.R

import com.mazrou.boilerplate.ui.BaseActivity
import com.mazrou.boilerplate.ui.auth.state.AuthStateEvent
import com.mazrou.boilerplate.ui.main.MainActivity
import com.mazrou.boilerplate.util.Constants.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import com.mazrou.boilerplate.util.StateMessageCallback
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


@FlowPreview
@ExperimentalCoroutinesApi
class AuthActivity : BaseActivity() , KodeinAware {

    override val kodein: Kodein by  closestKodein()

    val viewModel: AuthViewModel  by instance( )



    //  private val viewModel: AuthViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        subscribeObservers()
    }


    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
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
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthViewState: $viewState")
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        viewModel.numActiveJobs.observe(this, Observer { jobCounter ->
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(this, Observer { stateMessage ->

            stateMessage?.let {

                if(stateMessage.response.message.equals(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE)){
                    onFinishCheckPreviousAuthUser()
                }

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

        sessionManager.cachedToken.observe(this, Observer{ token ->
            token.let{ authToken ->
                if(authToken != null && authToken.account_pk != -1 && authToken.token != null){
                    navMainActivity()
                }
            }
        })
    }

    private fun onFinishCheckPreviousAuthUser(){
        fragment_container.visibility = View.VISIBLE

    }

    fun navMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkPreviousAuthUser(){
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent())
    }




}