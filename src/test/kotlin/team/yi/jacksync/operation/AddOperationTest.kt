package team.yi.jacksync.operation

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils

@Suppress("LocalVariableName", "VariableNaming")
class AddOperationTest : BaseTest() {
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
        val addOperation = AddOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(title))
        val addValueJson = mapper.writeValueAsString(addOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
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
        val addOperation = AddOperation(JacksonUtils.toJsonPointer("/author"), mapper.valueToTree(author))
        val addValueJson = mapper.writeValueAsString(addOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.author, author)
    }

    @Test
    @Throws(Exception::class)
    fun addAuthorFirstName() {
        val postV1 = Post()
        postV1.author = Author()

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val firstName = "james"
        val addOperation = AddOperation(JacksonUtils.toJsonPointer("/author/firstName"), mapper.valueToTree(firstName))
        val addValueJson = mapper.writeValueAsString(addOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.author?.firstName, firstName)
    }

    @Test
    @Throws(Exception::class)
    fun addFirstSection() {
        val postV1 = Post()
        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val section = Section("section-1", null)
        val sections: MutableList<Section> = ArrayList()
        sections.add(section)

        val addOperation = AddOperation(JacksonUtils.toJsonPointer("/sections"), mapper.valueToTree(sections))
        val addValueJson = mapper.writeValueAsString(addOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 1)
        Assertions.assertEquals(postV2.sections?.get(0), section)
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
        val addOperation = AddOperation(JacksonUtils.toJsonPointer("/sections/2"), mapper.valueToTree(section2_5))
        val addValueJson = mapper.writeValueAsString(addOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
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
        val addOperation = AddOperation(JacksonUtils.toJsonPointer("/sections/4"), mapper.valueToTree(section5))
        val addValueJson = mapper.writeValueAsString(addOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(4), section5)
    }

    @Test
    @Throws(Exception::class)
    fun addAfterLastSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV1Node = mapper.valueToTree<JsonNode>(postV1)
        val section5 = Section("section-5", null)
        val addOperation = AddOperation(JacksonUtils.toJsonPointer("/sections/-"), mapper.valueToTree(section5))
        val addValueJson = mapper.writeValueAsString(addOperation)

        // read operation
        val operation = mapper.readValue(addValueJson, PatchOperation::class.java)
        val postV2Node = operation.apply(postV1Node)
        val postV2 = mapper.treeToValue(postV2Node, Post::class.java)

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(4), section5)
    }
}
