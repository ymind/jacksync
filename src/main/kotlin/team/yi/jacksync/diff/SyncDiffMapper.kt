package team.yi.jacksync.diff

import team.yi.jacksync.exception.DiffProcessingException
import team.yi.jacksync.sync.*

interface SyncDiffMapper {
    @Throws(DiffProcessingException::class)
    fun <T> diff(source: SyncObject<T>, target: SyncObject<T>): SyncData
}
