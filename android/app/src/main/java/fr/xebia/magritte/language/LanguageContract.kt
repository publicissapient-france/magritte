package fr.xebia.magritte.language

import fr.xebia.magritte.BasePresenter
import fr.xebia.magritte.BaseView
import fr.xebia.magritte.model.MagritteData

interface LanguageContract {

    interface View : BaseView<Presenter> {

        fun displayResult(data: MagritteData)

        fun displayError(message: String)
    }

    interface Presenter : BasePresenter {
        fun getData()
    }
}