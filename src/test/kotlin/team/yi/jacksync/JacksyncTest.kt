package team.yi.jacksync

import org.junit.jupiter.api.*
import team.yi.jacksync.support.dto.*
import team.yi.jacksync.sync.SyncObject
import java.util.*

class JacksyncTest : BaseTest() {
    private val jacksync = Jacksync.builder(objectMapperWrapper)
        .syncProcessor()
        .diffMapper()
        .simpleDiffStrategy()
        .build()

    @Test
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

        println(0.toBitSet().also {
            it.set(0, true)
            it.set(1, true)
            it.set(2, true)
        }.toInt())
    }
}

fun Int.toBitSet(): BitSet {
    var intValue = this
    val bits = BitSet()
    var index = 0

    while (intValue != 0) {
        if (intValue % 2 != 0) bits.set(index)

        ++index

        intValue = intValue ushr 1
    }

    return bits
}

fun BitSet.toInt(): Int {
    return (0 until this.length()).sumOf {
        if (this[it]) {
            1 shl it
        } else {
            0
        }
    }
}
