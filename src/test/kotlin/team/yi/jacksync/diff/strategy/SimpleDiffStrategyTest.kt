package team.yi.jacksync.diff.strategy

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*

@Suppress("LocalVariableName", "VariableNaming")
class SimpleDiffStrategyTest : BaseTest() {
    private val simpleDiffStrategy = SimpleDiffStrategy()

    @Test
    fun addTitle() {
        val postV1 = Post()
        val postV1_1 = Post()
        postV1_1.title = "my test title"

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun addAuthor() {
        val postV1 = Post()
        val postV1_1 = Post()
        postV1_1.author = Author("james", "bond", "james.bond@007.com")

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun addAuthorFirstName() {
        val postV1 = Post()
        postV1.author = Author()

        val postV1_1 = Post()
        postV1_1.author = Author("james", null, null)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun addFirstSection() {
        val postV1 = Post()
        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
        )

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
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

        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-2.5", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
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

        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeTitle() {
        val postV1 = Post()
        postV1.title = "my test title"

        val postV1_1 = Post()

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeAuthor() {
        val postV1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")

        val postV1_1 = Post()

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeAuthorFirstName() {
        val postV1 = Post()
        postV1.author = Author("james", null, null)

        val postV1_1 = Post()
        postV1_1.author = Author()

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeSections() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
        )

        val postV1_1 = Post()

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeFirstSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
        )

        val postV1_1 = Post()

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeMiddleSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-2.5", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeLastSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(postV1_1)
        val operations = simpleDiffStrategy.diff(sourceJsonNode, targetJsonNode)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }
}
