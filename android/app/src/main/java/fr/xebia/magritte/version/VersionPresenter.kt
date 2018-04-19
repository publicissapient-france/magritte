package fr.xebia.magritte.version

import fr.xebia.magritte.data.MagritteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class VersionPresenter(val view: VersionContract.View,
                       private val repository: MagritteRepository)
    : VersionContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
        // TODO
    }

    override fun unsubscribe() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    override fun getData() {
        // TODO cache json in case of offline
        compositeDisposable.add(repository.getVersion()
                .flatMap { repository.getModels() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { view.displayResult(it) },
                        { view.displayError(it.localizedMessage) }
                )
        )
    }
}