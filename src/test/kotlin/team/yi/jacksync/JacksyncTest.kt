package team.yi.jacksync

import org.junit.jupiter.api.*
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.sync.SyncObject

class JacksyncTest : BaseTest() {
    private lateinit var jacksync: Jacksync

    @BeforeEach
    fun beforeEach() {
        mapper = newObjectMapper()

        jacksync = Jacksync
            .builder(mapper)
            .syncProcessor()
            .diffMapper()
            .simpleDiffStrategy()
            .build()
    }

    @Test
    @Throws(Exception::class)
    fun addTitle() {
        val title = "my test title"

        val postV1 = Post()
        postV1.version = 1L
        postV1.author = Author("1", "2", "3")
        postV1.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-3", null),
            Section("section-4", null),
        )

        val postV2 = Post()
        postV2.version = 2L
        postV2.title = title
        postV2.author = Author("james", "bond", "3")
        postV2.sections = listOf(
            Section("section-1", null),
            Section("section-2", null),
            Section("section-X", null),
            Section("section-4", null),
        )

        // SyncData regular diff
        val syncData = jacksync.diffMapper.diff(SyncObject(1L, postV1), SyncObject(2L, postV2))
        // SyncObject clientSync
        val syncPostV2 = jacksync.syncProcessor.clientSync(SyncObject(1L, postV1), syncData)

        Assertions.assertEquals(syncPostV2.data, postV2)
    }
}
