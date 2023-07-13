package team.yi.jacksync.diff.strategy

import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.operation.PatchOperation

interface DiffStrategy {
    fun diff(sourceJsonNode: JsonNode, targetJsonNode: JsonNode, invertible: Boolean = false): List<PatchOperation>
}
