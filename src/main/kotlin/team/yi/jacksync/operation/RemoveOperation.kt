package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.*
import team.yi.jacksync.exception.NoSuchPathException
import team.yi.jacksync.utils.JacksonUtils

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "remove" operation.
 *
 *
 * The "remove" operation removes the value at the target location.
 *
 *
 * The target location MUST exist for the operation to be successful.
 *
 *
 * If removing an element from an array, any elements above the specified index
 * are shifted one position to the left.
 *
 *
 * Example:
 *
 *
 * 1. A JSON Patch document:
 *
 *
 * { "op": "remove", "path": "/a/b/c"}.
 *
 * @author Shagaba
 */
class RemoveOperation : PatchPathOperation {
    constructor() : super()
    constructor(path: JsonPointer) : super(path)

    override fun apply(sourceJsonNode: JsonNode?): JsonNode? {
        val pathJsonNode = JacksonUtils.locateHeadContainer(sourceJsonNode ?: return null, path)

        if (pathJsonNode.isArray) {
            val pathArrayNode = pathJsonNode as ArrayNode
            val index = JacksonUtils.parseLast(path)

            if (index < 0 || index > pathArrayNode.size()) throw NoSuchPathException("No such path index - $index")

            pathArrayNode.remove(index)
        } else {
            val pathObjectNode = pathJsonNode as ObjectNode

            pathObjectNode.remove(JacksonUtils.lastFieldName(path))
        }

        return sourceJsonNode
    }
}
