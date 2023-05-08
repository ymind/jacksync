package team.yi.jacksync.diff

import team.yi.jacksync.JacksonObjectMapperWrapper
import team.yi.jacksync.diff.strategy.*
import team.yi.jacksync.exception.DiffProcessingException
import team.yi.jacksync.sync.*
import team.yi.jacksync.utils.ChecksumUtils

open class SyncObjectDiffMapper(
    protected var objectMapperWrapper: JacksonObjectMapperWrapper,
    diffStrategy: DiffStrategy = SimpleDiffStrategy(),
    var isComputeChecksum: Boolean = false,
) : SyncDiffMapper {
    var objectDiffMapper: ObjectDiffMapper
        protected set

    init {
        objectDiffMapper = ObjectDiffMapper(objectMapperWrapper, diffStrategy)
    }

    @Throws(DiffProcessingException::class)
    override fun <T> diff(source: SyncObject<T>, target: SyncObject<T>): SyncData {
        return try {
            val targetChecksum = if (isComputeChecksum) {
                val targetJson = objectMapperWrapper.writeValueAsString(target)

                ChecksumUtils.computeChecksum(targetJson)
            } else {
                null
            }

            SyncData(
                source.version,
                target.version,
                targetChecksum,
                objectDiffMapper.diff(source.data, target.data),
            )
        } catch (e: Exception) {
            throw DiffProcessingException(e)
        }
    }
}
