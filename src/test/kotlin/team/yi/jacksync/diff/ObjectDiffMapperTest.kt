package team.yi.jacksync.diff

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.support.dto.*

class ObjectDiffMapperTest : BaseTest() {
    private val diffMapper = ObjectDiffMapper(objectMapperWrapper)

    @Test
    fun diffFailsSourceIsNull() {
        val target = Post()

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            diffMapper.diff(null, target)
        }
    }

    @Test
    fun diffFailsTargetIsNull() {
        val source = Post()

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            diffMapper.diff(source, null)
        }
    }

    @Test
    fun addTitle() {
        val source = Post()
        val target = Post()
        target.title = "my test title"

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun addAuthor() {
        val source = Post()
        val target = Post()
        target.author = Author("james", "bond", "james.bond@007.com")

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun addAuthorFirstName() {
        val source = Post()
        source.author = Author()

        val target = Post()
        target.author = Author("james", null, null)

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun addFirstSection() {
        val source = Post()

        val target = Post()
        target.sections = listOf(
            Section("section-1", null)
        )

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun addMiddleSection() {
        val source = Post()
        source.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val target = Post()
        target.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-2.5", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun addLastSection() {
        val source = Post()
        source.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val target = Post()
        target.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(1, operations.size)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeTitle() {
        val source = Post()
        source.title = "my test title"

        val target = Post()
        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeAuthor() {
        val source = Post()
        source.author = Author("james", "bond", "james.bond@007.com")

        val target = Post()
        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeAuthorFirstName() {
        val source = Post()
        source.author = Author("james", null, null)

        val target = Post()
        target.author = Author()

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeSections() {
        val source = Post()
        source.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
        )

        val target = Post()
        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeFirstSection() {
        val source = Post()
        source.sections = listOf(
            Section("section-1", null)
        )

        val target = Post()
        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeMiddleSection() {
        val source = Post()
        source.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-2.5", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val target = Post()
        target.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun removeLastSection() {
        val source = Post()
        source.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        val target = Post()
        target.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val operations = diffMapper.diff(source, target)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }

    @Test
    fun complicated() {
        val source = Post()
        source.id = "1"
        source.version = 1L
        source.author = Author("firstName", "lastName", "email")
        source.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-x", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        val target = Post()
        target.id = "1"
        target.title = "A Title"
        target.version = 2L
        target.author = Author("firstName", "lastName", "email@email.com")
        target.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4 update", null, "private note"),
        )

        val operations = diffMapper.diff(source, target, true)

        // operations simple diff
        val sourceJsonNode = objectMapperWrapper.valueToTree<JsonNode>(source)
        val targetJsonNode = objectMapperWrapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }
}
