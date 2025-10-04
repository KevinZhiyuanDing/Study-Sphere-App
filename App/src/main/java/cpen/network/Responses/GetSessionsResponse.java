package cpen.network.Responses;

import org.json.JSONArray;
import org.json.JSONObject;

import cpen.ADT.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a response that sends info to client, of all the available
 * study sessions between the starting date and ending date,
 * with filtering based on the course.
 * <p>
 * This class implements {@link Response} and provides methods
 * for decoding the response and retrieving the type of response.
 * <p>
 * The {@code GetSessionsResponse} ensures:
 * - Response is valid according to its type
 */
public class GetSessionsResponse implements Response {
    private final ResponseType responseType = ResponseType.GET_SESSIONS;
    private final List<Session> sessionsList = new ArrayList<>();

    // Representation Invariant (RI):
    // - str is not null

    // Abstraction Function (AF):
    // - Represents a response that sends info to client, of all the available
    //      study sessions between the starting date and ending date,
    //      with filtering based on the course.
    // - Response is decoded

    /**
     * Finds all the available study sessions between the starting date and ending date,
     * with filtering based on the course.
     *
     * @param str the encoded response
     */
    public GetSessionsResponse(String str) {
        System.out.println(str);
        JSONArray sessions = new JSONArray(str);

        for (int i = 0; i < sessions.length(); i++) {
            JSONObject sessionMap = sessions.getJSONObject(i);

            String startDate = sessionMap.getString("startTime");
            String endDate = sessionMap.getString("endTime");

            DateTime startDateObj = parseDateTime(startDate);
            DateTime endDateObj = parseDateTime(endDate);

            User host = new User(sessionMap.getString("hostUser"), "");
            String course = sessionMap.getString("course");
            String room = sessionMap.getString("room");
            String building = sessionMap.getString("building");
            String desc = sessionMap.getString("desc");

            sessionsList.add(new Session(host, building, course, room, startDateObj, endDateObj, desc));
        }
    }

    private DateTime parseDateTime(String dateTimeStr) {
        // Expected format: "YYYY-MM-DDHH:00"
        String date = dateTimeStr.substring(0, 10); // "YYYY-MM-DD"
        String hour = dateTimeStr.substring(10, 12); // "HH"

        // Split the date part into year, month, and day
        String[] dateParts = date.split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        int hourInt = Integer.parseInt(hour); // hour part

        return new DateTime(year, month, day, hourInt);
    }

    /**
     * Retrieves list of all the sessions between the starting date and ending date.
     *
     * @return list of sessions between the starting date and ending date
     */
    public List<Session> getSessionsList() {
        return new ArrayList<>(sessionsList);
    }

    /**
     * Extract the response's type
     *
     * @return the type of the response
     */
    @Override
    public ResponseType getThisResponseType() {
        return responseType;
    }
}