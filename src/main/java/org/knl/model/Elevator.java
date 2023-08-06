package org.knl.model;

public interface Elevator {

    boolean needsKeycard();

    int getWeightLimitInKgs();

    ElevatorType getElevatorType();

    boolean isActive();

    void setActive(boolean active);

    boolean toggleElevator();

    boolean canHandleWeight(int weightInKgs);
}
