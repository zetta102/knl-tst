package org.knl.model;

public class FreightElevator implements Elevator {
    final boolean needsKeycard;
    final int weightLimitInKgs;
    final ElevatorType elevatorType;
    boolean isActive;

    public FreightElevator(boolean needsKeycard, int weightLimitInKgs, ElevatorType elevatorType, boolean isActive) {
        this.needsKeycard = needsKeycard;
        this.weightLimitInKgs = weightLimitInKgs;
        this.elevatorType = elevatorType;
        this.isActive = isActive;
    }

    @Override
    public boolean needsKeycard() {
        return needsKeycard;
    }

    @Override
    public int getWeightLimitInKgs() {
        return weightLimitInKgs;
    }

    @Override
    public ElevatorType getElevatorType() {
        return elevatorType;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean toggleElevator() {
        isActive = !isActive;
        System.out.printf("""
                The elevator is now
                %s""", (isActive ? "ACTIVE" : "INACTIVE"));
        return isActive;
    }

    @Override
    public boolean canHandleWeight(int weightInKgs) {
        return weightInKgs <= getWeightLimitInKgs();
    }
}
