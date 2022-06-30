package com.spotlight.spotlightapp.utilities.viewutils

sealed class RecyclerViewType<R>(val viewTypeInt: Int) {
    companion object {
        const val HEADER_VIEW_TYPE = 0
        const val ITEM_VIEW_TYPE = 1
        const val FOOTER_VIEW_TYPE = 2
    }

    class Header<T> : RecyclerViewType<T>(HEADER_VIEW_TYPE)
    class Item<T>(val content: T) : RecyclerViewType<T>(ITEM_VIEW_TYPE)
    class Footer<T> : RecyclerViewType<T>(FOOTER_VIEW_TYPE)

    fun isEqual(other: RecyclerViewType<R>, equalityChecker: (R, R?) -> Boolean): Boolean =
        (viewTypeInt == other.viewTypeInt) && if (this is Item<R> && other is Item<R>) equalityChecker(
            content, other.content) else true
}
