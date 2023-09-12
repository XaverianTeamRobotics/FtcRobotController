package org.firstinspires.ftc.teamcode.internals.registration.xml

import org.w3c.dom.Element

class XMLCodeLine(val node: Element) {
    fun runAction(env: XmlEvironment) {
        env.functions[XmlActionMap.mapFromMPSString(node.attributes.getNamedItem("type").nodeValue)]?.run(env, node)
    }
}