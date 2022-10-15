package com.freespinslink.user.network.base

open class OneShotEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun isHandled(): Boolean{
        return hasBeenHandled
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}