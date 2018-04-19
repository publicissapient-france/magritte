package fr.xebia.magritte.level

import fr.xebia.magritte.data.MagritteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LevelPresenter(val view: LevelContract.View, private val repository: MagritteRepository) : LevelContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadData(modelId: String, category: String) {
        val filePath = repository.getModelFilePath(modelId)
        compositeDisposable.add(
                repository.readLabels(category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { view.displayClassifier(filePath, it.map { it.value }) }
                        )
        )
    }
}