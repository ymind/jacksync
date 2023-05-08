package team.yi.jacksync.operation

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils

class RemoveOperationTest : BaseTest() {
    @Test
    @Throws(Exception::class)
    fun removeTitle() {
        val title = "my test title"
        val postV1 = Post()
        postV1.title = title

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val removeOperation = RemoveOperation(JacksonUtils.toJsonPointer("/title"))
        val removeValueJson = objectMapperWrapper.writeValueAsString(removeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(removeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertNull(postV2.title)
    }

    @Test
    @Throws(Exception::class)
    fun removeAuthor() {
        val author = Author("james", "bond", "james.bond@007.com")
        val postV1 = Post()
        postV1.author = author

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val removeOperation = RemoveOperation(JacksonUtils.toJsonPointer("/author"))
        val removeValueJson = objectMapperWrapper.writeValueAsString(removeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(removeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertNull(postV2.author)
    }

    @Test
    @Throws(Exception::class)
    fun removeAuthorFirstName() {
        val author = Author("james", "bond", "james.bond@007.com")
        val postV1 = Post()
        postV1.author = author

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val removeOperation = RemoveOperation(JacksonUtils.toJsonPointer("/author/firstName"))
        val removeValueJson = objectMapperWrapper.writeValueAsString(removeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(removeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertNull(postV2.author?.firstName)
    }

    @Test
    @Throws(Exception::class)
    fun removeSections() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val removeOperation = RemoveOperation(JacksonUtils.toJsonPointer("/sections"))
        val removeValueJson = objectMapperWrapper.writeValueAsString(removeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(removeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertNull(postV2.sections)
    }

    @Test
    @Throws(Exception::class)
    fun removeFirstSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val removeOperation = RemoveOperation(JacksonUtils.toJsonPointer("/sections/0"))
        val removeValueJson = objectMapperWrapper.writeValueAsString(removeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(removeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 0)
    }

    @Test
    @Throws(Exception::class)
    fun removeMiddleSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val removeOperation = RemoveOperation(JacksonUtils.toJsonPointer("/sections/1"))
        val removeValueJson = objectMapperWrapper.writeValueAsString(removeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(removeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 2)
        Assertions.assertEquals(postV2.sections?.get(0)?.title, "section-1")
        Assertions.assertEquals(postV2.sections?.get(1)?.title, "section-3")
    }

    @Test
    @Throws(Exception::class)
    fun removeLastSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val removeOperation = RemoveOperation(JacksonUtils.toJsonPointer("/sections/3"))
        val removeValueJson = objectMapperWrapper.writeValueAsString(removeOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(removeValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 3)
        Assertions.assertEquals(postV2.sections?.get(2)?.title, "section-3")
    }
}
