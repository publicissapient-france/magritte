package fr.xebia.magritte

import kotlin.reflect.KClass

object Dependency {

    private val dependencies: MutableMap<KClass<out Any>, Any> = mutableMapOf()

    fun <D : Any> get(clazz: Class<D>): D {
        return try {
            clazz.cast(dependencies[clazz.kotlin])
        } catch (e: IllegalStateException) {
            throw DependencyNotInjectedException(clazz.simpleName)
        }
    }

    fun <D : Any> set(clazz: Class<D>, dependency: D) {
        dependencies[clazz.kotlin] = dependency
    }
}

class DependencyNotInjectedException(errorMessage: String?) : Exception(errorMessage)