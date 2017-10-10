package fr.xebia.magritte.home

import android.content.Context
import fr.xebia.magritte.data.MagritteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainPresenter(val view: MainContract.View,
                    private val repository: MagritteRepository) : MainContract.Presenter {

    private val TAG: String = MainPresenter::class.java.simpleName
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadInitData() {
        if (repository.getModelFilePath() == null) {
            view.displayLoading()
            compositeDisposable.add(
                    repository.getModelFile()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { view.displayLoadedWithSuccess(it.absolutePath) },
                                    { view.displayLoadingError() }
                            )
            )
        } else {
            view.displayLoadedWithSuccess(repository.getModelFilePath())
        }
    }
}