package team.yi.jacksync.sync

import team.yi.jacksync.operation.PatchOperation

data class SyncData(
    val version: Long,
    val masterVersion: Long,
    val targetChecksum: String?,
    val operations: List<PatchOperation>,
)
