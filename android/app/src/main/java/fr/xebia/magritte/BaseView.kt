package fr.xebia.magritte

interface BaseView<T> {

    fun setPresenter(presenter: T)
}