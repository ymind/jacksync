package team.yi.jacksync.patch

import team.yi.jacksync.exception.PatchProcessingException
import team.yi.jacksync.operation.PatchOperation

interface PatchProcessor {
    @Throws(PatchProcessingException::class)
    fun <T : Any> patch(sourceObject: T, jsonOperations: String): T

    @Throws(PatchProcessingException::class)
    fun <T : Any> patch(sourceObject: T, operations: List<PatchOperation>): T
}
