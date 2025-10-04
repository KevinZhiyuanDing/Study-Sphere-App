package cpen.network.Requests;

import org.json.JSONObject;

import cpen.ADT.DateTimeFormat;
import cpen.ADT.Session;
import cpen.ADT.User;

/**
 * Represents a request that sends info to server on whether host accepts or rejects
 * a requesters request to join study session.
 * <p>
 * This class implements {@link Request} and provides methods
 * for encoding the request and retrieving the type of request.
 * <p>
 * The {@code AcceptSessionRequest} ensures:
 * - Request is valid when being constructed
 */
public class AcceptSessionRequest implements Request {
    private final RequestType requestType = RequestType.ACCEPT_REQUEST;
    private final User host;
    private final User requester;
    private final Session session;
    private final boolean acceptRequest;

    // Representation Invariant (RI):
    // - host, requester, session are not null

    // Abstraction Function (AF):
    // - Represents a request that sends info on whether host accepts/rejects a requesters request
    //      to join study session
    // - Request can also be encoded

    /**
     * Constructs a request that sends info to server on whether host accepts or rejects
     * a requesters request to join study session.
     *
     * @param host          the host of the session
     * @param requester     the user requesting to join the session
     * @param session       the session
     * @param acceptRequest whether the host accepts/rejects requester request
     */
    public AcceptSessionRequest(User host, User requester, Session session, boolean acceptRequest) {
        if (host == null || requester == null || session == null) {
            throw new IllegalArgumentException("Host, requester, and session cannot be null.");
        }

        this.host = host;
        this.requester = requester;
        this.session = session;
        this.acceptRequest = acceptRequest;
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
        json.put("hostPass", host.getPassword());

        json.put("requestUser", requester.getUsername());
        json.put("requestPass", requester.getPassword());

        json.put("course", session.getCourse());

        json.put("building", session.getBuilding());
        json.put("roomNum", session.getRoom());
        json.put("startTime", session.getStartTime().getDateTimeString(DateTimeFormat.YYYY_MM_DD));
        json.put("endTime", session.getEndTime().getDateTimeString(DateTimeFormat.YYYY_MM_DD));
        json.put("sessionDesc", session.getDescription());

        json.put("accept", acceptRequest);

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
