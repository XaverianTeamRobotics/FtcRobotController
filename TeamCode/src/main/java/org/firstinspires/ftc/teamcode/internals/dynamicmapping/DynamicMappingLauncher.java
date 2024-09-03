package org.firstinspires.ftc.teamcode.internals.dynamicmapping;

import android.content.Context;
import org.firstinspires.ftc.ftccommon.external.OnCreate;

public class DynamicMappingLauncher {
    /**
     * This method must use java due to a limitation of how method modifiers work on kotlin.
     * The FIRST SDK doesn't properly analyze the method modifiers,
     * breaking kotlin's ability to use the OnCreate annotation.
     */
    @OnCreate
    public static void onCreate(Context context) {
        DynamicMappingManager.onCreate(context);
    }
}
