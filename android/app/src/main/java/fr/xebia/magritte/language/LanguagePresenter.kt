package fr.xebia.magritte.language

import fr.xebia.magritte.data.MagritteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LanguagePresenter(val view: LanguageContract.View, private val repository: MagritteRepository) : LanguageContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
        // TODO
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun getData() {
        compositeDisposable.add(repository.getData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { view.displayResult(it) },
                        { view.displayError(it.localizedMessage) }
                )
        )
    }
}