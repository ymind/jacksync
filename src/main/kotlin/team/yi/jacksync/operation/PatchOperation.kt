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
    JsonSubTypes.Type(value = AddOperation::class, name = AddOperation.OP_NAME),
    JsonSubTypes.Type(value = RemoveOperation::class, name = RemoveOperation.OP_NAME),
    JsonSubTypes.Type(value = ReplaceOperation::class, name = ReplaceOperation.OP_NAME),
    JsonSubTypes.Type(value = CopyOperation::class, name = CopyOperation.OP_NAME),
    JsonSubTypes.Type(value = MoveOperation::class, name = MoveOperation.OP_NAME),
    JsonSubTypes.Type(value = TestOperation::class, name = TestOperation.OP_NAME),
    JsonSubTypes.Type(value = MergeOperation::class, name = MergeOperation.OP_NAME)
)
abstract class PatchOperation : UnaryOperator<JsonNode?> {
    abstract val path: JsonPointer

    companion object {
        fun getOpName(operation: PatchOperation): String? {
            return when (operation) {
                is AddOperation -> AddOperation.OP_NAME
                is RemoveOperation -> RemoveOperation.OP_NAME
                is ReplaceOperation -> ReplaceOperation.OP_NAME
                is CopyOperation -> CopyOperation.OP_NAME
                is MoveOperation -> MoveOperation.OP_NAME
                is TestOperation -> TestOperation.OP_NAME
                is MergeOperation -> MergeOperation.OP_NAME

                else -> null
            }
        }
    }
}
