package team.yi.jacksync.patch

import com.fasterxml.jackson.core.type.TypeReference
import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.diff.ObjectDiffMapper
import team.yi.jacksync.operation.*
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.utils.JacksonUtils
import java.util.*

@Suppress("LocalVariableName", "VariableNaming")
class ObjectPatchProcessorTest : BaseTest() {
    private lateinit var patchProcessor: PatchProcessor

    @BeforeEach
    fun beforeEach() {
        patchProcessor = ObjectPatchProcessor(objectMapperWrapper)
    }

    @Test
    @Throws(Exception::class)
    fun serverSyncV1() {
        // server post
        val postV1 = Post()
        postV1.version = 1L

        // operations
        val addOperation: PatchOperation = AddOperation(
            JacksonUtils.toJsonPointer("/title"),
            objectMapperWrapper.valueToTree("my test title")
        )
        val operations = listOf(addOperation)

        // expected target
        val targetPost = Post()
        targetPost.title = "my test title"
        targetPost.version = 1L

        // server patch
        val postV2 = patchProcessor.patch(postV1, operations)

        Assertions.assertEquals(postV2, targetPost)
    }

    @Test
    @Throws(Exception::class)
    fun clientSyncV2() {
        // server post
        val serverPostV1 = Post()
        serverPostV1.version = 2L
        // 1st update
        serverPostV1.title = "my 2nd test title"

        // operations
        val addOperation: PatchOperation = AddOperation(
            JacksonUtils.toJsonPointer("/title"),
            objectMapperWrapper.valueToTree("my 2nd test title")
        )
        val replaceOperation: PatchOperation = ReplaceOperation(
            JacksonUtils.toJsonPointer("/version"),
            objectMapperWrapper.valueToTree(2)
        )
        val operations = listOf(addOperation, replaceOperation)

        // expected target
        val targetPost = Post()
        targetPost.title = serverPostV1.title
        targetPost.version = 2L

        // server patch
        val postV2 = patchProcessor.patch(serverPostV1, operations)

        Assertions.assertEquals(postV2, targetPost)
    }

    @Test
    @Throws(Exception::class)
    fun clientSyncV3() {
        // client post
        val postV1 = Post()
        postV1.version = 1L
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
        )

        // expected post
        val postV2 = Post()
        postV2.version = 2L
        postV2.title = "my 2nd test title"
        postV2.sections = listOf(
            Section("section-1", null),
            Section("section-3", null),
        )

        // [{"op":"replace","path":"/version","value":2},{"op":"replace","path":"/title","value":"my 2nd test title"},{"op":"remove","path":"/sections/1"}]
        val operations = ObjectDiffMapper(objectMapperWrapper).diff(postV1, postV2)
        val jsonOperations = objectMapperWrapper.writerFor(object : TypeReference<List<PatchOperation>>() {}).writeValueAsString(operations)

        // server patch
        val postV2_1 = patchProcessor.patch(postV1, operations)
        val postV2_2 = patchProcessor.patch(postV1, jsonOperations)

        Assertions.assertEquals(postV2_1, postV2)
        Assertions.assertEquals(postV2_2, postV2)
    }

    @Test
    @Throws(Exception::class)
    fun clientSyncV4() {
        // client post
        val postV1 = Post()
        postV1.id = "007"
        postV1.title = "Diamonds Are Forever"
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
        )

        // expected post
        val postV2 = Post()
        postV2.id = "007"
        postV2.title = "Diamonds Are Forever"
        postV2.author = Author("james", "bond", "james.bond@mi6.com")
        postV2.sections = listOf(
            Section("section-1", null),
            Section("section-3", null),
        )
        postV2.tags = listOf("007", "Sean Connery", "action")

        val operations = ObjectDiffMapper(objectMapperWrapper).diff(postV1, postV2)
        val jsonOperations = objectMapperWrapper.writerFor(object : TypeReference<List<PatchOperation>>() {}).writeValueAsString(operations)

        // server patch
        val postV2_1 = patchProcessor.patch(postV1, operations)
        val postV2_2 = patchProcessor.patch(postV1, jsonOperations)

        Assertions.assertEquals(postV2_1, postV2)
        Assertions.assertEquals(postV2_2, postV2)
    }
}
