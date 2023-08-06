package org.knl.model;

import java.util.List;

public class Building {

    /**
     * For simplicity's sake, 0 will be bottom floor and -1 will be the basement
     **/
    private final int stories;

    private final Elevator publicElevator;

    private final Elevator freightElevator;
    private final List<Integer> lockedFloors;
    private int currentStory = 0;

    public Building(int stories, Elevator publicElevator, Elevator freightElevator, List<Integer> lockedFloors) {
        this.stories = stories;
        this.publicElevator = publicElevator;
        this.freightElevator = freightElevator;
        this.lockedFloors = lockedFloors;
    }

    public int getStories() {
        return stories;
    }

    public int getCurrentStory() {
        return currentStory;
    }

    public boolean goToStory(int story, int weightInKgs, boolean hasKeycard, String elevator) {
        Elevator solicitedElevator = getElevator(elevator);

        if (!solicitedElevator.isActive()) {
            System.out.println("""
                    This elevator is not active,
                    please select another one.
                    """);
            return false;
        }

        if (!isStoryAvailable(story)) {
            System.out.printf("""
                    Where are you trying to go?
                    This building only has %s stories
                    and a basement, located at floor -1.
                    """, stories);
            return false;
        }

        if (!solicitedElevator.canHandleWeight(weightInKgs)) {
            System.out.printf("""
                    WARNING!
                    WARNING!
                    Weight limit of %s exceeded.
                    Starting shutdown sequence.
                    """, solicitedElevator.getWeightLimitInKgs());
            solicitedElevator.setActive(false);
            return false;
        }

        if (!canAccessStory(solicitedElevator, hasKeycard, story)) {
            System.out.printf("""
                    Sorry, you need a keycard
                    in order to access floor %s.
                    """, story);
            return false;
        }

        currentStory = story;
        System.out.printf("""
                You are now on story %s.
                Returning to menu.
                """, currentStory);
        return true;
    }

    public boolean isStoryAvailable(int story) {
        return story >= -1 && story <= getStories() && story != getCurrentStory();
    }

    public boolean canAccessStory(Elevator elevator, boolean hasKeycard, int story) {

        if (!elevator.needsKeycard() || lockedFloors.isEmpty() || !lockedFloors.contains(story)) {
            return true;
        } else {
            return hasKeycard;
        }
    }

    public Elevator getElevator(String elevatorType) {
        return switch (elevatorType) {
            case "freight" -> freightElevator;
            default -> {
                if (publicElevator.isActive()) {
                    System.out.println("Unexpected value introduced, defaulting to public elevator: " + elevatorType);
                    yield publicElevator;
                }
                throw new IllegalStateException("No general-use elevators in service found, please come back later: " + elevatorType);
            }
        };
    }
}
