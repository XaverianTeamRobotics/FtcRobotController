package org.firstinspires.ftc.teamcode.internals.telemetry.graphics

/**
 * An Item is a choice in a [Menu].
 */
class Item
/**
 * Creates an item.
 * @param name The name of the item, or what will be displayed on the menu for this item.
 */(val name: String) {
    override fun toString(): String {
        return name ?: "NULL"
    }

    /**
     * Returns true if the object specified is equal to this object.
     * <br></br>
     * <br></br>
     * Note that if the object passed to this method is a [String], the value of the object will be compared to [.getName], while all other types will be compared by the [Object.equals] method.
     */
    override fun equals(obj: Any?): Boolean {
        return if (obj != null) {
            if (String::class.java.isAssignableFrom(obj.javaClass)) {
                obj == name
            } else {
                super.equals(obj)
            }
        } else {
            false
        }
    }
}
