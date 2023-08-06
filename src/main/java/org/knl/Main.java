package org.knl;

import org.apache.commons.lang3.math.NumberUtils;
import org.knl.model.Building;
import org.knl.model.ElevatorType;
import org.knl.model.FreightElevator;
import org.knl.model.PublicElevator;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Welcome to the building sim!
                                    
                Please, let's first create your dream building.
                """);
        System.out.println("""
                How many stories does your building have?
                """);

        int stories = getStories(scanner);

        System.out.println("""
                Good! Now, does your building have floors accessible only
                from the freight elevator and to people with keycards?
                If so, what are the numbers of those floors?
                Please introduce a comma-separated string of values.
                """);

        List<Integer> lockedFloors = getLockedFloors(scanner, stories);

        System.out.println("""
                Next question!
                What are the maximum values that your public and freight elevators allow, respectively?
                Please introduce two comma-separated numbers in kilograms for the value.
                The first one will be the public elevators' weight limit, while the second one will be the same for freight's
                """);

        int[] weights = getWeights(scanner);

        System.out.println("""
                We're almost there!
                Last question: do you have a keycard to access the aforementioned floors?
                Please, answer either true or false.
                """);

        boolean hasKeycard = isHasKeycard(scanner);

        System.out.println("Now constructing the building, please wait");

        FreightElevator freightElevator = new FreightElevator(true, weights[1], ElevatorType.FREIGHT, true);

        PublicElevator publicElevator = new PublicElevator(false, weights[0], ElevatorType.PUBLIC, true);

        Building building = new Building(stories, publicElevator, freightElevator, lockedFloors);

        System.out.println("Finished creating the building, now you can start exploring the stories");

        while (true) {
            System.out.println("""
                    Access menu for the building:
                    1 - Go to a specific floor
                    2 - Update keycard status
                    9 - Exit
                    """);

            scanner.nextLine();
            int option = scanner.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.println("""
                            Please introduce the floor you want to go to
                            """);
                    scanner.nextLine();
                    int floor = scanner.nextInt();
                    System.out.println("""
                            Please introduce the total weight in kgs of the object that will be transported on the elevator
                            """);
                    scanner.nextLine();
                    int weight = scanner.nextInt();
                    System.out.println("""
                            Please introduce the type of elevator you will be taking to go to that floor.
                            Currently, only 'freight' and 'public' elevators are available
                            """);
                    scanner.nextLine();
                    String type = scanner.next();

                    building.goToStory(floor, weight, hasKeycard, type);
                }
                case 2 -> {
                    System.out.println("""
                            Updating keycard possession status.
                            Please, answer either true or false.
                            """);
                    scanner.nextLine();
                    hasKeycard = scanner.nextBoolean();
                }
                case 9 -> {
                    System.out.println("""
                            Thanks for coming to the building!
                            Have a safe travel home!
                            """);
                    return;
                }
            }
        }
    }

    private static boolean isHasKeycard(Scanner scanner) {
        boolean hasKeycard;
        try {
            hasKeycard = scanner.nextBoolean();
        } catch (Exception e) {
            System.out.println("""
                    Incorrect value used as input, defaulting to false.
                    Only 'true' or 'false' values are allowed.
                    Please try again
                    """);
            hasKeycard = false;
        }
        return hasKeycard;
    }

    private static int[] getWeights(Scanner scanner) {
        String weightsStr = scanner.nextLine();

        int[] weights = Arrays.stream(weightsStr.split(","))
                .map(String::strip)
                .filter(NumberUtils::isCreatable)
                .mapToInt(Integer::valueOf)
                .filter(value -> value > 0)
                .toArray();
        while (weights.length != 2) {
            System.out.println("""
                    There where more (or less) values introduced than 2.
                    Only non-negative, comma-separated values are allowed.
                    Please try again.
                    """);
            weightsStr = scanner.nextLine();

            weights = Arrays.stream(weightsStr.split(","))
                    .map(String::strip)
                    .filter(NumberUtils::isCreatable)
                    .mapToInt(Integer::valueOf)
                    .toArray();
        }
        return weights;
    }

    private static List<Integer> getLockedFloors(Scanner scanner, int stories) {
        scanner.nextLine();
        String lockedFloorsStr = scanner.nextLine();

        List<Integer> lockedFloors = Arrays.stream(lockedFloorsStr.split(","))
                .map(String::strip)
                .filter(NumberUtils::isCreatable)
                .map(Integer::valueOf)
                .toList();

        while (lockedFloors.stream().min(Integer::compareTo).get() < -1 &&
                lockedFloors.stream().max(Integer::compareTo).get() >= stories) {
            System.out.println("""
                    Some of the floors you introduced are invalid.
                    Only non-negative (other than -1, the basement), comma-separated values are allowed.
                    Please try again.
                    """);
            lockedFloorsStr = scanner.nextLine();

            lockedFloors = Arrays.stream(lockedFloorsStr.split(","))
                    .map(String::strip)
                    .filter(NumberUtils::isCreatable)
                    .map(Integer::valueOf)
                    .toList();
        }
        return lockedFloors;
    }

    private static int getStories(Scanner scanner) {
        int stories = scanner.nextInt();

        while (stories <= 1) {
            System.out.println("""
                    Please insert a number greater or equal to one.
                    """);
            stories = scanner.nextInt();
        }
        return stories;
    }
}