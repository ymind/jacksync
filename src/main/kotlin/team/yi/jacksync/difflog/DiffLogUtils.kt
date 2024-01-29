package team.yi.jacksync.difflog

import team.yi.jacksync.exception.DiffLogException
import team.yi.jacksync.operation.*

object DiffLogUtils {
    fun toDiffLogs(vararg patchOperations: PatchOperation): List<DiffLog> {
        return toDiffLogs(patchOperations.toList())
    }

    fun toDiffLogs(patchOperations: List<PatchOperation>): List<DiffLog> {
        val patchPathOperations = patchOperations.filter {
            it is PatchPathValueOperation || it is RemoveOperation
        }.map { it as PatchPathOperation }

        if (patchPathOperations.size != patchOperations.size) {
            val message = buildString {
                append("Only support ")
                append("`${AddOperation.OP_NAME}`, ")
                append("`${ReplaceOperation.OP_NAME}`, ")
                append("`${RemoveOperation.OP_NAME}`, ")
                append("and `${TestOperation.OP_NAME}` operations.")
            }

            throw DiffLogException(message)
        }

        if (patchPathOperations.size % 2 == 1) {
            throw DiffLogException("The size of `patchOperations` must be an even number.")
        }

        val testOperations = patchPathOperations.filterIsInstance<TestOperation>()

        if (testOperations.size * 2 != patchPathOperations.size) {
            throw DiffLogException("The number of test operations must be half the size of the operations list.")
        }

        return patchPathOperations.filter { it !is TestOperation }.let { operations ->
            testOperations.map { testOperation ->
                val path = testOperation.path
                val operation = requireNotNull(operations.firstOrNull { it.path == path }) {
                    val message = "No `PatchOperation` were found matching the `TestOperation(path=$path)`."

                    throw DiffLogException(message)
                }

                val op = requireNotNull(PatchOperation.getOpName(operation))
                val value = if (operation is PatchPathValueOperation) operation.value else null

                DiffLog(op, testOperation.path, testOperation.value, value)
            }
        }
    }
}
