package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode

/**
 * Base class for patch operations taking a path & value
 *
 * @author Shagaba
 */
abstract class PatchPathValueOperation : PatchPathOperation {
    var value: JsonNode? = null

    constructor() : super()
    constructor(value: JsonNode?) : super() {
        this.value = value
    }

    constructor(path: JsonPointer, value: JsonNode?) : super(path) {
        this.value = value
    }
}
