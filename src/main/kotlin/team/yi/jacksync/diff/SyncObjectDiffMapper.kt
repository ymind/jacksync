package team.yi.jacksync.diff

import team.yi.jacksync.JacksonObjectMapperWrapper
import team.yi.jacksync.diff.strategy.*
import team.yi.jacksync.exception.DiffProcessingException
import team.yi.jacksync.sync.*
import team.yi.jacksync.utils.ChecksumUtils

class SyncObjectDiffMapper(
    private val objectMapperWrapper: JacksonObjectMapperWrapper,
    diffStrategy: DiffStrategy = SimpleDiffStrategy(),
    internal var isComputeChecksum: Boolean = false,
) : SyncDiffMapper {
    private val objectDiffMapper: ObjectDiffMapper = ObjectDiffMapper(objectMapperWrapper, diffStrategy)

    override fun <T> diff(source: SyncObject<T>, target: SyncObject<T>, invertible: Boolean): SyncData {
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
                objectDiffMapper.diff(source.data, target.data, invertible),
            )
        } catch (e: Exception) {
            throw DiffProcessingException(e)
        }
    }
}
