package team.yi.jacksync.operation

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils

@Suppress("LocalVariableName", "VariableNaming")
class MergeOperationTest : BaseTest() {
    @BeforeEach
    fun beforeEach() {
        mapper = newObjectMapper()
    }

    @Test
    @Throws(Exception::class)
    fun addTitle() {
        val postV1 = Post()
        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val title = "my test title"
        val postV1_1 = Post()
        postV1_1.title = title

        val mergeOperation = MergeOperation(JacksonUtils.toJsonPointer("/"), mapper.valueToTree(postV1_1))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.title, title)
    }

    @Test
    @Throws(Exception::class)
    fun addAuthor() {
        val postV1 = Post()
        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val author = Author("james", "bond", "james.bond@007.com")
        val postV1_1 = Post()
        postV1_1.author = author

        val mergeOperation = MergeOperation(JacksonUtils.toJsonPointer(""), mapper.valueToTree(postV1_1))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.author, author)
    }

    @Test
    @Throws(Exception::class)
    fun replaceAuthorFirstName() {
        val postV1 = Post()
        postV1.author = Author("1", "2", "3")

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val mergeOperation = MergeOperation(mapper.readTree("{\"author\":{\"firstName\":\"james\"}}"))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.author?.firstName, "james")
    }

    @Test
    @Throws(Exception::class)
    fun replaceAuthorFirstNameToNull() {
        val postV1 = Post()
        postV1.author = Author("1", "2", "3")

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val mergeOperation = MergeOperation(mapper.readTree("{\"author\":{\"firstName\":null}}"))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertNull(postV2.author?.firstName)
    }

    @Test
    @Throws(Exception::class)
    fun removeAuthor() {
        val postV1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")
        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val postV1_1 = Post()
        val mergeOperation = MergeOperation(JacksonUtils.toJsonPointer(""), mapper.valueToTree(postV1_1))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun addSections() {
        val postV1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val postV1_1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
        )

        val mergeOperation = MergeOperation(mapper.valueToTree(postV1_1))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun addMiddleSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val section2_5 = Section("section-2.5", null)
        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            section2_5,
            Section("section-3", null),
            Section("section-4", null),
        )

        val mergeOperation = MergeOperation(mapper.valueToTree(postV1_1))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(2), section2_5)
    }

    @Test
    @Throws(Exception::class)
    fun addLastSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val section5 = Section("section-5", null)
        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            section5,
        )

        val mergeOperation = MergeOperation(mapper.valueToTree(postV1_1))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(4), section5)
    }

    @Test
    @Throws(Exception::class)
    fun removeSections() {
        val postV1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
        )

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val postV1_1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")

        val mergeOperation = MergeOperation(mapper.valueToTree(postV1_1))
        val mergeValueJson = mapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = mapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1_1)
    }
}
