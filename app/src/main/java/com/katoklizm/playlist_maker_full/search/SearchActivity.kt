package com.katoklizm.playlist_maker_full.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.katoklizm.playlist_maker_full.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var enteredText: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBack.setOnClickListener {
            finish()
        }

        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.text.clear()
            hideSoftKeyboard()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchClearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        if (savedInstanceState != null) {
            enteredText = savedInstanceState.getString(USER_TEXT)
            binding.searchEditText.setText(enteredText)
        }

        binding.searchEditText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        enteredText = binding.searchEditText.text.toString()
        outState.putString(USER_TEXT, enteredText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        enteredText = savedInstanceState.getString(USER_TEXT)
        binding.searchEditText.setText(enteredText)
    }

    private fun hideSoftKeyboard() {
        val hide = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    private companion object {
        const val USER_TEXT = "USER_TEXT"
    }
}

