package team.yi.jacksync.difflog

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode

data class DiffLog(
    val op: String,
    val path: JsonPointer,
    val oldValue: JsonNode? = null,
    val newValue: JsonNode? = null,
)
