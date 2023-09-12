package org.firstinspires.ftc.teamcode.internals.registration.xml.action_runners

import org.firstinspires.ftc.teamcode.internals.registration.xml.XmlEvironment
import org.w3c.dom.Element

interface BaseRunner {
    fun run(env: XmlEvironment, node: Element)
}