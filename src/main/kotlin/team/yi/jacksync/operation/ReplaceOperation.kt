package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.*
import team.yi.jacksync.exception.NoSuchPathException
import team.yi.jacksync.utils.JacksonUtils

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "replace" operation.
 *
 *
 * The "replace" operation replaces the value at the target location with a new
 * value. The operation object MUST contain a "value" member whose content
 * specifies the replacement value.
 *
 *
 * The target location MUST exist for the operation to be successful.
 *
 *
 * This operation is functionally identical to a "remove" operation for a value,
 * followed immediately by an "add" operation at the same location with the
 * replacement value.
 *
 *
 * Example:
 *
 *
 * 1. A JSON Patch document:
 *
 *
 * { "op": "replace", "path": "/a/b/c", "value": 42 }.
 *
 * @author Shagaba
 */
class ReplaceOperation : PatchPathValueOperation {
    constructor() : super()
    constructor(path: JsonPointer, value: JsonNode?) : super(path, value)

    override fun apply(sourceJsonNode: JsonNode?): JsonNode? {
        val pathJsonNode = JacksonUtils.locateHeadContainer(sourceJsonNode ?: return null, path)

        if (pathJsonNode.isArray) {
            val pathArrayNode = pathJsonNode as ArrayNode
            val index = JacksonUtils.parseLast(path)

            if (index < 0 || index > pathArrayNode.size()) throw NoSuchPathException("No such path index - $index")

            pathArrayNode[index] = value
        } else {
            val pathObjectNode = pathJsonNode as ObjectNode

            pathObjectNode.replace(JacksonUtils.lastFieldName(path), value)
        }

        return sourceJsonNode
    }

    companion object {
        const val OP_NAME = "replace"
    }
}
