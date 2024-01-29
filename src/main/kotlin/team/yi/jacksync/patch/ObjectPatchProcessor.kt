package team.yi.jacksync.patch

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.JacksonObjectMapperWrapper
import team.yi.jacksync.exception.PatchProcessingException
import team.yi.jacksync.operation.PatchOperation
import java.io.IOException

class ObjectPatchProcessor(private val objectMapperWrapper: JacksonObjectMapperWrapper) : PatchProcessor {
    override fun <T : Any> patch(sourceObject: T, jsonOperations: String): T {
        val operations: List<PatchOperation>

        try {
            operations = objectMapperWrapper.readValue(
                jsonOperations,
                object : TypeReference<List<PatchOperation>>() {},
            )
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }

        return patch(sourceObject, operations)
    }

    @Suppress("TooGenericExceptionCaught")
    override fun <T : Any> patch(sourceObject: T, operations: List<PatchOperation>): T {
        return try {
            val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(sourceObject)
            val targetJsonNode = patch(sourceJsonNode, operations)

            objectMapperWrapper.treeToValue(targetJsonNode, sourceObject.javaClass)
        } catch (e: Exception) {
            throw PatchProcessingException(e)
        }
    }

    fun patch(currentJsonNode: JsonNode, operations: List<PatchOperation>): JsonNode {
        var syncedJsonNode = currentJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        return syncedJsonNode
    }
}
