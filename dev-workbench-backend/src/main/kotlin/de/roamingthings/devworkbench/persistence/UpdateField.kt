package de.roamingthings.devworkbench.persistence

data class UpdateField<T>(val value: T? = null, val ignored: Boolean = false) {

    fun get(): T = value ?: throw IllegalStateException("value cannot be null.")
    fun getOrDefault(defaultValue: T): T = if (ignored) defaultValue else get()
    fun getOrNull(): T? = value
    fun getOrNullOrDefault(defaultValue: T?): T? = if (ignored) defaultValue else value

    companion object {
        fun <T> ignore() = UpdateField<T>(ignored = true)
        fun <T> setNull() = UpdateField<T>()
        fun <T> of(value: T) = UpdateField<T>(value = value)
    }

}