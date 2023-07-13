package team.yi.jacksync.operation

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils

class ReplaceOperationTest : BaseTest() {
    @Test
    fun replaceTitle() {
        val postV1 = Post()
        postV1.title = "1stTitle"

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val title = "my test title"
        val replaceOperation = ReplaceOperation(JacksonUtils.toJsonPointer("/title"), objectMapperWrapper.valueToTree(title))
        val replaceValueJson = objectMapperWrapper.writeValueAsString(replaceOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(replaceValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.title, title)
    }

    @Test
    fun replaceAuthor() {
        val authorV1 = Author("harry", "potter", "harry.potter@wizard.com")
        val postV1 = Post()
        postV1.author = authorV1

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val author = Author("james", "bond", "james.bond@007.com")
        val replaceOperation = ReplaceOperation(JacksonUtils.toJsonPointer("/author"), objectMapperWrapper.valueToTree(author))
        val replaceValueJson = objectMapperWrapper.writeValueAsString(replaceOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(replaceValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.author, author)
    }

    @Test
    fun replaceAuthorFirstName() {
        val authorV1 = Author("harry", "potter", "harry.potter@wizard.com")
        val postV1 = Post()
        postV1.author = authorV1

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val firstName = "james"
        val replaceOperation = ReplaceOperation(JacksonUtils.toJsonPointer("/author/firstName"), objectMapperWrapper.valueToTree(firstName))
        val replaceValueJson = objectMapperWrapper.writeValueAsString(replaceOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(replaceValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.author?.firstName, firstName)
        Assertions.assertEquals(postV2.author?.lastName, authorV1.lastName)
        Assertions.assertEquals(postV2.author?.email, authorV1.email)
    }

    @Test
    fun replaceMiddleSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3333", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val section3 = Section("section-3", null)
        val replaceOperation = ReplaceOperation(JacksonUtils.toJsonPointer("/sections/2"), objectMapperWrapper.valueToTree(section3))
        val replaceValueJson = objectMapperWrapper.writeValueAsString(replaceOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(replaceValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(2), section3)
    }

    @Test
    fun replaceLastSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5555", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val section5 = Section("section-5", null)
        val replaceOperation = ReplaceOperation(JacksonUtils.toJsonPointer("/sections/4"), objectMapperWrapper.valueToTree(section5))
        val replaceValueJson = objectMapperWrapper.writeValueAsString(replaceOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(replaceValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(4), section5)
    }
}
