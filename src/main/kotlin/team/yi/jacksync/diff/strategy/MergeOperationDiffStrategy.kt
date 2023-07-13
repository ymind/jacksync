package team.yi.jacksync.diff.strategy

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import team.yi.jacksync.operation.*
import team.yi.jacksync.utils.JacksonUtils

class MergeOperationDiffStrategy : DiffStrategy {
    var diffStrategy: DiffStrategy = SimpleDiffStrategy()

    override fun diff(sourceJsonNode: JsonNode, targetJsonNode: JsonNode): List<PatchOperation> {
        val operations = diffStrategy.diff(sourceJsonNode, targetJsonNode)

        return optimize(targetJsonNode, operations)
    }

    private fun optimize(targetJsonNode: JsonNode, operations: List<PatchOperation>): List<PatchOperation> {
        val optimizedOperations = mutableListOf<PatchOperation>()
        val parentToJsonPointerDataMap = mutableMapOf<JsonPointer, JsonPointerData>()

        for (operation in operations) {
            val pathJsonNode = JacksonUtils.locateHeadContainer(targetJsonNode, operation.path)

            if (pathJsonNode.isObject) {
                if (operation.javaClass == RemoveOperation::class.java) {
                    optimizedOperations.add(operation)
                } else {
                    val parentPointer = operation.path.head()
                    val jsonPointerData = parentToJsonPointerDataMap[parentPointer] ?: JsonPointerData().also {
                        parentToJsonPointerDataMap[parentPointer] = it
                    }

                    jsonPointerData.operations.add(operation)
                    jsonPointerData.fieldNames.add(JacksonUtils.lastFieldName(operation.path))
                }
            } else if (pathJsonNode.isArray) {
                optimizedOperations.add(operation)
            }
        }

        // merge process must start handling arrays
        optimizedOperations.addAll(optimize(targetJsonNode, parentToJsonPointerDataMap))

        return optimizedOperations
    }

    private fun optimize(targetJsonNode: JsonNode, parentToJsonPointerDataMap: Map<JsonPointer, JsonPointerData>): List<PatchOperation> {
        val optimizedOperations = mutableListOf<PatchOperation>()
        val parentToMergeOperation = mutableMapOf<JsonPointer, MergeOperation>()

        for (parentPath in parentToJsonPointerDataMap.keys) {
            val jsonPointerData = parentToJsonPointerDataMap[parentPath] ?: continue
            val parentJsonNode = JacksonUtils.locateContainer(targetJsonNode, parentPath)
            val parentObjectNode = parentJsonNode.deepCopy<ObjectNode>()
            parentObjectNode.retain(jsonPointerData.fieldNames)

            var mergeOperation = MergeOperation(parentPath, parentObjectNode)
            mergeOperation = parentObjectMergeOperation(targetJsonNode, mergeOperation)

            val parentMergeOperation = parentToMergeOperation[mergeOperation.path]

            if (parentMergeOperation == null) {
                parentToMergeOperation[mergeOperation.path] = mergeOperation
            } else {
                val mergedJsonNode = MergeOperation(parentMergeOperation.value).apply(mergeOperation.value)

                parentMergeOperation.value = mergedJsonNode
            }
        }

        if (parentToMergeOperation.isNotEmpty()) optimizedOperations.addAll(parentToMergeOperation.values)

        return optimizedOperations
    }

    private fun parentObjectMergeOperation(targetJsonNode: JsonNode, mergeOperation: MergeOperation): MergeOperation {
        if (JacksonUtils.isRoot(mergeOperation.path)) return mergeOperation

        val parentJsonNode = JacksonUtils.locateHeadContainer(targetJsonNode, mergeOperation.path)

        return if (parentJsonNode.isObject) {
            val parentObjectNode = parentJsonNode.deepCopy<ObjectNode>()
            parentObjectNode.removeAll()
            parentObjectNode.set<JsonNode>(JacksonUtils.lastFieldName(mergeOperation.path), mergeOperation.value)

            val parentMergeOperation = MergeOperation(mergeOperation.path.head(), parentObjectNode)

            parentObjectMergeOperation(targetJsonNode, parentMergeOperation)
        } else {
            mergeOperation
        }
    }
}
