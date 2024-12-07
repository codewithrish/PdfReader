package com.codewithrish.pdfreader.core.common.util

object DataUnitConverter {

    private const val BITS_IN_BYTE = 8L
    private const val BYTES_IN_KILOBYTE = 1024L
    private const val KILOBYTES_IN_MEGABYTE = 1024L
    private const val MEGABYTES_IN_GIGABYTE = 1024L
    private const val GIGABYTES_IN_TERABYTE = 1024L

    // Bits to other units
    fun bitsToBytes(bits: Long): Double = bits.toDouble() / BITS_IN_BYTE
    fun bitsToKilobytes(bits: Long): Double = bitsToBytes(bits) / BYTES_IN_KILOBYTE
    fun bitsToMegabytes(bits: Long): Double = bitsToKilobytes(bits) / KILOBYTES_IN_MEGABYTE
    fun bitsToGigabytes(bits: Long): Double = bitsToMegabytes(bits) / MEGABYTES_IN_GIGABYTE
    fun bitsToTerabytes(bits: Long): Double = bitsToGigabytes(bits) / GIGABYTES_IN_TERABYTE

    // Bytes to other units
    fun bytesToKilobytes(bytes: Long): Double = bytes.toDouble() / BYTES_IN_KILOBYTE
    fun bytesToMegabytes(bytes: Long): Double = bytesToKilobytes(bytes) / KILOBYTES_IN_MEGABYTE
    fun bytesToGigabytes(bytes: Long): Double = bytesToMegabytes(bytes) / MEGABYTES_IN_GIGABYTE
    fun bytesToTerabytes(bytes: Long): Double = bytesToGigabytes(bytes) / GIGABYTES_IN_TERABYTE

    // Kilobytes to other units
    fun kilobytesToMegabytes(kilobytes: Long): Double = kilobytes.toDouble() / KILOBYTES_IN_MEGABYTE
    fun kilobytesToGigabytes(kilobytes: Long): Double = kilobytesToMegabytes(kilobytes) / MEGABYTES_IN_GIGABYTE
    fun kilobytesToTerabytes(kilobytes: Long): Double = kilobytesToGigabytes(kilobytes) / GIGABYTES_IN_TERABYTE

    // Megabytes to other units
    fun megabytesToGigabytes(megabytes: Long): Double = megabytes.toDouble() / MEGABYTES_IN_GIGABYTE
    fun megabytesToTerabytes(megabytes: Long): Double = megabytesToGigabytes(megabytes) / GIGABYTES_IN_TERABYTE

    // Gigabytes to other units
    fun gigabytesToTerabytes(gigabytes: Long): Double = gigabytes.toDouble() / GIGABYTES_IN_TERABYTE

    // Human-readable format
    fun formatDataSize(bytes: Long): String {
        val kb = bytesToKilobytes(bytes)
        val mb = bytesToMegabytes(bytes)
        val gb = bytesToGigabytes(bytes)
        val tb = bytesToTerabytes(bytes)

        return when {
            tb >= 1 -> String.format("%.2f TB", tb)
            gb >= 1 -> String.format("%.2f GB", gb)
            mb >= 1 -> String.format("%.2f MB", mb)
            kb >= 1 -> String.format("%.2f KB", kb)
            else -> "$bytes Bytes"
        }
    }
}
