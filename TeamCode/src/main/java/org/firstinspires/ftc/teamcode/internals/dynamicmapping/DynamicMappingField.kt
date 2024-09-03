package org.firstinspires.ftc.teamcode.internals.dynamicmapping

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DynamicMappingField(val hardwareType: DynamicMappingHardware, val default: Int)
