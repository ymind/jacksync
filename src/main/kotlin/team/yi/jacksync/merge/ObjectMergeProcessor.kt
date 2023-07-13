package team.yi.jacksync.merge

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.JacksonObjectMapperWrapper
import team.yi.jacksync.exception.MergeProcessingException
import team.yi.jacksync.operation.MergeOperation
import team.yi.jacksync.utils.JacksonUtils
import java.io.IOException

class ObjectMergeProcessor(private val objectMapperWrapper: JacksonObjectMapperWrapper) : MergeProcessor {
    override fun <T : Any> merge(sourceObject: T, jsonValue: String?): T {
        return merge(sourceObject, "", jsonValue)
    }

    override fun <T : Any> merge(sourceObject: T, path: String, jsonValue: String?): T {
        return try {
            merge(sourceObject, JacksonUtils.toJsonPointer(path), objectMapperWrapper.readTree(jsonValue))
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }
    }

    override fun <T : Any> merge(sourceObject: T, value: JsonNode?): T {
        return merge(sourceObject, MergeOperation(value))
    }

    override fun <T : Any> merge(sourceObject: T, path: JsonPointer, value: JsonNode?): T {
        return merge(sourceObject, MergeOperation(path, value))
    }

    override fun <T : Any> merge(sourceObject: T, operation: MergeOperation): T {
        return try {
            val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(sourceObject)
            val targetJsonNode = operation.apply(sourceJsonNode.deepCopy())

            objectMapperWrapper.treeToValue(targetJsonNode, sourceObject.javaClass)
        } catch (e: Exception) {
            throw MergeProcessingException(e)
        }
    }
}
