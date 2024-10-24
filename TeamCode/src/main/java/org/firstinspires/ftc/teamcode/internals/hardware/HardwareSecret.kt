package org.firstinspires.ftc.teamcode.internals.hardware

object HardwareSecret {
    /**
     * !!!WARNING!!!
     *
     * This secret key is used by the internal classes to prevent accidentally tampering with sensitive code.
     * This should only be used by the internal classes. Most operations that require this key are advanced and will
     * have side effects if used incorrectly.
     */
    internal const val secret = "whopperwhopperwhopperwhopper"

    class SecretException : Exception("An invalid secret was used")
}