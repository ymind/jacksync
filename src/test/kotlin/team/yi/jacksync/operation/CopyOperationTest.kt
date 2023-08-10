package team.yi.jacksync.operation

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils

class CopyOperationTest : BaseTest() {
    @Test
    fun copyFromTitleToAuthorFirstName() {
        val copyMe = "please copy me"
        val author = Author("james", "bond", "james.bond@007.com")
        val postV1 = Post()
        postV1.title = copyMe
        postV1.author = author

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val copyOperation =
            CopyOperation(JacksonUtils.toJsonPointer("/title"), JacksonUtils.toJsonPointer("/author/firstName"))
        val addValueJson = objectMapperWrapper.writeValueAsString(copyOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.title, copyMe)
        Assertions.assertEquals(postV2.author?.firstName, copyMe)
        Assertions.assertEquals(postV2.author?.lastName, postV1.author?.lastName)
        Assertions.assertEquals(postV2.author?.email, postV1.author?.email)
    }

    @Test
    fun copyFromTagToCategory() {
        val copyMe = "please copy me"
        val postV1 = Post()
        postV1.tags = listOf("tag1", "tag2", copyMe, "tag3")
        postV1.categories = ArrayList()

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val copyOperation =
            CopyOperation(JacksonUtils.toJsonPointer("/tags/2"), JacksonUtils.toJsonPointer("/categories/0"))
        val addValueJson = objectMapperWrapper.writeValueAsString(copyOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.categories?.get(0), copyMe)
        Assertions.assertEquals(postV2.categories?.size, 1)
        Assertions.assertEquals(postV2.tags?.size, 4)
    }

    @Test
    fun copySection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val copyOperation =
            CopyOperation(JacksonUtils.toJsonPointer("/sections/1"), JacksonUtils.toJsonPointer("/sections/3"))
        val addValueJson = objectMapperWrapper.writeValueAsString(copyOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 6)
        Assertions.assertEquals(postV2.sections?.get(3), postV1.sections?.get(1))
    }

    @Test
    fun copyAfterLastSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        val postV1Node = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val copyOperation =
            CopyOperation(JacksonUtils.toJsonPointer("/sections/1"), JacksonUtils.toJsonPointer("/sections/-"))
        val addValueJson = objectMapperWrapper.writeValueAsString(copyOperation)

        // read operation
        val operation = objectMapperWrapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = objectMapperWrapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 6)
        Assertions.assertEquals(postV2.sections?.get(5), postV1.sections?.get(1))
    }
}
