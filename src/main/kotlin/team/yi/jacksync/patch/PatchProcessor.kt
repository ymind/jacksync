package team.yi.jacksync.patch

import team.yi.jacksync.operation.PatchOperation

interface PatchProcessor {
    fun <T : Any> patch(sourceObject: T, jsonOperations: String): T

    fun <T : Any> patch(sourceObject: T, operations: List<PatchOperation>): T
}
