package fr.xebia.magritte.home

import fr.xebia.magritte.BasePresenter
import fr.xebia.magritte.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {

        fun displayLoading()

        fun displayLoadedWithSuccess(filePath: String?)

        fun displayLoadingError()
    }

    interface Presenter : BasePresenter {

        fun loadInitData()
    }
}