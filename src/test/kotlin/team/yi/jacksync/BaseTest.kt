package team.yi.jacksync

import com.fasterxml.jackson.databind.*

@Suppress("UnnecessaryAbstractClass")
abstract class BaseTest {
    protected var mapper = newObjectMapper()

    fun newObjectMapper(): ObjectMapper {
        val jacksonObjectMapper = ObjectMapper()
        jacksonObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        jacksonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        return jacksonObjectMapper
    }
}
