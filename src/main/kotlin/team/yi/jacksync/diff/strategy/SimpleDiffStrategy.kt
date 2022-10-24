package team.yi.jacksync.diff.strategy

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.exception.DiffProcessingException
import team.yi.jacksync.operation.*
import team.yi.jacksync.utils.JacksonUtils
import kotlin.math.max

class SimpleDiffStrategy : DiffStrategy {
    @Throws(DiffProcessingException::class)
    override fun diff(sourceJsonNode: JsonNode, targetJsonNode: JsonNode): List<PatchOperation> {
        val operations: MutableList<PatchOperation> = ArrayList()

        return diff(sourceJsonNode, targetJsonNode, operations, JsonPointer.compile(""))
    }

    fun diff(sourceJsonNode: JsonNode?, targetJsonNode: JsonNode?, patchOperations: MutableList<PatchOperation>, path: JsonPointer): List<PatchOperation> {
        if (sourceJsonNode == null || targetJsonNode == null) return patchOperations

        if (sourceJsonNode != targetJsonNode) {
            if (sourceJsonNode.isArray && targetJsonNode.isArray) {
                diffArrays(sourceJsonNode, targetJsonNode, patchOperations, path)
            } else if (sourceJsonNode.isObject && targetJsonNode.isObject) {
                diffObjects(sourceJsonNode, targetJsonNode, patchOperations, path)
            } else {
                patchOperations.add(ReplaceOperation(path, targetJsonNode.deepCopy()))
            }
        }

        return patchOperations
    }

    private fun diffArrays(sourceJsonNode: JsonNode, targetJsonNode: JsonNode, patchOperations: MutableList<PatchOperation>, path: JsonPointer): List<PatchOperation> {
        if (sourceJsonNode.isArray && targetJsonNode.isArray) {
            val commonNodes: MutableList<JsonNode> = ArrayList()

            sourceJsonNode.iterator().forEachRemaining { e -> commonNodes.add(e) }

            val targetNodes: MutableList<JsonNode> = ArrayList()

            targetJsonNode.iterator().forEachRemaining { e -> targetNodes.add(e) }

            commonNodes.retainAll(targetNodes)

            var commonIndex = 0
            var sourceIndex = 0
            var targetIndex = 0
            val maxIndex = max(sourceJsonNode.size(), targetJsonNode.size())

            (0 until maxIndex).forEach { _ ->
                val commonNode = if (commonNodes.size > commonIndex) commonNodes[commonIndex] else null
                val sourceNode = if (sourceJsonNode.size() > sourceIndex) sourceJsonNode[sourceIndex] else null
                val targetNode = if (targetJsonNode.size() > targetIndex) targetJsonNode[targetIndex] else null

                if (commonNode == sourceNode && commonNode == targetNode) {
                    ++commonIndex
                    ++sourceIndex
                    ++targetIndex
                } else {
                    when (commonNode) {
                        sourceNode -> {
                            // add missing target
                            val targetPath = JacksonUtils.append(path, (targetIndex++).toString())

                            patchOperations.add(AddOperation(targetPath, targetNode?.deepCopy()))
                        }

                        targetNode -> {
                            // remove target
                            val targetPath = JacksonUtils.append(path, (sourceIndex++).toString())

                            patchOperations.add(RemoveOperation(targetPath))
                        }

                        else -> {
                            val targetPath = JacksonUtils.append(path, (targetIndex++).toString())

                            diff(sourceNode, targetNode, patchOperations, targetPath)

                            ++sourceIndex
                        }
                    }
                }
            }
        }

        return patchOperations
    }

    private fun diffObjects(sourceJsonNode: JsonNode, targetJsonNode: JsonNode, patchOperations: MutableList<PatchOperation>, path: JsonPointer): List<PatchOperation> {
        if (sourceJsonNode.isObject && targetJsonNode.isObject) {
            // source iteration
            sourceJsonNode.fieldNames().forEachRemaining { fieldName ->
                val fieldNamePath = JacksonUtils.append(path, fieldName ?: return@forEachRemaining)

                if (targetJsonNode.has(fieldName)) {
                    diff(sourceJsonNode.path(fieldName), targetJsonNode.path(fieldName), patchOperations, fieldNamePath)
                } else {
                    patchOperations.add(RemoveOperation(fieldNamePath))
                }
            }

            // target iteration
            targetJsonNode.fieldNames().forEachRemaining { fieldName ->
                if (!sourceJsonNode.has(fieldName ?: return@forEachRemaining)) {
                    val fieldNamePath = JacksonUtils.append(path, fieldName)

                    patchOperations.add(AddOperation(fieldNamePath, targetJsonNode.path(fieldName).deepCopy()))
                }
            }
        }

        return patchOperations
    }
}
