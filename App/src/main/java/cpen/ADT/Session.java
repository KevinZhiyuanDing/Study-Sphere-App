// Move to cpen/adt/Session.java
package cpen.ADT;

import java.util.ArrayList;
import cpen.ADT.DateTime;
import cpen.ADT.User;

/**
 * Represents a study session scheduled for a particular course and room at a specific time.
 */
public class Session {
    private User host;
    private String building;
    private String course;
    private String room;
    private DateTime startTime;
    private DateTime endTime; // startTime.date = endTime.date, endTime.time > startTime.time
    private String description; // user will give a text description of study session
    private ArrayList<User> members = new ArrayList<>();
    private ArrayList<User> requesters = new ArrayList<>(); // list of users that request to join the study session

    /**
     * Constructs a new Session with specified parameters.
     *
     * @param host        The user who is hosting the session
     * @param course      The course associated with the session
     * @param room        The room where the session will be held
     * @param startTime   The start time of the session
     * @param endTime     The end time of the session
     * @param description A host provided description of the session
     */
    public Session(User host, String building, String course, String room, DateTime startTime, DateTime endTime,
                   String description) {
        this.host = host;
        this.course = course;
        this.room = room;
        this.building = building;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    /**
     * Gets the host of the session.
     *
     * @return the User object representing the host of the session
     */
    public User getHost() {
        return host;
    }

    /**
     * Sets the host of the session.
     *
     * @param host the host of the session
     */
    public void setHost(User host) {
        this.host = host;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the session as a DateTime object
     *
     * @param startTime the start time of the session
     */
    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the session as a DateTime object
     *
     * @return the end time of the session
     */
    public DateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the session as a DateTime object
     *
     * @param endTime the start time of the session
     */
    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the description of the session.
     *
     * @return the description of the session as a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the session.
     *
     * @param description the description of the session as a String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the list of members in the session.
     *
     * @return an ArrayList of Users representing the members of the session
     */
    public ArrayList<User> getMembers() {
        return members;
    }

    /**
     * Sets the list of members for the session.
     *
     * @param members an ArrayList of Users representing the members to be set for the session
     */
    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    /**
     * Gets the list of users who have requested to join the session.
     *
     * @return an ArrayList of Users representing the requesters of the session
     */
    public ArrayList<User> getRequesters() {
        return requesters;
    }

    /**
     * Sets the list of users who have requested to join the session.
     *
     * @param requesters an ArrayList of Users representing the requesters to be set for the session
     */
    public void setRequesters(ArrayList<User> requesters) {
        this.requesters = requesters;
    }

    public String getCourse() {
        return course;
    }

    public String getRoom() {
        return room;
    }

    public String getBuilding() {
        return building;
    }
}