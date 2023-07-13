package team.yi.jacksync.operation

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils

@Suppress("LocalVariableName", "VariableNaming")
class MergeOperationTest : BaseTest() {
    @Test
    fun addTitle() {
        val postV1 = Post()
        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val title = "my test title"
        val postV1_1 = Post()
        postV1_1.title = title

        val mergeOperation = MergeOperation(JacksonUtils.toJsonPointer("/"), objectMapperWrapper.valueToTree(postV1_1))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.title, title)
    }

    @Test
    fun addAuthor() {
        val postV1 = Post()
        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val author = Author("james", "bond", "james.bond@007.com")
        val postV1_1 = Post()
        postV1_1.author = author

        val mergeOperation = MergeOperation(JacksonUtils.toJsonPointer(""), objectMapperWrapper.valueToTree(postV1_1))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.author, author)
    }

    @Test
    fun replaceAuthorFirstName() {
        val postV1 = Post()
        postV1.author = Author("1", "2", "3")

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val mergeOperation = MergeOperation(objectMapperWrapper.readTree("{\"author\":{\"firstName\":\"james\"}}"))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.author?.firstName, "james")
    }

    @Test
    fun replaceAuthorFirstNameToNull() {
        val postV1 = Post()
        postV1.author = Author("1", "2", "3")

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val mergeOperation = MergeOperation(objectMapperWrapper.readTree("{\"author\":{\"firstName\":null}}"))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertNull(postV2.author?.firstName)
    }

    @Test
    fun removeAuthor() {
        val postV1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")
        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val postV1_1 = Post()
        val mergeOperation = MergeOperation(JacksonUtils.toJsonPointer(""), objectMapperWrapper.valueToTree(postV1_1))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1_1)
    }

    @Test
    fun addSections() {
        val postV1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val postV1_1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
        )

        val mergeOperation = MergeOperation(objectMapperWrapper.valueToTree(postV1_1))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1_1)
    }

    @Test
    fun addMiddleSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val section2_5 = Section("section-2.5", null)
        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            section2_5,
            Section("section-3", null),
            Section("section-4", null),
        )

        val mergeOperation = MergeOperation(objectMapperWrapper.valueToTree(postV1_1))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(2), section2_5)
    }

    @Test
    fun addLastSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val section5 = Section("section-5", null)
        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            section5,
        )

        val mergeOperation = MergeOperation(objectMapperWrapper.valueToTree(postV1_1))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(4), section5)
    }

    @Test
    fun removeSections() {
        val postV1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val postV1_1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")

        val mergeOperation = MergeOperation(objectMapperWrapper.valueToTree(postV1_1))
        val mergeValueJson = objectMapperWrapper.writeValueAsString(mergeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(mergeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2, postV1_1)
    }
}
