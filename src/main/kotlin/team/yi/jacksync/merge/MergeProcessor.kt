package team.yi.jacksync.merge

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.operation.MergeOperation

interface MergeProcessor {
    fun <T : Any> merge(sourceObject: T, jsonValue: String?): T

    fun <T : Any> merge(sourceObject: T, path: String, jsonValue: String?): T

    fun <T : Any> merge(sourceObject: T, value: JsonNode?): T

    fun <T : Any> merge(sourceObject: T, path: JsonPointer, value: JsonNode?): T

    fun <T : Any> merge(sourceObject: T, operation: MergeOperation): T
}
