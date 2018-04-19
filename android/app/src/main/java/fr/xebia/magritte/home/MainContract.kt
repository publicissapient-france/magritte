package fr.xebia.magritte.home

import fr.xebia.magritte.BasePresenter
import fr.xebia.magritte.BaseView
import fr.xebia.magritte.model.MagritteModel

interface MainContract {

    interface View : BaseView<Presenter> {

        fun displayLoading()

        fun displayLoadedWithSuccess()

        fun displayLoadingError()
    }

    interface Presenter : BasePresenter {

        fun loadInitData(model: MagritteModel)
    }
}