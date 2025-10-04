package cpen.network.Requests;

import org.json.JSONObject;

import cpen.ADT.*;

/**
 * Represents a request that sends info to server for all the available
 * study sessions between the starting date and ending date,
 * with filtering based on the course.
 * <p>
 * This class implements {@link Request} and provides methods
 * for encoding the request and retrieving the type of request.
 * <p>
 * The {@code GetSessionsRequest} ensures:
 * - Request is valid when being constructed
 */
import javax.xml.stream.Location;
import java.util.Date;

public class GetSessionsRequest implements Request {
    RequestType requestType = RequestType.GET_SESSIONS;
    private User host = new User("", "");
    private String course = "";
    private String building = "";
    private DateTime startTime = null;
    private DateTime endTime = null;

    // Representation Invariant (RI):
    // - host, requester, course, room, startTime, endTime are not null

    // Abstraction Function (AF):
    // - Represents a request that sends info for all the available
    //      study sessions between the starting date and ending date, with course filtering.
    // - Request can also be encoded

    /**
     * Constructs a request that sends info to server for all the available
     * study sessions between the starting date and ending date,
     * with filtering based on the course.
     *
     * @param host      the host of a session
     * @param requester the user requesting to view the possible study sessions
     * @param course    the course
     * @param room      the room which a session is held
     * @param startTime the starting date to begin search
     * @param endTime   the ending date to end search
     */
    public GetSessionsRequest(User host) {
        this.host = host;
    }

    /**
     * Constructs a request that sends info to server for all the available
     * study sessions between the starting date and ending date,
     * with filtering based on the course.
     *
     * @param course    the course
     * @param room      the room which a session is held
     * @param startTime the starting date to begin search
     * @param endTime   the ending date to end search
     */
    public GetSessionsRequest(String course, String building, DateTime startTime, DateTime endTime) {
        this.course = course;
        this.building = building;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets a string that expresses all the necessary information to make a request
     *
     * @return the encoded version of the request information
     */
    @Override
    public String string() {
        JSONObject json = new JSONObject();
        json.put("requestType", requestType.name());
        json.put("hostUser", host.getUsername());

        json.put("building", building);

        json.put("course", course);

        if (startTime != null && endTime != null) {
            json.put("startDate", startTime.getDateTimeString(DateTimeFormat.YYYY_MM_DD));
            json.put("endDate", endTime.getDateTimeString(DateTimeFormat.YYYY_MM_DD));
        } else {
            json.put("startDate", "");
            json.put("endDate", "");
        }

        return json.toString();
    }

    /**
     * Extract the request's type
     *
     * @return the type of the request
     */
    @Override
    public RequestType getRequestType() {
        return this.requestType;
    }
}
