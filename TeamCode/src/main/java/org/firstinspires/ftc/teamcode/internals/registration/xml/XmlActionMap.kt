package org.firstinspires.ftc.teamcode.internals.registration.xml

enum class XmlActionMap {
    TELEMETRY_LOG_LINE,
    VARIABLE_SET,
    VARIABLE_USAGE,
    VARIABLE_DECLARE,
    INT_CONSTANT,
    BINARY_EXPRESSION,
    PARENTHESES,
    CLEAR_TELEMETRY,
    MOTOR_SET_POWER,
    FLOAT_CONSTANT,
    WAIT;

    companion object {
        @JvmStatic
        fun mapFromMPSString(s: String): XmlActionMap {
            return when (s) {
                "TelemetryLogLine" -> TELEMETRY_LOG_LINE
                "VariableSetStatement" -> VARIABLE_SET
                "VariableUsageExpression" -> VARIABLE_USAGE
                "VariableStatement" -> VARIABLE_DECLARE
                "IntegerConstant" -> INT_CONSTANT
                "BinaryOperation" -> BINARY_EXPRESSION
                "ParenthesizedExpression" -> PARENTHESES
                "TelemetryClear" -> CLEAR_TELEMETRY
                "WaitStatement" -> WAIT
                "MotorSetStatement" -> MOTOR_SET_POWER
                "FloatingPointConstant" -> FLOAT_CONSTANT
                else -> throw IllegalArgumentException("Unknown action $s")
            }
        }
    }
}