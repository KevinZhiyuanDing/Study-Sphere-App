// Move to cpen/adt/Room.java
package cpen.adt;

import cpen.ADT.DateTime;

/**
 * Represents a Room with specific building, room number, a time frame,
 */
public class Room {
    private String building;
    private String roomNumber;
    private DateTime startTime;
    private DateTime endTime;
    private String url;

    /**
     * Constructs a Room with specified building, room number, start time, and end time.
     *
     * @param building the abbreviated name of the building where the room is located
     * @param roomNumber the room number of the room within the building
     * @param startTime the start time for when the room is available
     * @param endTime the end time for when the room is available
     */
    public Room(String building, String roomNumber, DateTime startTime, DateTime endTime, String url) {
        this.building = building;
        this.roomNumber = roomNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.url = url;
    }

    /**
     * Gets the building name of where the room is located.
     *
     * @return the building name as a String
     */
    public String getBuilding() {
        return building;
    }

    /**
     * Sets the building name where the room is located.
     *
     * @param building the building name
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * Gets the room number of the room
     *
     * @return  the room number of the room
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Sets the room number of the room.
     *
     * @param roomNumber the room number to set
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Gets the start time for when the room is available
     *
     * @return the start time as a DateTime object
     */
    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time for when the room is available.
     *
     * @param startTime the start time to set as a DateTime object
     */
    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time for when the room is available.
     *
     * @return the end time as a DateTime object
     */
    public DateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time for when the room is available
     *
     * @param endTime the end time to set as a DateTime object
     */
    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the URL that leads to the book site of the Room.
     *
     * @return the URL as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL that leads to the book site of the Room.
     *
     * @param url the URL to be set for the room
     */
    public void setUrl(String url) {
        this.url = url;
    }
}

