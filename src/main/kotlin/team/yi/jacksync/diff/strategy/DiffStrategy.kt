package team.yi.jacksync.diff.strategy

import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.exception.DiffProcessingException
import team.yi.jacksync.operation.*

interface DiffStrategy {
    @Throws(DiffProcessingException::class)
    fun diff(sourceJsonNode: JsonNode, targetJsonNode: JsonNode): List<PatchOperation>
}
