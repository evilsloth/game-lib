package io.github.evilsloth.gamelib.common.model

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val exception: Throwable?
) {
    enum class Status {
        READY,
        LOADING,
        ERROR
    }

    companion object {
        fun <T> ready(data: T?): Resource<T> {
            return Resource(Status.READY, data, null)
        }

        fun <T> error(exception: Throwable? = null): Resource<T> {
            return Resource(Status.ERROR, null, exception)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }
}