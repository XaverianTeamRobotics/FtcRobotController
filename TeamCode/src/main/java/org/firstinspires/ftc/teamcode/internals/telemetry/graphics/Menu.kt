package org.firstinspires.ftc.teamcode.internals.telemetry.graphics

import com.qualcomm.robotcore.util.Range

/**
 * A Menu is a collection of choices which appear on the telemetry logger. A Menu is designed to be managed by another class, which sends input to the menu and displays the menu on the logger.
 */
class Menu private constructor(builder: MenuBuilder) {
    private val ITEMS = builder.ITEMS
    private val DESCRIPTION = builder.description
    private var selected = 0
    private val SPACER = "————————————————————————————————————"

    /**
     * Moves the selected item up by 1 or down by 1. If it's at either end of the list and you ask it to go beyond the list's bounds, nothing will happen. Whatever manages this menu is responsible for determining when this should occur.
     * @param direction The direction, up or down. Up is positive, down is negative.
     */
    fun selectItem(direction: Boolean) {
        val dir = if (direction) -1 else 1
        selected = Range.clip(selected + dir, 0, ITEMS.size - 1)
    }

    /**
     * "Clicks" the selected item. Whatever manages this menu is responsible for clearing the logger and processing the clicked item.
     */
    fun clickSelectedItem(): Item {
        return ITEMS[selected]
    }

    /**
     * "Draws" the menu. Whatever manages this menu is responsible for printing this in the logger.
     */
    fun draw(): String {
        val builder = StringBuilder()
        // add the description
        builder.append(DESCRIPTION).append(System.lineSeparator()).append(SPACER).append(System.lineSeparator())
        // add the items--and identify which item is selected and reflect that in the UI
        for (i in ITEMS.indices) {
            val item = ITEMS[i]
            if (i == selected) {
                builder.append("⇨ ").append(item.name).append(System.lineSeparator())
            } else {
                builder.append("  ").append(item.name).append(System.lineSeparator())
            }
        }
        // add hints
        builder.append(SPACER).append(System.lineSeparator()).append("✜ Navigate                   Select Ⓐ")
        return builder.toString()
    }

    /**
     * Builds a [Menu].
     */
    class MenuBuilder {
        internal val ITEMS: ArrayList<Item> = ArrayList()
        var description: String? = null
            internal set

        /**
         * Adds an [Item], or choice, to this menu.
         */
        fun addItem(item: Item): MenuBuilder {
            ITEMS.add(item)
            return this
        }

        /**
         * Adds an [Item], or choice, to this menu represented by a [String]. This [String] will be used to build the [Item] supplied to the menu.
         */
        fun addItem(item: String?): MenuBuilder {
            ITEMS.add(Item(item!!))
            return this
        }

        /**
         * Sets the description of this menu. The description is displayed at the top of the menu above the choices and should indicate to the user what the choices in this menu mean and why they're important.
         */
        fun setDescription(description: String?): MenuBuilder {
            this.description = description
            return this
        }

        fun build(): Menu {
            return Menu(this)
        }
    }
}
