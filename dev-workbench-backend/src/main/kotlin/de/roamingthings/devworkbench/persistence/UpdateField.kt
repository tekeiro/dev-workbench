package de.roamingthings.devworkbench.persistence

open class UpdateField<T>(val value: T? = null, val ignored: Boolean = false) {

    fun get(): T = value ?: throw IllegalStateException("value cannot be null.")
    fun getOrDefault(defaultValue: T): T = if (ignored) defaultValue else get()

    companion object {
        fun <T> ignore() = UpdateField<T>(ignored = true)
        fun <T> of(value: T) = UpdateField<T>(value = value)
    }

}