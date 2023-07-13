package team.yi.jacksync.sync

interface SyncProcessor {
    var isChecksumValidationEnabled: Boolean

    fun <T : Any> clientSync(sourceObject: SyncObject<T>, syncData: SyncData): SyncObject<T>

    fun <T : Any> masterSync(sourceObject: SyncObject<T>, syncData: SyncData): SyncObject<T>
}
