package team.yi.jacksync.sync

import team.yi.jacksync.exception.SyncException

interface SyncProcessor {
    var isChecksumValidationEnabled: Boolean

    @Throws(SyncException::class)
    fun <T : Any> clientSync(sourceObject: SyncObject<T>, syncData: SyncData): SyncObject<T>

    @Throws(SyncException::class)
    fun <T : Any> masterSync(sourceObject: SyncObject<T>, syncData: SyncData): SyncObject<T>
}
