package com.katoklizm.playlist_maker_full.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.ActivityMediaLibraryBinding
import com.katoklizm.playlist_maker_full.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBack.setOnClickListener {
            finish()
        }

        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.setText("")
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

        binding.searchEditText.addTextChangedListener(simpleTextWatcher)


    }

    companion object {
        const val USER_TEXT = "USER_TEXT"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_TEXT, binding.searchEditText.toString())
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        if (savedInstanceState != null) {
//            binding.searchEditText.textAlignment = savedInstanceState.getInt(USER_TEXT,0)
//        }
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        binding.searchEditText.textAlignment = savedInstanceState.getInt(USER_TEXT,0)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}

