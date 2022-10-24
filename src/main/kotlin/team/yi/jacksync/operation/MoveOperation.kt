package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.utils.JacksonUtils

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "move" operation.
 *
 *
 * The "move" operation removes the value at a specified location and adds it to
 * the target location.
 *
 *
 * The operation object MUST contain a "from" member, which is a string
 * containing a JSON Pointer value that references the location in the target
 * document to move the value from.
 *
 *
 * The "from" location MUST exist for the operation to be successful.
 *
 *
 * This operation is functionally identical to a "remove" operation on the
 * "from" location, followed immediately by an "add" operation at the target
 * location with the value that was just removed.
 *
 *
 * The "from" location MUST NOT be a proper prefix of the "path" location,
 * a location cannot be moved into one of its children.
 *
 *
 * Example:
 *
 *
 * 1. A JSON Patch document:
 *
 *
 * { "op": "move", "from": "/a/b/c", "path": "/a/b/d" }.
 *
 * @author Shagaba
 */
class MoveOperation : PatchDualPathOperation {
    constructor() : super()
    constructor(from: JsonPointer, path: JsonPointer) : super(from, path)

    override fun apply(sourceJsonNode: JsonNode?): JsonNode? {
        val pathJsonNode = JacksonUtils.locate(sourceJsonNode ?: return null, from)
        val copiedValue = pathJsonNode.deepCopy<JsonNode>()
        val removeOperation = RemoveOperation(from)
        val addOperation = AddOperation(path, copiedValue)

        return addOperation.apply(removeOperation.apply(sourceJsonNode))
    }
}
