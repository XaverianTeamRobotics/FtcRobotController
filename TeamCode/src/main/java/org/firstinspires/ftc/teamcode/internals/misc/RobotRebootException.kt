package org.firstinspires.ftc.teamcode.internals.misc

class RobotRebootException : RuntimeException {
    constructor() : super()

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)
}
