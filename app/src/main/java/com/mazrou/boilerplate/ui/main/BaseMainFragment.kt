package com.mazrou.boilerplate.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

import com.mazrou.boilerplate.ui.UICommunicationListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


/**
 * @author Mazrou Ayoub
 */

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseMainFragment(
    @LayoutRes
    private val layout: Int
) : Fragment(layout) , KodeinAware {

    override val kodein: Kodein by  closestKodein()
    val viewModel: MainViewModel  by instance()

    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      // setupChannel()
    }

   // private fun setupChannel() = viewModel.setupChannel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {

        }

    }

}