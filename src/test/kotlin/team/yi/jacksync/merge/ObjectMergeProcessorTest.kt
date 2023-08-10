package team.yi.jacksync.merge

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.operation.MergeOperation
import team.yi.jacksync.support.dto.*

@Suppress("LocalVariableName", "VariableNaming")
class ObjectMergeProcessorTest : BaseTest() {
    private val mergeProcessor = ObjectMergeProcessor(objectMapperWrapper)

    @Test
    fun addTitle() {
        val postV1 = Post()
        val title = "my test title"
        val postV1_1 = Post()
        postV1_1.title = title

        val postExpected = Post()
        postExpected.title = "my test title"

        val mergeOperation = MergeOperation(objectMapperWrapper.valueToTree(postV1_1))
        val postV2 = mergeProcessor.merge(postV1, mergeOperation)

        Assertions.assertEquals(postV2, postExpected)
    }

    @Test
    fun addAuthor() {
        val postV1 = Post()
        val author = Author("james", "bond", "james.bond@007.com")
        val postV1_1 = Post()
        postV1_1.author = author

        val postExpected = Post()
        postExpected.author = Author("james", "bond", "james.bond@007.com")

        val mergeOperation = MergeOperation(objectMapperWrapper.valueToTree(postV1_1))
        val postV2 = mergeProcessor.merge(postV1, mergeOperation)

        Assertions.assertEquals(postV2, postExpected)
    }

    @Test
    fun replaceAuthorFirstName() {
        val postV1 = Post()
        postV1.author = Author("1", "2", "3")

        val postExpected = Post()
        postExpected.author = Author("james", "2", "3")

        val postV2 = mergeProcessor.merge(
            postV1,
            objectMapperWrapper.readTree("{\"author\":{\"firstName\":\"james\"}}"),
        )

        Assertions.assertEquals(postV2, postExpected)
    }

    @Test
    fun replaceAuthorFirstNameToNull() {
        val postV1 = Post()
        postV1.author = Author("1", "2", "3")

        val postV2 = mergeProcessor.merge(postV1, "{\"author\":{\"firstName\":null}}")

        Assertions.assertNull(postV2.author?.firstName)
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

        val section2_5 = Section("section-2.5", null)

        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            section2_5,
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV2 = mergeProcessor.merge(postV1, objectMapperWrapper.valueToTree<JsonNode>(postV1_1))

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

        val section5 = Section("section-5", null)

        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            section5,
        )

        val postV2 = mergeProcessor.merge(postV1, objectMapperWrapper.valueToTree<JsonNode>(postV1_1))

        Assertions.assertEquals(postV2.sections?.size, 5)
        Assertions.assertEquals(postV2.sections?.get(4), section5)
    }
}
