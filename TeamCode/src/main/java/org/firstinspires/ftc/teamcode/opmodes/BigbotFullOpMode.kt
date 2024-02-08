package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.features.Hand
import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain
import org.firstinspires.ftc.teamcode.features.PowerplayTwoMotorArm
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

@Disabled
class BigbotFullOpMode : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(
            NativeMecanumDrivetrain(
                DrivetrainMapMode.FR_BR_FL_BL,
                false, true,
                false
            )
        )
        registerFeature(PowerplayTwoMotorArm())
        registerFeature(Hand())
    }

    override fun run() {
    }
}
