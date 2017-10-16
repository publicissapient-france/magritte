package fr.xebia.magritte.level

interface LevelContract {

    interface View {

        fun displayClassifier(filePath: String?, labels: List<String>)

        fun displayError()
    }

    interface Presenter {

        fun loadData(category: String)
    }
}