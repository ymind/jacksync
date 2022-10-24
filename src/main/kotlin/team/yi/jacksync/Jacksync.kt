package team.yi.jacksync

import com.fasterxml.jackson.databind.ObjectMapper
import team.yi.jacksync.diff.*
import team.yi.jacksync.diff.strategy.*
import team.yi.jacksync.sync.*

class Jacksync private constructor(jacksyncBuilder: JacksyncBuilder) {
    val objectMapper: ObjectMapper
    val syncProcessor: SyncProcessor
    val diffMapper: SyncDiffMapper

    init {
        objectMapper = jacksyncBuilder.objectMapper
        syncProcessor = jacksyncBuilder.localSyncProcessor
        diffMapper = jacksyncBuilder.diffMapperBuilder.syncObjectDiffMapper
    }

    class JacksyncBuilder(internal val objectMapper: ObjectMapper) {
        internal lateinit var localSyncProcessor: LocalSyncProcessor
        internal lateinit var diffMapperBuilder: DiffMapperBuilder

        /**
         * localSync to set
         */
        fun syncProcessor(): JacksyncBuilder {
            localSyncProcessor = LocalSyncProcessor(objectMapper)

            return this
        }

        fun diffMapper(): DiffMapperBuilder {
            diffMapperBuilder = DiffMapperBuilder(this, objectMapper)

            return diffMapperBuilder
        }

        fun build(): Jacksync {
            return Jacksync(this)
        }
    }

    class DiffMapperBuilder(
        private val jacksyncBuilder: JacksyncBuilder,
        private val objectMapper: ObjectMapper,
    ) {
        internal var syncObjectDiffMapper: SyncObjectDiffMapper

        init {
            syncObjectDiffMapper = SyncObjectDiffMapper(objectMapper)
        }

        fun diffStrategy(diffStrategy: DiffStrategy): JacksyncBuilder {
            syncObjectDiffMapper = SyncObjectDiffMapper(objectMapper, diffStrategy)

            return jacksyncBuilder
        }

        fun mergeOperationDiffProcessor(): JacksyncBuilder {
            syncObjectDiffMapper = SyncObjectDiffMapper(objectMapper, MergeOperationDiffStrategy())

            return jacksyncBuilder
        }

        fun simpleDiffStrategy(): JacksyncBuilder {
            syncObjectDiffMapper = SyncObjectDiffMapper(objectMapper, SimpleDiffStrategy())

            return jacksyncBuilder
        }

        fun computeChecksum(isComputeChecksum: Boolean): JacksyncBuilder {
            syncObjectDiffMapper.isComputeChecksum = isComputeChecksum

            return jacksyncBuilder
        }
    }

    companion object {
        fun builder(objectMapper: ObjectMapper): JacksyncBuilder {
            return JacksyncBuilder(objectMapper)
        }
    }
}
