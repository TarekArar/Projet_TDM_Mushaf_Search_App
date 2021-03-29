package com.mazrou.boilerplate.ui

import com.mazrou.boilerplate.util.Response
import com.mazrou.boilerplate.util.StateMessageCallback

/**
 * @author Mazrou Ayoub
 */
interface UICommunicationListener {
    fun onResponseReceived(
        response: Response<Any?>?,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean, useDialog: Boolean = false)


    fun hideSoftKeyboard()


}