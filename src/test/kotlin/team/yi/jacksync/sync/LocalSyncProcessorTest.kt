package team.yi.jacksync.sync

import org.junit.jupiter.api.*
import team.yi.jacksync.BaseTest
import team.yi.jacksync.operation.*
import team.yi.jacksync.support.dto.Post
import team.yi.jacksync.utils.*
import java.util.*

class LocalSyncProcessorTest : BaseTest() {
    private lateinit var syncProcessor: SyncProcessor

    @BeforeEach
    fun beforeEach() {
        mapper = newObjectMapper()
        syncProcessor = LocalSyncProcessor(mapper)
    }

    @Test
    @Throws(Exception::class)
    fun serverSyncV1() {
        // server post
        val serverPostV1 = Post()
        serverPostV1.version = 1L

        // client post
        val clientPostV1 = Post()
        clientPostV1.version = 1L
        // 1st update
        clientPostV1.title = "my test title"

        // expected target
        val targetPost = Post()
        targetPost.title = clientPostV1.title
        targetPost.version = 1L

        // sync syncData & operations
        val syncData = SyncData(
            1L,
            0L,
            ChecksumUtils.computeChecksum(mapper.writeValueAsString(targetPost)),
            listOf(AddOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(targetPost.title)))
        )

        // server sync
        val syncPostV2 = syncProcessor.clientSync(SyncObject(1L, serverPostV1), syncData)

        Assertions.assertEquals(syncPostV2.data, targetPost)
    }

    @Test
    @Throws(Exception::class)
    fun clientSyncV2() {
        // client post
        val clientPostV1 = Post()
        clientPostV1.version = 1L
        clientPostV1.title = "my test title"

        // server post
        val serverPostV1 = Post()
        serverPostV1.version = 2L
        // 1st update
        serverPostV1.title = "my 2nd test title"

        // expected target
        val targetPost = Post()
        targetPost.title = serverPostV1.title
        targetPost.version = 2L

        // sync syncData & operations
        val addOperation = AddOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(targetPost.title))
        val replaceOperation = ReplaceOperation(JacksonUtils.toJsonPointer("/version"), mapper.valueToTree(2))
        val operations = listOf<PatchOperation>(addOperation, replaceOperation)

        val syncData = SyncData(
            1L,
            2L,
            ChecksumUtils.computeChecksum(mapper.writeValueAsString(targetPost)),
            operations
        )

        // server sync
        val syncPostV2 = syncProcessor.masterSync(SyncObject(1L, clientPostV1), syncData)

        Assertions.assertEquals(syncPostV2.data, targetPost)
    }
}
