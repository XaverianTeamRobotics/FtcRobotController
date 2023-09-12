package org.firstinspires.ftc.teamcode.internals.hardware.data

data class GyroData(val heading: Int, val raw: IntArray, val status: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GyroData

        if (heading != other.heading) return false
        if (!raw.contentEquals(other.raw)) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = heading
        result = 31 * result + raw.contentHashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}
