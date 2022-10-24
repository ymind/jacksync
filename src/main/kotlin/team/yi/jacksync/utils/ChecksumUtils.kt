package team.yi.jacksync.utils

import java.nio.charset.StandardCharsets
import java.util.zip.*

/**
 * Utility methods to compute and validate checksums.
 *
 * @author Shagaba
 */
object ChecksumUtils {
    fun computeChecksum(string: String?): String {
        require(!string.isNullOrEmpty()) { "Input string cannot be null or empty" }

        val jsonBytes = string.toByteArray(StandardCharsets.UTF_8)

        return computeChecksum(jsonBytes)
    }

    fun computeChecksum(byteArray: ByteArray): String {
        val checksum: Checksum = CRC32()
        checksum.update(byteArray, 0, byteArray.size)

        return java.lang.Long.toHexString(checksum.value)
    }

    fun verifyChecksum(string: String?, receivedChecksum: String?): Boolean {
        require(!string.isNullOrEmpty()) { "Input string cannot be null or empty" }
        require(!receivedChecksum.isNullOrEmpty()) { "Checksum cannot be null or empty" }

        val checksum = computeChecksum(string)

        return checksum == receivedChecksum
    }

    fun verifyChecksum(byteArray: ByteArray, receivedChecksum: String?): Boolean {
        require(!receivedChecksum.isNullOrEmpty()) { "checksum cannot be null or empty" }

        val checksum = computeChecksum(byteArray)

        return checksum == receivedChecksum
    }
}
