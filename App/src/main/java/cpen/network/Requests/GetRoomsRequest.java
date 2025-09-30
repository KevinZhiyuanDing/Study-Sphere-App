package cpen.network.Requests;

import org.json.JSONObject;

import cpen.adt.DateTime;
import cpen.adt.DateTimeFormat;

/**
 * Represents a request that sends info to server for all the available
 * empty rooms between the starting date and ending date.
 * <p>
 * This class implements {@link Request} and provides methods
 * for encoding the request and retrieving the type of request.
 * <p>
 * The {@code GetRoomsRequest} ensures:
 * - Request is valid when being constructed
 */
public class GetRoomsRequest implements Request {
    RequestType requestType = RequestType.GET_ROOMS;
    String building;
    DateTime startDate;
    DateTime endDate;

    // Representation Invariant (RI):
    // - building, starting date, ending date are not null

    // Abstraction Function (AF):
    // - Represents a request that sends info for all the available
    //      empty rooms between the starting date and ending date.
    // - Request can also be encoded

    /**
     * Constructs a request that sends info to server for all the available
     * empty rooms between the starting date and ending date.
     *
     * @param building  the host of the session
     * @param startDate the starting date to begin search
     * @param endDate   the ending date to end search
     */
    public GetRoomsRequest(String building, DateTime startDate, DateTime endDate) {
        if (building == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Building, starting Date, and ending Date cannot be null.");
        }
        this.building = building;
        this.startDate = startDate;
        this.endDate = endDate;
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
        json.put("building", building);
        json.put("startDate", startDate.getDateTimeString(DateTimeFormat.YYYY_MM_DD));
        json.put("endDate", endDate.getDateTimeString(DateTimeFormat.YYYY_MM_DD));

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
