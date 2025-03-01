package org.firstinspires.ftc.teamcode.autonomous.limelight

fun alignmentGraphic(sizeX: Int, sizeY: Int, x: Int, y: Int): String {
    val sb = StringBuilder()
    for (i in 0..sizeY) {
        for (j in 0..(sizeX * 2)) {
            when {
                i == 0 || i == sizeY                    -> sb.append("-")
                j == 0 || j == sizeX * 2                -> sb.append("|")
                i == y && j == x * 2                    -> sb.append("+")
                i == y                                  -> sb.append("-")
                j == x * 2                              -> sb.append("|")
                i == sizeX / 2 && j == sizeY * 2 / 2    -> sb.append("o")
                else                                    -> sb.append(" ")
            }
        }
        sb.append("\n")
    }
    return sb.toString()
}

fun rotationGraphic(p: Int, s: Int): String {
    val sb = StringBuilder()
    val halfS = s / 2
    when {
        p < 0 -> sb.append(" ".repeat(halfS - kotlin.math.abs(p))).append("<".repeat(kotlin.math.abs(p))).append("|")
        p > 0 -> sb.append(" ".repeat(halfS)).append("|").append(">".repeat(p))
        else -> sb.append(" ".repeat(halfS)).append("|")
    }
    return sb.toString()
}