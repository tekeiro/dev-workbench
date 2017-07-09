package de.roamingthings.devworkbench.persistence

class UpdateNullableField<T>(val value: T? = null, val ignored: Boolean = false) {

    fun get(): T = value ?: throw IllegalStateException("value cannot be null.")
    fun getOrDefault(defaultValue: T): T = if (ignored) defaultValue else get()
    fun getOrNull(): T? = value
    fun getOrNullOrDefault(defaultValue: T?): T? = if (ignored) defaultValue else value

    companion object {
        fun <T> ignore() = UpdateNullableField<T>(ignored = true)
        fun <T> of(value: T) = UpdateNullableField<T>(value = value)
        fun <T> setNull() = UpdateNullableField<T>()
    }

}