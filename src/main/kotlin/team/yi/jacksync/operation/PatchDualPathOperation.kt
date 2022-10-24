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
abstract class PatchDualPathOperation : PatchPathOperation {
    @JsonSerialize(using = ToStringSerializer::class)
    @JsonDeserialize(using = JsonPointerDeserializer::class)
    protected lateinit var from: JsonPointer

    constructor() : super()
    constructor(from: JsonPointer, path: JsonPointer) : super(path) {
        this.from = from
    }
}
