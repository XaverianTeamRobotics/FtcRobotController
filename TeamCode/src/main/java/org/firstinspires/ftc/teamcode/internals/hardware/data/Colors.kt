package org.firstinspires.ftc.teamcode.internals.hardware.data

data class Colors(val rgba: IntArray, val hsv: DoubleArray, val gsv: Int, val gain: Double, val raw: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Colors

        if (!rgba.contentEquals(other.rgba)) return false
        if (!hsv.contentEquals(other.hsv)) return false
        if (gsv != other.gsv) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rgba.contentHashCode()
        result = 31 * result + hsv.contentHashCode()
        result = 31 * result + gsv
        return result
    }
}
