package com.vkalns.tv_shower

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.vkalns.tv_shower.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity(), MainContract.View{

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: MainContract.Presenter<MainContract.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.presenter = createPresenter(cacheDir)
        presenter.onAttach(this)
        binding.searchButton.setOnClickListener {
            if (binding.searchBar.text.isNotBlank()){
                presenter.searchForShow(binding.searchBar.text.toString())
                dismissKeyboard(this)
                binding.searchBar.setText("")
            } else{
                displayErrorToast("Please enter something in the search field")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun displayShowPoster(showImageUrl: String) {
        Picasso.get().isLoggingEnabled = true
        Picasso.get()
            .load(showImageUrl)
            .resize(binding.showImage.width, 0)
            .into(binding.showImage)
    }

    override fun displayShowTitle(title: String) {
        binding.showTitle.apply {
            text = title
            visibility = View.VISIBLE
        }
    }

    override fun displayDaysSincePremiere(subtitleText: String) {
        binding.showPremiereDays.apply {
            text = subtitleText
            visibility = View.VISIBLE
        }
    }

    override fun displayErrorToast(errorMessage: String) {
        Toast.makeText(
            this,
            errorMessage,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun dismissKeyboard(activity: Activity) {
        val inputMethodManager : InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity.currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.applicationWindowToken, 0)
        }
    }

    override fun isNetworkConnected(): Boolean {
        //1
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //2
        val activeNetwork = connectivityManager.activeNetwork
        //3
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        //4
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun createPresenter(cacheDir: File): MainContract.Presenter<MainContract.View> = MainPresenter(cacheDir)
}