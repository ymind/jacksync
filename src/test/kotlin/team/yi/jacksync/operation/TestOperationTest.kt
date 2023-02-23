package team.yi.jacksync.operation

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.exception.InvalidTestValueException
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils
import java.util.*

class TestOperationTest : BaseTest() {
    @BeforeEach
    fun beforeEach() {
        mapper = newObjectMapper()
    }

    @Test
    @Throws(Exception::class)
    fun testTitle() {
        val title = "please test me"
        val postV1 = Post()
        postV1.title = title

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val testOperation = TestOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(title))
        val testValueJson = mapper.writeValueAsString(testOperation)

        // read operation
        val operation = mapper.readValue(testValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1)
    }

    @Test
    fun testInvalidTitle() {
        Assertions.assertThrows(InvalidTestValueException::class.java) {
            val title = "please test me"
            val postV1 = Post()
            postV1.title = "$title, im different"

            val postV1Node = mapper.valueToTree<JsonNode>(postV1)
            val testOperation = TestOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(title))
            val testValueJson = mapper.writeValueAsString(testOperation)

            // read operation
            val operation = mapper.readValue(testValueJson, PatchOperation::class.java)

            operation.apply(postV1Node)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testTags() {
        val testMe = "please test me"
        val postV1 = Post()
        postV1.tags = listOf("tag1", "tag2", testMe, "tag3")

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val testOperation = TestOperation(JacksonUtils.toJsonPointer("/tags/2"), mapper.valueToTree(testMe))
        val testValueJson = mapper.writeValueAsString(testOperation)

        // read operation
        val operation = mapper.readValue(testValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1)
    }

    @Test
    fun testInvalidTag() {
        Assertions.assertThrows(InvalidTestValueException::class.java) {
            val testMe = "please test me"
            val postV1 = Post()
            postV1.tags = listOf("tag1", "tag2", "$testMe, im different", "tag3")

            val postV1Node = mapper.valueToTree<JsonNode>(postV1)
            val testOperation = TestOperation(JacksonUtils.toJsonPointer("/tags/2"), mapper.valueToTree(testMe))
            val testValueJson = mapper.writeValueAsString(testOperation)

            // read operation
            val operation = mapper.readValue(testValueJson, PatchOperation::class.java)
            val postV2Node = operation.apply(postV1Node)

            mapper.treeToValue(postV2Node, Post::class.java)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testSection() {
        val section2 = Section("section-2", null)
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val testOperation = TestOperation(JacksonUtils.toJsonPointer("/sections/1"), mapper.valueToTree(section2))
        val testValueJson = mapper.writeValueAsString(testOperation)

        // read operation
        val operation = mapper.readValue(testValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1)
    }

    @Test
    fun testInvalidSection() {
        Assertions.assertThrows(InvalidTestValueException::class.java) {
            val section2 = Section("section-2Invalid", null)
            val postV1 = Post()
            postV1.sections = listOf(
                Section("section-1", null),
                Section("section-2", null),
                Section("section-3", null),
                Section("section-4", null),
                Section("section-5", null),
            )

            val postV1Node = mapper.valueToTree<JsonNode>(postV1)
            val testOperation = TestOperation(JacksonUtils.toJsonPointer("/sections/1"), mapper.valueToTree(section2))
            val testValueJson = mapper.writeValueAsString(testOperation)

            // read operation
            val operation = mapper.readValue(testValueJson, PatchOperation::class.java)
            val postV2Node = operation.apply(postV1Node)

            mapper.treeToValue(postV2Node, Post::class.java)
        }
    }
}