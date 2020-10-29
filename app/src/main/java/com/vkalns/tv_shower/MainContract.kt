package com.vkalns.tv_shower

import com.vkalns.tv_shower.model.Show


interface MainContract   {

    interface View {

        fun createPresenter(): Presenter<View>
        fun displayShowTitle(title: String)
        fun displayDaysSincePremiere(subtitle: String)
        fun displayShowPoster(posterURL: String)
        fun displayErrorToast(errorMessage: String)
        fun isNetworkConnected(): Boolean
    }

    interface Presenter <in T : View> {
        fun onAttach(view: T)
        fun onDetach()
        fun searchForShow(name :String)
        fun displayShowInfo(show: Show)
        fun getDaysCountToDisplay(releaseDateString: String): String

    }
}