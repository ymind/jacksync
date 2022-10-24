package team.yi.jacksync.merge

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.*
import team.yi.jacksync.exception.MergeProcessingException
import team.yi.jacksync.operation.MergeOperation
import team.yi.jacksync.utils.JacksonUtils
import java.io.IOException

class ObjectMergeProcessor(private val objectMapper: ObjectMapper) : MergeProcessor {
    @Throws(MergeProcessingException::class)
    override fun <T : Any> merge(sourceObject: T, jsonValue: String?): T {
        return merge(sourceObject, "", jsonValue)
    }

    @Throws(MergeProcessingException::class)
    override fun <T : Any> merge(sourceObject: T, path: String, jsonValue: String?): T {
        return try {
            merge(sourceObject, JacksonUtils.toJsonPointer(path), objectMapper.readTree(jsonValue))
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }
    }

    @Throws(MergeProcessingException::class)
    override fun <T : Any> merge(sourceObject: T, value: JsonNode?): T {
        return merge(sourceObject, MergeOperation(value))
    }

    @Throws(MergeProcessingException::class)
    override fun <T : Any> merge(sourceObject: T, path: JsonPointer, value: JsonNode?): T {
        return merge(sourceObject, MergeOperation(path, value))
    }

    @Throws(MergeProcessingException::class)
    override fun <T : Any> merge(sourceObject: T, operation: MergeOperation): T {
        return try {
            val sourceJsonNode = objectMapper.valueToTree<JsonNode>(sourceObject)
            val targetJsonNode = operation.apply(sourceJsonNode.deepCopy())

            objectMapper.treeToValue(targetJsonNode, sourceObject.javaClass) as T
        } catch (e: Exception) {
            throw MergeProcessingException(e)
        }
    }
}
