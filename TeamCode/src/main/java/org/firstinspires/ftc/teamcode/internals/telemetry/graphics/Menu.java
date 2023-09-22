package org.firstinspires.ftc.teamcode.internals.telemetry.graphics;

import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;

/**
 * A Menu is a collection of choices which appear on the telemetry logger. A Menu is designed to be managed by another class, which sends input to the menu and displays the menu on the logger.
 */
public class Menu {

    private final ArrayList<Item> ITEMS;
    private final String DESCRIPTION;
    private int selected = 0;
    private final String SPACER = "——————————————————————————————————————";

    private Menu(MenuBuilder builder) {
        ITEMS = builder.ITEMS;
        DESCRIPTION = builder.description;
    }

    /**
     * Moves the selected item up by 1 or down by 1. If it's at either end of the list and you ask it to go beyond the list's bounds, nothing will happen. Whatever manages this menu is responsible for determining when this should occur.
     * @param direction The direction, up or down. Up is positive, down is negative.
     */
    public void selectItem(boolean direction) {
        int dir = direction ? -1 : 1;
        selected = Range.clip(selected + dir, 0, ITEMS.size() - 1);
    }

    /**
     * "Clicks" the selected item. Whatever manages this menu is responsible for clearing the logger and processing the clicked item.
     */
    public Item clickSelectedItem() {
        return ITEMS.get(selected);
    }

    /**
     * "Draws" the menu. Whatever manages this menu is responsible for printing this in the logger.
     */
    public String draw() {
        StringBuilder builder = new StringBuilder();
        // add the description
        builder.append(DESCRIPTION).append(System.lineSeparator()).append(SPACER).append(System.lineSeparator());
        // add the items--and identify which item is selected and reflect that in the UI
        for(int i = 0; i < ITEMS.size(); i++) {
            Item item = ITEMS.get(i);
            if(i == selected) {
                builder.append("⇨ ").append(item.getName()).append(System.lineSeparator());
            }else{
                builder.append("  ").append(item.getName()).append(System.lineSeparator());
            }
        }
        // add hints
        builder.append(SPACER).append(System.lineSeparator()).append("✜ Navigate                   Select Ⓐ");
        return builder.toString();
    }

    /**
     * Builds a {@link Menu}.
     */
    public static class MenuBuilder {

        private final ArrayList<Item> ITEMS = new ArrayList<>();
        private String description;

        /**
         * Adds an {@link Item}, or choice, to this menu.
         */
        public MenuBuilder addItem(Item item) {
            ITEMS.add(item);
            return this;
        }

        /**
         * Adds an {@link Item}, or choice, to this menu represented by a {@link String}. This {@link String} will be used to build the {@link Item} supplied to the menu.
         */
        public MenuBuilder addItem(String item) {
            ITEMS.add(new Item(item));
            return this;
        }

        /**
         * Sets the description of this menu. The description is displayed at the top of the menu above the choices and should indicate to the user what the choices in this menu mean and why they're important.
         */
        public MenuBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }

    }

}
