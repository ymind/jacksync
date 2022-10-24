package team.yi.jacksync.sync

import com.fasterxml.jackson.databind.ObjectMapper
import team.yi.jacksync.exception.*
import team.yi.jacksync.patch.*
import team.yi.jacksync.utils.ChecksumUtils

open class LocalSyncProcessor(private val objectMapper: ObjectMapper) : SyncProcessor {
    override var isChecksumValidationEnabled = false
    protected var patchProcessor: PatchProcessor = ObjectPatchProcessor(objectMapper)

    @Throws(SyncException::class)
    override fun <T : Any> clientSync(sourceObject: SyncObject<T>, syncData: SyncData): SyncObject<T> {
        return sync(sourceObject, syncData, syncData.masterVersion)
    }

    @Throws(SyncException::class)
    override fun <T : Any> masterSync(sourceObject: SyncObject<T>, syncData: SyncData): SyncObject<T> {
        return sync(sourceObject, syncData, sourceObject.version)
    }

    @Throws(SyncException::class)
    protected fun <T : Any> sync(sourceObject: SyncObject<T>, syncData: SyncData, targetVersion: Long): SyncObject<T> {
        if (sourceObject.version != syncData.version) throw InvalidSyncVersionException("Sync Version Mismatch")

        val targetObject = patchProcessor.patch(sourceObject.data, syncData.operations)

        if (isChecksumValidationEnabled) try {
            val targetJson = objectMapper.writeValueAsString(targetObject)
            val isChecksumValid = ChecksumUtils.verifyChecksum(targetJson, syncData.targetChecksum)

            if (!isChecksumValid) throw ChecksumMismatchException("Checksum on target does not match checksum on syncData")
        } catch (e: Exception) {
            throw SyncProcessingException(e)
        }

        return SyncObject(targetVersion, targetObject)
    }
}
