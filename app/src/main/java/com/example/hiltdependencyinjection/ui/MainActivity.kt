package com.example.hiltdependencyinjection.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.hiltdependencyinjection.R
import com.example.hiltdependencyinjection.model.Blog
import com.example.hiltdependencyinjection.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val TAG = "AppDebug"

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer {
            when (it) {
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(it.exception.message)
                }
                DataState.Loading -> {
                    displayProgressBar(true)
                }
                is DataState.Success -> {
                    displayProgressBar(false)
                    appendBlogTitles(it.data)
                }
            }
        })
    }

    private fun displayError(message: String?) {
        if(message != null){
            findViewById<TextView>(R.id.text).text = message
        }
        else{
            findViewById<TextView>(R.id.text).text = "Unknown Error"
        }
    }

    private fun displayProgressBar(isDisplayed : Boolean){
        findViewById<ProgressBar>(R.id.progress_bar).isVisible = isDisplayed
    }

    private fun appendBlogTitles(blogs:  List<Blog>){
        val stringBuilder = StringBuilder()
        for(blog in blogs){
            stringBuilder.append(blog.title + "\n")
        }
        findViewById<TextView>(R.id.text).text = stringBuilder.toString()
    }
}