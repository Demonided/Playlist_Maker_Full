package com.katoklizm.playlist_maker_full.ui.common

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity<STATE, VIEW_MODEL: BaseViewModel<STATE>> : AppCompatActivity() {

    val viewModel: VIEW_MODEL by viewModel()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        viewModel.state.observe(this) {
            renderState(it)
        }
    }

    abstract fun renderState(state: STATE)
}