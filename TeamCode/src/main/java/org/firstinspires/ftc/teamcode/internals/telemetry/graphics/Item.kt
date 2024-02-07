package org.firstinspires.ftc.teamcode.internals.telemetry.graphics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * An Item is a choice in a {@link Menu}.
 */
public class Item {

    private final String NAME;

    /**
     * Creates an item.
     * @param name The name of the item, or what will be displayed on the menu for this item.
     */
    public Item(String name) {
        NAME = name;
    }

    public String getName() {
        return NAME;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return getName() != null ? getName() : "NULL";
    }

    /**
     * Returns true if the object specified is equal to this object.
     * <br>
     * <br>
     * Note that if the object passed to this method is a {@link String}, the value of the object will be compared to {@link #getName()}, while all other types will be compared by the {@link Object#equals(Object)} method.
     */
    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if(obj != null) {
            if(String.class.isAssignableFrom(obj.getClass())) {
                return obj.equals(getName());
            }else{
                return super.equals(obj);
            }
        }else{
            return false;
        }
    }
}
