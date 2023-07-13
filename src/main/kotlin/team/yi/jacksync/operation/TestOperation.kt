package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.exception.InvalidTestValueException
import team.yi.jacksync.utils.JacksonUtils

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "test" operation.
 *
 *
 * The "test" operation tests that a value at the target location is equal to a
 * specified value.
 *
 *
 * The operation object MUST contain a "value" member that conveys the value to
 * be compared to the target location's value.
 *
 *
 * The target location MUST be equal to the "value" value for the operation to
 * be considered successful.
 *
 *
 * Here, "equal" means that the value at the target location and the value
 * conveyed by "value" are of the same JSON type, and that they are considered
 * equal by the following rules for that type:
 *
 *
 * o strings: are considered equal if they contain the same number of Unicode
 * characters and their code points are byte-by-byte equal.
 *
 *
 * o numbers: are considered equal if their values are numerically equal.
 *
 *
 * o arrays: are considered equal if they contain the same number of values, and
 * if each value can be considered equal to the value at the corresponding
 * position in the other array, using this list of type-specific rules.
 *
 *
 * o objects: are considered equal if they contain the same number of members,
 * and if each member can be considered equal to a member in the other object,
 * by comparing their keys (as strings) and their values (using this list of
 * type-specific rules).
 *
 *
 * o literals (false, true, and null): are considered equal if they are the
 * same.
 *
 *
 * Examples:
 *
 *
 * 1. A JSON Patch document:
 *
 *
 * { "op": "test", "path": "/a/b/c", "value": "foo" }.
 *
 * @author Shagaba
 */
class TestOperation : PatchPathValueOperation {
    constructor() : super()
    constructor(path: JsonPointer, value: JsonNode?) : super(path, value)

    override fun apply(sourceJsonNode: JsonNode?): JsonNode? {
        val pathJsonNode = JacksonUtils.locate(sourceJsonNode ?: return null, path)

        if (pathJsonNode != value) throw InvalidTestValueException("Value test failure - Expected: $value, but: was $pathJsonNode")

        return sourceJsonNode
    }

    companion object {
        const val OP_NAME = "test"
    }
}
