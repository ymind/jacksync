package team.yi.jacksync.utils

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.exception.*

/**
 * Utility class for dealing with Jackson library API.
 *
 * @author Shagaba
 */
object JacksonUtils {
    const val SEPARATOR = "/"
    const val AFTER_LAST_ARRAY_ELEMENT = "-"

    fun isRoot(jsonPointer: JsonPointer): Boolean {
        return try {
            jsonPointer.head() == null
        } catch (e: Exception) {
            false
        }
    }

    fun toJsonPointer(path: String?): JsonPointer {
        requireNotNull(path) { "Path cannot be null" }
        require(path == path.trim()) { "Path has not been trimmed: '$path'" }

        return if (SEPARATOR == path.trim()) {
            JsonPointer.compile("")
        } else {
            JsonPointer.compile(path)
        }
    }

    fun locateHeadContainer(sourceJsonNode: JsonNode, path: JsonPointer): JsonNode {
        return locateContainer(sourceJsonNode, path.head())
    }

    fun locateContainer(sourceJsonNode: JsonNode, path: JsonPointer): JsonNode {
        val pathJsonNode = locate(sourceJsonNode, path)

        if (!pathJsonNode.isContainerNode) throw IllegalContainerException("Path is not a container - $path")

        return pathJsonNode
    }

    fun locate(sourceJsonNode: JsonNode, path: JsonPointer): JsonNode {
        var pathJsonNode = sourceJsonNode

        if (!isRoot(path)) pathJsonNode = sourceJsonNode.at(path)
        if (pathJsonNode.isMissingNode) throw NoSuchPathException("No such path - $path")

        return pathJsonNode
    }

    fun lastFieldName(jsonPointer: JsonPointer): String {
        val lastPath = jsonPointer.last().toString()

        return lastPath.substring(1)
    }

    fun parseLast(path: JsonPointer): Int {
        val fieldName = lastFieldName(path)

        return try {
            fieldName.toInt()
        } catch (exception: NumberFormatException) {
            throw NoSuchPathException("Path is not an index path - $path")
        }
    }

    fun isAfterLastArrayElement(path: JsonPointer): Boolean {
        val fieldName = lastFieldName(path)

        return AFTER_LAST_ARRAY_ELEMENT == fieldName
    }

    fun append(path: JsonPointer, fieldName: String): JsonPointer {
        return path.append(JsonPointer.compile(SEPARATOR + fieldName))
    }
}
