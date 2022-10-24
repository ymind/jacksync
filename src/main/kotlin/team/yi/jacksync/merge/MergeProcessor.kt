package team.yi.jacksync.merge

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import team.yi.jacksync.exception.MergeProcessingException
import team.yi.jacksync.operation.MergeOperation

interface MergeProcessor {
    @Throws(MergeProcessingException::class)
    fun <T : Any> merge(sourceObject: T, jsonValue: String?): T

    @Throws(MergeProcessingException::class)
    fun <T : Any> merge(sourceObject: T, path: String, jsonValue: String?): T

    @Throws(MergeProcessingException::class)
    fun <T : Any> merge(sourceObject: T, value: JsonNode?): T

    @Throws(MergeProcessingException::class)
    fun <T : Any> merge(sourceObject: T, path: JsonPointer, value: JsonNode?): T

    @Throws(MergeProcessingException::class)
    fun <T : Any> merge(sourceObject: T, operation: MergeOperation): T
}
