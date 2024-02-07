package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.features.Hand
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

@Disabled
class HandTest : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(Hand())
    }

    override fun run() {
    }
}
