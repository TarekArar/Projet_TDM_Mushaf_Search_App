package com.mazrou.boilerplate.ui.auth


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mazrou.boilerplate.R
import com.mazrou.boilerplate.di.activityScopedFragmentViewModel
import com.mazrou.boilerplate.ui.BaseActivity
import com.mazrou.boilerplate.ui.auth.state.AuthStateEvent
import com.mazrou.boilerplate.ui.auth.state.LoginFields
import com.mazrou.boilerplate.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


@ExperimentalCoroutinesApi
@FlowPreview
class LoginFragment : BaseAuthFragment(R.layout.fragment_login) , KodeinAware {

    override val kodein: Kodein by  closestKodein()

    val viewModel: AuthViewModel  by instance( )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        login_button.setOnClickListener {
            login()
        }

    }

    fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer{
            it.loginFields?.let{
                it.login_email?.let{input_username.setText(it)}
                it.login_password?.let{input_password.setText(it)}
            }
        })
    }

    private fun login(){
        saveLoginFields()
        (requireActivity() as BaseActivity).hideSoftKeyboard()
        viewModel.setStateEvent(
            AuthStateEvent.LoginAttemptEvent(
                input_username.text.toString(),
                input_password.text.toString()
            )
        )
    }

    private fun saveLoginFields(){
        viewModel.setLoginFields(
            LoginFields(
                input_username.text.toString(),
                input_password.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveLoginFields()
    }




}