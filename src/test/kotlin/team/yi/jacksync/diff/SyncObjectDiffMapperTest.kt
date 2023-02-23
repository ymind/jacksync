package team.yi.jacksync.diff

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.diff.strategy.MergeOperationDiffStrategy
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.sync.SyncObject

@Suppress("LocalVariableName", "VariableNaming")
class SyncObjectDiffMapperTest : BaseTest() {
    private lateinit var syncDiffMapper: SyncDiffMapper

    @BeforeEach
    fun beforeEach() {
        mapper = newObjectMapper()
        syncDiffMapper = SyncObjectDiffMapper(mapper)
    }

    @Test
    fun diffFailsSourceIsNull() {
        val target = Post()

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            try {
                syncDiffMapper.diff(
                    SyncObject(1L, null),
                    SyncObject(1L, target)
                )
            } catch (e: Exception) {
                throw e.cause!!
            }
        }
    }

    @Test
    fun diffFailsTargetIsNull() {
        val source = Post()

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            try {
                syncDiffMapper.diff(
                    SyncObject(1L, source),
                    SyncObject(1L, null)
                )
            } catch (e: Exception) {
                throw e.cause!!
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun addTitle() {
        val postV1 = Post()
        val postV1_1 = Post()
        postV1_1.title = "my test title"

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun addAuthor() {
        val postV1 = Post()
        val postV1_1 = Post()
        postV1_1.author = Author("james", "bond", "james.bond@007.com")

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun addAuthorFirstName() {
        val postV1 = Post()
        postV1.author = Author()

        val postV1_1 = Post()
        postV1_1.author = Author("james", null, null)

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun addFirstSection() {
        val postV1 = Post()
        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null)
        )

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
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

        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-2.5", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
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

        val postV1_1 = Post()
        postV1_1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
            Section("section-5", null),
        )

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun removeTitle() {
        val postV1 = Post()
        postV1.title = "my test title"

        val postV1_1 = Post()

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun removeAuthor() {
        val postV1 = Post()
        postV1.author = Author("james", "bond", "james.bond@007.com")

        val postV1_1 = Post()

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun removeAuthorFirstName() {
        val postV1 = Post()
        postV1.author = Author("james", null, null)

        val postV1_1 = Post()
        postV1_1.author = Author()

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
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

        val postV1_1 = Post()

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun removeFirstSection() {
        val postV1 = Post()
        postV1.sections = listOf(
            Section("section-1", null)
        )

        val postV1_1 = Post()

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
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
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
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
        val sourceJsonNode = mapper.valueToTree<JsonNode>(postV1)
        val targetJsonNode = mapper.valueToTree<JsonNode>(postV1_1)
        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, postV1), SyncObject(1L, postV1_1))
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(operations.size, 1)
        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
        Assertions.assertEquals(mapper.treeToValue(syncedJsonNode, Post::class.java), postV1_1)
    }

    @Test
    @Throws(Exception::class)
    fun complicated() {
        syncDiffMapper = SyncObjectDiffMapper(mapper, MergeOperationDiffStrategy())

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

        val (_, _, _, operations) = syncDiffMapper.diff(SyncObject(1L, source), SyncObject(1L, target))

        // operations simple diff
        val sourceJsonNode = mapper.valueToTree<JsonNode>(source)
        val targetJsonNode = mapper.valueToTree<JsonNode>(target)
        var syncedJsonNode = sourceJsonNode.deepCopy<JsonNode>()

        for (operation in operations) {
            syncedJsonNode = operation.apply(syncedJsonNode)
        }

        Assertions.assertEquals(syncedJsonNode, targetJsonNode)
    }
}