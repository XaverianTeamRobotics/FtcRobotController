package org.firstinspires.ftc.teamcode.internals.registration.xml

import org.firstinspires.ftc.teamcode.internals.registration.xml.action_runners.BaseRunner
import org.firstinspires.ftc.teamcode.internals.registration.xml.action_runners.addAllRunners

class XmlEvironment {
    fun init() {
        addAllRunners(this)
    }

    val variables = mutableMapOf<String, XmlVariable<Any>>()
    val functions = mutableMapOf<XmlActionMap, BaseRunner>()
    var cacheValue1: Any? = null
    var cacheValue2: Any? = null
    var cacheValue3: Any? = null
    var cacheValue4: Any? = null

    fun resetCachedValues() {
        cacheValue1 = null
        cacheValue2 = null
        cacheValue3 = null
        cacheValue4 = null
    }
}
