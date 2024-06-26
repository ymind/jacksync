package team.yi.jacksync.diff

import team.yi.jacksync.operation.PatchOperation

interface DiffMapper {
    fun <T> diff(source: T, target: T, invertible: Boolean = false): List<PatchOperation>
}
