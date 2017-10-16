package fr.xebia.magritte.home

import fr.xebia.magritte.CATEGORY_FRUIT
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
        if (!repository.getInitDataLoadingStatus()) {
            view.displayLoading()
            compositeDisposable.add(
                    repository.getModelFile()
                            .flatMap { repository.saveModelFileToDisk(it) }
                            .flatMap { repository.getModelLabel(CATEGORY_FRUIT) }
                            .flatMap { repository.insertLabels(CATEGORY_FRUIT, it) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        repository.setInitDataLoadingStatus(true)
                                        view.displayLoadedWithSuccess()
                                    },
                                    {
                                        repository.setInitDataLoadingStatus(false)
                                        view.displayLoadingError()
                                    }
                            )
            )
        } else {
            view.displayLoadedWithSuccess()
        }
    }
}