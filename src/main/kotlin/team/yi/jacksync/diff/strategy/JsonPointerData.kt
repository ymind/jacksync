package team.yi.jacksync.diff.strategy

import team.yi.jacksync.operation.PatchOperation

class JsonPointerData {
    val fieldNames: MutableList<String> = mutableListOf()
    val operations: MutableList<PatchOperation> = mutableListOf()
}
