package team.yi.jacksync.diff

import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.JacksonObjectMapperWrapper
import team.yi.jacksync.diff.strategy.*
import team.yi.jacksync.operation.PatchOperation

class ObjectDiffMapper(
    private val objectMapperWrapper: JacksonObjectMapperWrapper,
    private val diffStrategy: DiffStrategy = SimpleDiffStrategy(),
) : DiffMapper {
    override fun <T> diff(source: T, target: T): List<PatchOperation> {
        requireNotNull(source) { "Source object cannot be null" }
        requireNotNull(target) { "Target object cannot be null" }

        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)

        return diffStrategy.diff(sourceJsonNode, targetJsonNode)
    }
}
