package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.*
import team.yi.jacksync.exception.NoSuchPathException
import team.yi.jacksync.utils.JacksonUtils

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "add" operation.
 *
 *
 * The "add" operation performs one of the following functions, depending upon
 * what the target location references:
 *
 *
 * o If the target location specifies an array index, a new value is inserted
 * into the array at the specified index.
 *
 *
 * o The character "-" is a new array index referenced value of a nonexistent
 * member after the last array element "/foo/-".
 *
 *
 * o If the target location specifies an object member that does not already
 * exist, a new member is added to the object.
 *
 *
 * o If the target location specifies an object member that does exist, that
 * member's value is replaced.
 *
 *
 * The operation object MUST contain a "value" member whose content specifies
 * the value to be added.
 *
 *
 * Examples:
 *
 *
 * 1. A JSON Patch document:
 *
 *
 * { "op": "add", "path": "/a/b/c", "value": [ "foo", "bar" ] }.
 *
 *
 * 2. Adding an Object Member:
 *
 *
 * An example target JSON document: { "foo": "bar"}.
 *
 *
 * A JSON Patch document: [ { "op": "add", "path": "/baz", "value": "qux" } ].
 *
 *
 * The resulting JSON document: { "baz": "qux", "foo": "bar" }.
 *
 *
 * 3. Adding an Array Element:
 *
 *
 * An example target JSON document: { "foo": [ "bar", "baz" ] }.
 *
 *
 * A JSON Patch document: [ { "op": "add", "path": "/foo/1", "value": "qux" } ].
 *
 *
 * The resulting JSON document: { "foo": [ "bar", "qux", "baz" ] }.
 *
 * @author Shagaba
 */
class AddOperation : PatchPathValueOperation {
    constructor() : super()
    constructor(path: JsonPointer, value: JsonNode?) : super(path, value)

    override fun apply(sourceJsonNode: JsonNode?): JsonNode? {
        val pathJsonNode = JacksonUtils.locateHeadContainer(sourceJsonNode ?: return null, path)

        if (pathJsonNode.isArray) {
            val pathArrayNode = pathJsonNode as ArrayNode

            if (JacksonUtils.isAfterLastArrayElement(path)) pathArrayNode.add(value) else {
                val index = JacksonUtils.parseLast(path)

                if (index < 0 || index > pathArrayNode.size()) throw NoSuchPathException("No such path index - $index")
                if (index == pathArrayNode.size()) pathArrayNode.add(value) else pathArrayNode.insert(index, value)
            }
        } else {
            val pathObjectNode = pathJsonNode as ObjectNode

            pathObjectNode.replace(JacksonUtils.lastFieldName(path), value)
        }

        return sourceJsonNode
    }
}
