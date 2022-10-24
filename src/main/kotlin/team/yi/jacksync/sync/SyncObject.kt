package team.yi.jacksync.sync

data class SyncObject<T>(
    val version: Long,
    val data: T,
)
