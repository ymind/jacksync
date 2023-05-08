package team.yi.jacksync

import team.yi.jacksync.diff.*
import team.yi.jacksync.diff.strategy.*
import team.yi.jacksync.sync.*

class Jacksync private constructor(jacksyncBuilder: JacksyncBuilder) {
    val objectMapperWrapper: JacksonObjectMapperWrapper
    val syncProcessor: SyncProcessor
    val diffMapper: SyncDiffMapper

    init {
        objectMapperWrapper = jacksyncBuilder.mapperWrapper
        syncProcessor = jacksyncBuilder.localSyncProcessor
        diffMapper = jacksyncBuilder.diffMapperBuilder.syncObjectDiffMapper
    }

    class JacksyncBuilder(internal val mapperWrapper: JacksonObjectMapperWrapper) {
        internal lateinit var localSyncProcessor: LocalSyncProcessor
        internal lateinit var diffMapperBuilder: DiffMapperBuilder

        /**
         * localSync to set
         */
        fun syncProcessor(): JacksyncBuilder {
            localSyncProcessor = LocalSyncProcessor(mapperWrapper)

            return this
        }

        fun diffMapper(): DiffMapperBuilder {
            diffMapperBuilder = DiffMapperBuilder(this, mapperWrapper)

            return diffMapperBuilder
        }

        fun build(): Jacksync {
            return Jacksync(this)
        }
    }

    class DiffMapperBuilder(
        private val jacksyncBuilder: JacksyncBuilder,
        private val mapperWrapper: JacksonObjectMapperWrapper,
    ) {
        internal var syncObjectDiffMapper: SyncObjectDiffMapper

        init {
            syncObjectDiffMapper = SyncObjectDiffMapper(mapperWrapper)
        }

        fun diffStrategy(diffStrategy: DiffStrategy): JacksyncBuilder {
            syncObjectDiffMapper = SyncObjectDiffMapper(mapperWrapper, diffStrategy)

            return jacksyncBuilder
        }

        fun mergeOperationDiffProcessor(): JacksyncBuilder {
            syncObjectDiffMapper = SyncObjectDiffMapper(mapperWrapper, MergeOperationDiffStrategy())

            return jacksyncBuilder
        }

        fun simpleDiffStrategy(): JacksyncBuilder {
            syncObjectDiffMapper = SyncObjectDiffMapper(mapperWrapper, SimpleDiffStrategy())

            return jacksyncBuilder
        }

        fun computeChecksum(isComputeChecksum: Boolean): JacksyncBuilder {
            syncObjectDiffMapper.isComputeChecksum = isComputeChecksum

            return jacksyncBuilder
        }
    }

    companion object {
        fun builder(objectMapperWrapper: JacksonObjectMapperWrapper): JacksyncBuilder {
            return JacksyncBuilder(objectMapperWrapper)
        }
    }
}
