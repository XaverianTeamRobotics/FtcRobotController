package org.firstinspires.ftc.teamcode.internals.motion

import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoNoNavigationZones
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.BestPathFinder
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
fun initializeAutoAuto() {
    Logging.setAutoClear(false)
    Logging.clear()
    val time = System.currentTimeMillis()

    Logging.log("Initializing Auto Auto - Started")
    Logging.update()

    AutoNoNavigationZones.addCenterstageDefaults()
    BestPathFinder.populate()

    Logging.log(
        "\n\nAuto Auto Initialized: Overall Time - ${((System.currentTimeMillis() - time) / 1000).toInt()}s"
    )
    Logging.update()

    Logging.setAutoClear(true)
}
