package cpen.network.Requests;

import org.json.JSONObject;

import cpen.adt.DateTimeFormat;
import cpen.adt.Session;
import cpen.adt.User;

/**
 * Represents a request that sends info to server.
 * The information sent will be used to create a study session.
 * <p>
 * This class implements {@link Request} and provides methods
 * for encoding the request and retrieving the type of request.
 * <p>
 * The {@code CreateSessionRequest} ensures:
 * - Request is valid when being constructed
 */
public class CreateSessionRequest implements Request {
    RequestType requestType = RequestType.CREATE_SESSION;
    private final Session session;

    // Representation Invariant (RI):
    // - requester, session are not null

    // Abstraction Function (AF):
    // - Represents a request that sends info to server.
    //      The information sent will be used to create a study session.
    // - Request can also be encoded

    /**
     * Constructs a request that sends info to server.
     * The information sent will be used to create a study session.
     *
     * @param requester the user requesting to create a session
     * @param session   the session
     */
    public CreateSessionRequest(Session session) {
        this.session = session;
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

        json.put("hostUser", session.getHost().getUsername());

        json.put("course", session.getCourse());

        json.put("building", session.getBuilding());
        json.put("roomNum", session.getRoom());
        json.put("startTime", session.getStartTime().getDateTimeString(DateTimeFormat.YYYY_MM_DD));
        json.put("endTime", session.getEndTime().getDateTimeString(DateTimeFormat.YYYY_MM_DD));

        json.put("sessionDesc", session.getDescription());

        return json.toString();
    }

    /**
     * Extract the request's type
     *
     * @return the type of the request
     */
    @Override
    public RequestType getRequestType() {
        return requestType;
    }
}