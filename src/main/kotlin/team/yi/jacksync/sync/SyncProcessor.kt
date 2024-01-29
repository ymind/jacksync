package team.yi.jacksync.sync

interface SyncProcessor {
    val checksumValidationEnabled: Boolean

    fun <T : Any> clientSync(sourceObject: SyncObject<T>, syncData: SyncData): SyncObject<T>

    fun <T : Any> masterSync(sourceObject: SyncObject<T>, syncData: SyncData): SyncObject<T>
}
