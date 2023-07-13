package team.yi.jacksync.diff

import team.yi.jacksync.sync.*

interface SyncDiffMapper {
    fun <T> diff(source: SyncObject<T>, target: SyncObject<T>, invertible: Boolean = false): SyncData
}
