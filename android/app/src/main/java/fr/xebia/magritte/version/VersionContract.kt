package fr.xebia.magritte.version

import fr.xebia.magritte.BasePresenter
import fr.xebia.magritte.BaseView
import fr.xebia.magritte.model.MagritteModel
import fr.xebia.magritte.model.MagritteVersion

interface VersionContract {

    interface View : BaseView<Presenter> {

        fun displayResult(models: List<MagritteModel>)

        fun displayError(message: String)
    }

    interface Presenter : BasePresenter {

        fun getData()
    }
}