package team.yi.jacksync.diff

import team.yi.jacksync.exception.DiffProcessingException
import team.yi.jacksync.operation.PatchOperation

interface DiffMapper {
    @Throws(DiffProcessingException::class)
    fun <T> diff(source: T, target: T): List<PatchOperation>
}
