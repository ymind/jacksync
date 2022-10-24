package team.yi.jacksync.operation

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.annotation.*
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import team.yi.jacksync.utils.JsonPointerDeserializer

/**
 * Base class for patch operations taking a path
 *
 * @author Shagaba
 */
abstract class PatchPathOperation : PatchOperation {
    @JsonSerialize(using = ToStringSerializer::class)
    @JsonDeserialize(using = JsonPointerDeserializer::class)
    final override var path: JsonPointer

    constructor() : super() {
        path = JsonPointer.compile("")
    }

    constructor(path: JsonPointer) : super() {
        this.path = path
    }
}
