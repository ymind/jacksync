package team.yi.jacksync

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*

class JacksonObjectMapperWrapper(src: ObjectMapper) {
    val objectMapper: ObjectMapper = src.copy()
        .apply {
            this.setSerializationInclusion(JsonInclude.Include.ALWAYS)
            this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }

    constructor() : this(ObjectMapper())

    fun <T : JsonNode?> valueToTree(fromValue: Any?): T {
        return objectMapper.valueToTree(fromValue)
    }

    fun writeValueAsString(value: Any): String {
        return objectMapper.writeValueAsString(value)
    }

    fun <T> treeToValue(n: TreeNode?, valueType: Class<T>): T {
        return objectMapper.treeToValue(n, valueType)
    }

    fun readTree(content: String?): JsonNode? {
        return objectMapper.readTree(content)
    }

    fun <T> readValue(p: String, valueTypeRef: TypeReference<T>): T {
        return objectMapper.readValue(p, valueTypeRef)
    }

    fun <T> readValue(content: String?, valueType: Class<T>): T {
        return objectMapper.readValue(content, valueType)
    }

    fun writerFor(rootType: TypeReference<*>): ObjectWriter {
        return objectMapper.writerFor(rootType)
    }
}
