package team.yi.jacksync.utils

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*

class JsonPointerDeserializer : JsonDeserializer<JsonPointer>() {
    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): JsonPointer {
        return try {
            JsonPointer.compile(jsonParser.text)
        } catch (e: Exception) {
            throw JsonMappingException(jsonParser, "cannot deserialize JSON Pointer", e)
        }
    }
}
