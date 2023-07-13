package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.utils.JacksonUtils

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "copy" operation.
 *
 *
 * The "copy" operation copies the value at a specified location to the target
 * location.
 *
 *
 * The operation object MUST contain a "from" member, which is a string
 * containing a JSON Pointer value that references the location in the target
 * document to copy the value from.
 *
 *
 * The "from" location MUST exist for the operation to be successful.
 *
 *
 * This operation is functionally identical to an "add" operation at the target
 * location using the value specified in the "from" member.
 *
 *
 * Example:
 *
 *
 * 1. A JSON Patch document:
 *
 *
 * { "op": "copy", "from": "/a/b/c", "path": "/a/b/e" }.
 *
 * @author Shagaba
 */
class CopyOperation : PatchDualPathOperation {
    constructor() : super()
    constructor(from: JsonPointer, path: JsonPointer) : super(from, path)

    override fun apply(sourceJsonNode: JsonNode?): JsonNode? {
        val pathJsonNode = JacksonUtils.locate(sourceJsonNode ?: return null, from)
        val copiedValue = pathJsonNode.deepCopy<JsonNode>()
        val addOperation = AddOperation(path, copiedValue)

        return addOperation.apply(sourceJsonNode)
    }

    companion object {
        const val OP_NAME = "copy"
    }
}
