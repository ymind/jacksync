package team.yi.jacksync.operation

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import java.util.function.UnaryOperator

/**
 * Abstract class providing support methods for one patch operation.
 *
 * @author Shagaba
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "op")
@JsonSubTypes(
    JsonSubTypes.Type(value = AddOperation::class, name = "add"),
    JsonSubTypes.Type(value = RemoveOperation::class, name = "remove"),
    JsonSubTypes.Type(value = ReplaceOperation::class, name = "replace"),
    JsonSubTypes.Type(value = CopyOperation::class, name = "copy"),
    JsonSubTypes.Type(value = MoveOperation::class, name = "move"),
    JsonSubTypes.Type(value = TestOperation::class, name = "test"),
    JsonSubTypes.Type(value = MergeOperation::class, name = "merge")
)
abstract class PatchOperation : UnaryOperator<JsonNode?> {
    abstract val path: JsonPointer
}
