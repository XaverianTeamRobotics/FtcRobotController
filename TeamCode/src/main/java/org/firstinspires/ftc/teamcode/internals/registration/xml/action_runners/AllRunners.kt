package org.firstinspires.ftc.teamcode.internals.registration.xml.action_runners

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.registration.xml.XMLCodeLine
import org.firstinspires.ftc.teamcode.internals.registration.xml.XmlActionMap
import org.firstinspires.ftc.teamcode.internals.registration.xml.XmlEvironment
import org.firstinspires.ftc.teamcode.internals.registration.xml.XmlVariable
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging.Companion.telemetry
import org.w3c.dom.Element

class VariableDeclarationRunner : BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        val name = node.getElementsByTagName("name").item(0).textContent
        val value = (node.getElementsByTagName("value").item(0) as Element).getElementsByTagName("expression").item(0)

        XMLCodeLine(value as Element).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        env.variables[name] = XmlVariable(name, computedValue)
    }
}

class IntegerConstantRunner : BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        env.cacheValue1 = node.textContent.toInt()
    }
}

class TelemetryLogLineRunner : BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        val value = (node.getElementsByTagName("content").item(0) as Element).getElementsByTagName("expression").item(0)
        XMLCodeLine(value as Element).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        AdvancedLogging.logText(computedValue.toString())
        println(computedValue)
        AdvancedLogging.updateLog()
    }
}

class VariableSetRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        val name = node.getElementsByTagName("reference").item(0).textContent
        val value = (node.getElementsByTagName("value").item(0) as Element).getElementsByTagName("expression").item(0)
        XMLCodeLine(value as Element).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        env.variables[name]?.value = computedValue
    }
}

class VariableUsageRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        val name = node.textContent
        env.cacheValue1 = env.variables[name]?.value
    }
}

class BinaryExpressionRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        val left = node.getElementsByTagName("expression").item(0) as Element
        val right = node.getElementsByTagName("expression").item(1) as Element
        val operator = node.attributes.getNamedItem("operand").nodeValue

        XMLCodeLine(left as Element).runAction(env)
        val computedLeft = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: left
        env.resetCachedValues()

        XMLCodeLine(right as Element).runAction(env)
        val computedRight = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: right
        env.resetCachedValues()

        // Get the types of both the computed left and the computed right
        val leftType = computedLeft::class.simpleName
        val rightType = computedRight::class.simpleName

        // Try to parse the left and right as integers
        val leftInt = computedLeft.toString().toIntOrNull()
        val rightInt = computedRight.toString().toIntOrNull()

        // If both the left and right are integers, then we can do integer math
        if (leftInt != null && rightInt != null) {
            when (operator) {
                "+" -> env.cacheValue1 = leftInt + rightInt
                "-" -> env.cacheValue1 = leftInt - rightInt
                "*" -> env.cacheValue1 = leftInt * rightInt
                "/" -> env.cacheValue1 = leftInt / rightInt
                "%" -> env.cacheValue1 = leftInt % rightInt
                else -> throw Exception("Unknown operator $operator")
            }
        } else {
            // Otherwise, we can only do string concatenation
            when (operator) {
                "+" -> env.cacheValue1 = computedLeft.toString() + computedRight.toString()
                else -> throw Exception("Unknown operator $operator")
            }
        }
    }
}

class ParenthesisRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        val value = node.getElementsByTagName("expression").item(0)
        XMLCodeLine(value as Element).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        env.cacheValue1 = computedValue
    }
}

class ClearTelemetryRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        telemetry.clear()
        telemetry.update()
    }
}

class WaitRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        val value = node.getElementsByTagName("expression").item(0)
        XMLCodeLine(value as Element).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        HardwareGetter.opMode?.sleep(computedValue.toString().toLong())
    }
}

class MotorSetRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        val motor = node.getElementsByTagName("number").item(0).textContent.toInt()
        val value = node.getElementsByTagName("expression").item(0)
        XMLCodeLine(value as Element).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        val motorDevice = when (motor) {
            0 -> Devices.motor0
            1 -> Devices.motor1
            2 -> Devices.motor2
            3 -> Devices.motor3
            4 -> Devices.motor4
            5 -> Devices.motor5
            6 -> Devices.motor6
            7 -> Devices.motor7
            else -> throw Exception("Invalid motor number $motor")
        }

        motorDevice.power = computedValue.toString().toDouble()
    }
}

class FloatConstantRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Element) {
        env.cacheValue1 = node.textContent.toDouble()
    }
}

fun addAllRunners(xmlEvironment: XmlEvironment) {
    xmlEvironment.functions[XmlActionMap.VARIABLE_DECLARE] = VariableDeclarationRunner()
    xmlEvironment.functions[XmlActionMap.INT_CONSTANT] = IntegerConstantRunner()
    xmlEvironment.functions[XmlActionMap.TELEMETRY_LOG_LINE] = TelemetryLogLineRunner()
    xmlEvironment.functions[XmlActionMap.VARIABLE_SET] = VariableSetRunner()
    xmlEvironment.functions[XmlActionMap.VARIABLE_USAGE] = VariableUsageRunner()
    xmlEvironment.functions[XmlActionMap.BINARY_EXPRESSION] = BinaryExpressionRunner()
    xmlEvironment.functions[XmlActionMap.PARENTHESES] = ParenthesisRunner()
    xmlEvironment.functions[XmlActionMap.CLEAR_TELEMETRY] = ClearTelemetryRunner()
    xmlEvironment.functions[XmlActionMap.WAIT] = WaitRunner()
    xmlEvironment.functions[XmlActionMap.MOTOR_SET_POWER] = MotorSetRunner()
    xmlEvironment.functions[XmlActionMap.FLOAT_CONSTANT] = FloatConstantRunner()
}