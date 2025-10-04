package cpen.network.Responses;

import org.json.JSONObject;

import cpen.ADT.DateTime;
import cpen.ADT.Room;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a response that sends info to client of all the available
 * empty rooms between the starting date and ending date.
 * <p>
 * This class implements {@link Response} and provides methods
 * for decoding the response and retrieving the type of response.
 * <p>
 * The {@code GetRoomsResponse} ensures:
 * - Response is valid according to its type
 */
public class GetRoomsResponse implements Response {
    private final ResponseType responseType = ResponseType.GET_ROOMS;
    private List<Room> emptyRooms = new ArrayList<>();
    private List<String> urls = new ArrayList<>();

    // Representation Invariant (RI):
    // - str is not null

    // Abstraction Function (AF):
    // - Represents a response that sends info to client of all the available
    //      empty rooms between the starting date and ending date.
    // - Response is decoded

    /**
     * Finds all the available empty rooms between the starting date and ending date.
     *
     * @param str the encoded response
     */
    public GetRoomsResponse(String str) {
        JSONArray emptyRoomsList = new JSONArray(str);

        for (int i = 0; i < emptyRoomsList.length(); i++) {
            JSONObject roomMap = emptyRoomsList.getJSONObject(i);

            String date = roomMap.getString("date");
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            int day = Integer.parseInt(date.substring(8, 10));

            int startTime = Integer.parseInt(roomMap.getString("start_time").substring(0, 2));
            int endTime = Integer.parseInt(roomMap.getString("end_time").substring(0, 2));

            DateTime startTimeObj = new DateTime(year, month, day, startTime);
            DateTime endTimeObj = new DateTime(year, month, day, endTime);

            emptyRooms.add(new Room(roomMap.getString("group"), roomMap.getString("room_num"),
                            startTimeObj, endTimeObj, roomMap.getString("url")));
        }
    }

    /**
     * Retrieves list of all the available empty rooms between the starting date and ending date.
     *
     * @return list of available empty rooms
     */
    public List<Room> getEmptyRooms() {
        return new ArrayList<>(emptyRooms);
    }

    /**
     * Retrieves list of all the available empty rooms urls between the starting date and ending date.
     *
     * @return list of urls of available empty rooms
     */
    public List<String> getEmptyRoomUrls() {
        return new ArrayList<>(urls);
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