package team.yi.jacksync.operation

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils
import java.util.*

class MoveOperationTest : BaseTest() {
    @BeforeEach
    fun beforeEach() {
        mapper = newObjectMapper()
    }

    @Test
    @Throws(Exception::class)
    fun moveFromTitleToAuthorFirstName() {
        val moveMe = "please move me"
        val author = Author("james", "bond", "james.bond@007.com")
        val postV1 = Post()
        postV1.title = moveMe
        postV1.author = author

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val moveOperation = MoveOperation(JacksonUtils.toJsonPointer("/title"), JacksonUtils.toJsonPointer("/author/firstName"))
        val addValueJson = mapper.writeValueAsString(moveOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertNull(postV2.title)
        Assertions.assertEquals(postV2.author?.firstName, moveMe)
        Assertions.assertEquals(postV2.author?.lastName, postV1.author?.lastName)
        Assertions.assertEquals(postV2.author?.email, postV1.author?.email)
    }

    @Test
    @Throws(Exception::class)
    fun moveFromTagToCategory() {
        val moveMe = "please move me"
        val postV1 = Post()
        postV1.tags = listOf("tag1", "tag2", moveMe, "tag3")
        postV1.categories = ArrayList()

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val moveOperation = MoveOperation(JacksonUtils.toJsonPointer("/tags/2"), JacksonUtils.toJsonPointer("/categories/0"))
        val addValueJson = mapper.writeValueAsString(moveOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.categories?.get(0), moveMe)
        Assertions.assertEquals(postV2.categories?.size, 1)
        Assertions.assertEquals(postV2.tags?.size, 3)
    }

    @Test
    @Throws(Exception::class)
    fun moveSection() {
        val moveMe = "please move me"
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section(moveMe, null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-5", null),
        )

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val moveOperation = MoveOperation(JacksonUtils.toJsonPointer("/sections/1"), JacksonUtils.toJsonPointer("/sections/3"))
        val addValueJson = mapper.writeValueAsString(moveOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(3)?.title, moveMe)
    }

    @Test
    @Throws(Exception::class)
    fun moveAfterLastSection() {
        val moveMe = "please move me"
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section(moveMe, null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-5", null),
        )

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val moveOperation = MoveOperation(JacksonUtils.toJsonPointer("/sections/1"), JacksonUtils.toJsonPointer("/sections/-"))
        val addValueJson = mapper.writeValueAsString(moveOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(4)?.title, moveMe)
    }
}
