package uz.texnopos.texnoposedufinance.core

open class RealtimeChangesResource<out T> constructor(
    val status: RealtimeChangesResourceState,
    val data: T?,
    val message: String?
) {

    companion object {
        fun <T> onAdded(data: T): RealtimeChangesResource<T> {
            return RealtimeChangesResource(RealtimeChangesResourceState.ADDED, data, null)
        }

        fun <T> onModified(data: T): RealtimeChangesResource<T> {
            return RealtimeChangesResource(RealtimeChangesResourceState.MODIFIED, data, null)
        }

        fun <T> onRemoved(data: T): RealtimeChangesResource<T> {
            return RealtimeChangesResource(RealtimeChangesResourceState.REMOVED, data, null)
        }

        fun <T> error(message: String?): RealtimeChangesResource<T> {
            return RealtimeChangesResource(RealtimeChangesResourceState.ERROR, null, message)
        }

        fun <T> loading(): RealtimeChangesResource<T> {
            return RealtimeChangesResource(RealtimeChangesResourceState.LOADING, null, null)
        }
    }
}

enum class RealtimeChangesResourceState {
    LOADING, ADDED, REMOVED, MODIFIED, ERROR
}