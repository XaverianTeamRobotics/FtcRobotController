package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

public class AutoAutoCreatorConfig {
    /**
     * The color of the team. 0 is blue, 1 is red.
     * -1 is invalid.
     */
    private int teamColor = -1;

    /**
     * Starting position ID. 0 is left, 1 is right.
     * -1 is invalid.
     */
    private int startingPosition = -1;

    /**
     * Whether to place a pixel in the backdrop
     */
    private boolean placeBackdrop = false;

    /**
     * On the backdrop, which pixel position are we going to score on?
     * 0 for left, 1 for right.
     * -1 is invalid.
     */
    private int backdropPixelPosition = -1;

    /**
     * Whether to place a pixel on the spike mark
     */
    private boolean placeSpikeMark = false;

    /**
     * The place to park. 0 is left of backdrop, 1 is in front of backdrop, 2 is right of backdrop, 3 is starting point.
     * -1 is invalid.
     */
    private int parkPlace = -1;

    public AutoAutoCreatorConfig() {}

    public AutoAutoCreatorConfig(int teamColor, int startingPosition, boolean placeBackdrop, int backdropPixelPosition, boolean placeSpikeMark, int parkPlace) {
        this.teamColor = teamColor;
        this.startingPosition = startingPosition;
        this.placeBackdrop = placeBackdrop;
        this.backdropPixelPosition = backdropPixelPosition;
        this.placeSpikeMark = placeSpikeMark;
        this.parkPlace = parkPlace;
    }

    public void askQuestions() {
        MenuManager teamColorMenu = Questions.askAsyncC1("What is your team color?", "Blue", "Red");
        MenuManager startingPositionMenu = Questions.askAsyncC1("Where is your bot starting?", "Left", "Right");
        String purpleAnswer = "Purple (spike mark)"; // Declare these two to prevent spelling errors later
        String yellowAnswer = "Yellow (backdrop)";
        MenuManager pixelPlaceLocationsMenu = Questions.askAsyncC1("What pixels are loaded onto the bot?", "Neither", purpleAnswer, yellowAnswer, "Both");
        MenuManager backdropPixelPositionMenu = Questions.askAsyncC1("Where would you like to place the backdrop pixel within the place designated by the team prop?", "Left", "Right");
        String leftAnswer = "Left of Backdrop";
        String middleAnswer = "Middle of Backdrop";
        String rightAnswer = "Right of Backdrop";
        String startingPositionAnswer = "Starting Position";
        MenuManager parkPlaceMenu = Questions.askAsyncC1("Where should we park after scoring?", leftAnswer, middleAnswer, rightAnswer, startingPositionAnswer);

        teamColor = teamColorMenu.run().toString().equals("Blue") ? 0 : 1;
        startingPosition = startingPositionMenu.run().toString().equals("Left") ? 0 : 1;

        String pixelPlaceLocationsAnswer = pixelPlaceLocationsMenu.run().toString();
        if (pixelPlaceLocationsAnswer.equals(purpleAnswer) || pixelPlaceLocationsAnswer.equals("Both")) placeSpikeMark = true;
        if (pixelPlaceLocationsAnswer.equals(yellowAnswer) || pixelPlaceLocationsAnswer.equals("Both")) placeBackdrop = true;

        if (placeBackdrop)
            backdropPixelPosition = backdropPixelPositionMenu.run().toString().equals("Left") ? 0 : 1;

        String parkPlaceAnswer = parkPlaceMenu.run().toString();
        if (parkPlaceAnswer.equals(leftAnswer)) parkPlace = 0;
        if (parkPlaceAnswer.equals(middleAnswer)) parkPlace = 1;
        if (parkPlaceAnswer.equals(rightAnswer)) parkPlace = 2;
        if (parkPlaceAnswer.equals(startingPositionAnswer)) parkPlace = 3;
    }

    public int getTeamColor() {
        return teamColor;
    }

    public int getStartingPosition() {
        return startingPosition;
    }

    public boolean getPlaceBackdrop() {
        return placeBackdrop;
    }

    public int getBackdropPixelPosition() {
        return backdropPixelPosition;
    }

    public boolean getPlaceSpikeMark() {
        return placeSpikeMark;
    }

    public int getParkPlace() {
        return parkPlace;
    }
}
