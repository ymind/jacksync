package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import team.yi.jacksync.utils.JacksonUtils

/**
 * This is an implementation Inspired by RFC 6902 (JSON Patch) and RFC 7386
 * (JSON Merge Patch) - "merge" operation.
 *
 *
 * A JSON merge patch document describes changes to be made to a target JSON
 * document using a syntax that closely mimics the document being modified.
 *
 *
 * o Recipients of a merge patch document determine the exact set of changes
 * being requested by comparing the content of the provided patch against the
 * current content of the target document.
 *
 *
 * o If the provided merge patch contains members that do not appear within
 * the target, those members are added.
 *
 *
 * o If the target does contain the member, the value is replaced.
 *
 *
 * o Null values in the merge patch are given special meaning to indicate the
 * removal of existing values in the target.
 *
 *
 * Examples:
 *
 *
 * 1. A JSON Merge Patch document:
 *
 *
 * { "op" : "merge", "path" : "/", "value" : { "title" : null, "author" :
 * { "firstName" : "James" , "lastName" : "Bond" } } }.
 *
 * @author Shagaba
 */
class MergeOperation : PatchPathValueOperation {
    constructor() : super()
    constructor(value: JsonNode?) : super(value)
    constructor(path: JsonPointer, value: JsonNode?) : super(path, value)

    override fun apply(objectJsonNode: JsonNode?): JsonNode? {
        merge(objectJsonNode ?: return null, value, path)

        return objectJsonNode
    }

    private fun merge(sourceJsonNode: JsonNode, elementJsonNode: JsonNode?, path: JsonPointer) {
        val pathJsonNode = JacksonUtils.locate(sourceJsonNode, path)

        merge(pathJsonNode, elementJsonNode)
    }

    private fun merge(sourceJsonNode: JsonNode, elementJsonNode: JsonNode?) {
        if (elementJsonNode == null || elementJsonNode.size() == 0) {
            val sourceObjectNode = sourceJsonNode as ObjectNode

            sourceObjectNode.removeAll()
        } else {
            val iterator = elementJsonNode.fields()

            while (iterator.hasNext()) {
                val (key, nextValueJsonNode) = iterator.next()
                val sourceObjectNode = sourceJsonNode as ObjectNode
                val nextSourceJsonNode = sourceJsonNode[key]

                if (!sourceObjectNode.has(key)) {
                    sourceObjectNode.replace(key, nextValueJsonNode)

                    continue
                } else if (!nextSourceJsonNode.isObject || nextValueJsonNode.isNull) {
                    sourceObjectNode.replace(key, nextValueJsonNode)

                    continue
                }

                merge(nextSourceJsonNode, nextValueJsonNode)
            }
        }
    }

    companion object {
        const val OP_NAME = "merge"
    }
}
