package org.knl.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knl.model.*;

import java.util.Arrays;
import java.util.List;

public class BuildingTest {
    private Building building;
    private Elevator publicElevator;
    private Elevator freightElevator;

    @BeforeEach
    public void setup() {
        publicElevator = new PublicElevator(false, 1000, ElevatorType.PUBLIC, true);
        freightElevator = new FreightElevator(true, 2000, ElevatorType.FREIGHT, true);

        List<Integer> lockedFloors = Arrays.asList(1, 2, 3);

        building = new Building(10, publicElevator, freightElevator, lockedFloors);
    }

    @Test
    public void testGoToStory_PublicElevator() {
        boolean result = building.goToStory(10, 800, false, "public");
        Assertions.assertTrue(result);
        Assertions.assertEquals(10, building.getCurrentStory());
    }

    @Test
    public void testGoToStory_FreightElevatorWithKeycard() {
        boolean result = building.goToStory(10, 1500, true, "freight");
        Assertions.assertTrue(result);
        Assertions.assertEquals(10, building.getCurrentStory());
    }

    @Test
    public void testGoToStory_FreightElevatorWithoutKeycard() {
        boolean result = building.goToStory(2, 1500, false, "freight");
        Assertions.assertFalse(result);
        Assertions.assertEquals(0, building.getCurrentStory());
    }

    @Test
    public void testGoToStory_NonexistentFloor() {
        boolean result = building.goToStory(20, 500, true, "public");
        Assertions.assertFalse(result);
        Assertions.assertEquals(0, building.getCurrentStory());
    }

    @Test
    public void testGoToStory_ExceedWeightLimit() {
        boolean result = building.goToStory(3, 2500, true, "freight");
        Assertions.assertFalse(result);
        Assertions.assertEquals(0, building.getCurrentStory());
        Assertions.assertFalse(freightElevator.isActive());
    }

    @Test
    public void testGoToStory_LockedFloorWithKeycard() {
        boolean result = building.goToStory(-1, 700, true, "freight");
        Assertions.assertTrue(result);
        Assertions.assertEquals(-1, building.getCurrentStory());
    }

    @Test
    public void testGoToStory_LockedFloorWithoutKeycard() {
        boolean result = building.goToStory(50, 700, false, "freight");
        Assertions.assertFalse(result);
        Assertions.assertEquals(0, building.getCurrentStory());
    }

    @Test
    public void testIsStoryAvailable_ValidFloor() {
        boolean result = building.isStoryAvailable(5);
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsStoryAvailable_NegativeFloor() {
        boolean result = building.isStoryAvailable(-2);
        Assertions.assertFalse(result);
    }

    @Test
    public void testIsStoryAvailable_OutOfRangeFloor() {
        boolean result = building.isStoryAvailable(15);
        Assertions.assertFalse(result);
    }

    @Test
    public void testGoToStory_NoGeneralUseElevatorsAvailable() {
        publicElevator.setActive(false);
        freightElevator.setActive(false);

        Assertions.assertThrows(IllegalStateException.class,
                () -> building.goToStory(3, 500, true, "public"));
    }

    @Test
    public void testCanAccessStory_NoKeycardNeeded() {
        boolean result = building.canAccessStory(publicElevator, false, 3);
        Assertions.assertTrue(result);
    }

    @Test
    public void testCanAccessStory_KeycardRequired_FloorAccessible() {
        boolean result = building.canAccessStory(freightElevator, true, 3);
        Assertions.assertTrue(result);
    }

    @Test
    public void testCanAccessStory_KeycardRequired_FloorNotAccessible() {
        boolean result = building.canAccessStory(freightElevator, false, 3);
        Assertions.assertFalse(result);
    }

    @Test
    public void testGetElevator_PublicElevator() {
        Elevator elevator = building.getElevator("public");
        Assertions.assertEquals(publicElevator, elevator);
    }

    @Test
    public void testGetElevator_FreightElevator() {
        Elevator elevator = building.getElevator("freight");
        Assertions.assertEquals(freightElevator, elevator);
    }

    @Test
    public void testGetElevator_UnknownElevatorType_DefaultToPublic() {
        Elevator elevator = building.getElevator("unknown");
        Assertions.assertEquals(publicElevator, elevator);
    }

    @Test
    public void testIsStoryAvailable_CurrentFloor() {
        boolean result = building.isStoryAvailable(0);
        Assertions.assertFalse(result);
    }

    @Test
    public void testIsStoryAvailable_Basement() {
        boolean result = building.isStoryAvailable(-1);
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsStoryAvailable_InvalidFloor() {
        boolean result = building.isStoryAvailable(11);
        Assertions.assertFalse(result);
    }

    @Test
    public void testGoToStory_InvalidWeight() {
        boolean result = building.goToStory(4, 2500, true, "public");
        Assertions.assertFalse(result);
        Assertions.assertEquals(0, building.getCurrentStory());
    }
}
