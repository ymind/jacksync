package team.yi.jacksync.patch

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import team.yi.jacksync.exception.PatchProcessingException
import team.yi.jacksync.operation.PatchOperation
import java.io.IOException

class ObjectPatchProcessor(private val objectMapper: ObjectMapper) : PatchProcessor {
    @Throws(PatchProcessingException::class)
    override fun <T : Any> patch(sourceObject: T, jsonOperations: String): T {
        val operations: List<PatchOperation>

        try {
            operations = objectMapper.readValue(jsonOperations, object : TypeReference<List<PatchOperation>>() {})
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }

        return patch(sourceObject, operations)
    }

    @Throws(PatchProcessingException::class)
    override fun <T : Any> patch(sourceObject: T, operations: List<PatchOperation>): T {
        return try {
            val sourceJsonNode = objectMapper.valueToTree<JsonNode>(sourceObject)
            val targetJsonNode = patch(sourceJsonNode, operations)

            objectMapper.treeToValue(targetJsonNode, sourceObject.javaClass) as T
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
