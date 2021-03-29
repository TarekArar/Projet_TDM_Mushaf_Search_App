package com.mazrou.boilerplate.ui.main

import android.os.Bundle
import com.mazrou.boilerplate.R
import com.mazrou.boilerplate.ui.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun displayProgressBar(isLoading: Boolean, useDialog: Boolean) {

    }
}