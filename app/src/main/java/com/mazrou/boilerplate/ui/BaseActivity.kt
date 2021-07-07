package com.mazrou.boilerplate.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.callbacks.onDismiss

import com.mazrou.boilerplate.R

import com.mazrou.boilerplate.util.MessageType
import com.mazrou.boilerplate.util.Response
import com.mazrou.boilerplate.util.StateMessageCallback
import com.mazrou.boilerplate.util.UIComponentType

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


import org.kodein.di.generic.instance

abstract class BaseActivity : AppCompatActivity(), KodeinAware, UICommunicationListener {


    override val kodein: Kodein by closestKodein()


    val TAG: String = "AppDebug"
    private var dialogInView: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        hideSoftKeyboard()
    }

    override fun onResponseReceived(
        response: Response<Any?>?,
        stateMessageCallback: StateMessageCallback
    ) {

        response?.let {
            when (response.uiComponentType) {

                is UIComponentType.AreYouSureDialog -> {

                    response.message?.let {
                        areYouSureDialog(
                            message = it,
                            callback = response.uiComponentType.callback,
                            stateMessageCallback = stateMessageCallback ,
                        )
                    }
                }


                is UIComponentType.Toast -> {
                    response.message?.let {
                        displayToast(
                            message = it,
                            stateMessageCallback = stateMessageCallback
                        )
                    }
                }

                is UIComponentType.Dialog -> {
                    displayDialog(
                        response = response,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is UIComponentType.None -> {
                    // This would be a good place to send to your Error Reporting
                    // software of choice (ex: Firebase crash reporting)
                    Log.i(TAG, "onResponseReceived: ${response.message}")
                    stateMessageCallback.removeMessageFromStack()
                }

            }
        }
    }

    private fun displayDialog(
        response: Response<Any?>,
        stateMessageCallback: StateMessageCallback
    ) {
        response.message?.let { message ->
            dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    displayErrorDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Success -> {
                    displaySuccessDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Info -> {
                    displayInfoDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        } ?: stateMessageCallback.removeMessageFromStack()
    }


    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    private fun areYouSureDialog(
        message: String,
        callback: AreYouSureCallback? = null,
        stateMessageCallback: StateMessageCallback? = null,
        title: String = getString(R.string.are_you_sure)
    ): Dialog {
        return MaterialDialog(this)
            .show {
                title(text = title)
                cornerRadius(2f)
                message(text = message)
                negativeButton(R.string.text_no) {
                    callback?.cancel()
                    stateMessageCallback?.removeMessageFromStack()
                    dismiss()
                }
                positiveButton(R.string.text_yes) {
                    callback?.proceed()
                    stateMessageCallback?.removeMessageFromStack()
                    dismiss()
                }.apply {
                    getActionButton(WhichButton.NEGATIVE).updateTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_color
                        )
                    )
                    getActionButton(WhichButton.POSITIVE).updateTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_color
                        )
                    )
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    override fun onPause() {
        super.onPause()
        if (dialogInView != null) {
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }

    private fun displaySuccessDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_success)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayErrorDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_error)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayInfoDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_info)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }
}