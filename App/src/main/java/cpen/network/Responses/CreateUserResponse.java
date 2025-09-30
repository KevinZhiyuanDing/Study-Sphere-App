package cpen.network.Responses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a response that sends info to client on whether new user account has been created.
 * <p>
 * This class implements {@link Response} and provides methods
 * for decoding the response and retrieving the type of response.
 * <p>
 * The {@code CreateUserResponse} ensures:
 * - Response is valid according to its type
 */
public class CreateUserResponse implements Response {
    private final ResponseType responseType = ResponseType.CREATE_USER;
    private final boolean userAccepted;

    // Representation Invariant (RI):
    // - str is not null

    // Abstraction Function (AF):
    // - Represents a response that sends info to client on whether new user account has been created.
    // - Response is decoded

    /**
     * Finds whether the new user account has been created.
     *
     * @param str the encoded response
     */
    public CreateUserResponse (String str) {
        if (isValidJsonString(str)) {
            JSONObject parsed = new JSONObject(str);
            this.userAccepted = parsed.getBoolean("accepted");
        } else {
            throw new IllegalArgumentException("Invalid input string: invalid format.");
        }
    }

    private boolean isValidJsonString(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);

            return json.has("accepted");

        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * Retrieves whether the new user account has been created.
     *
     * @return true if user account is create, false otherwise
     */
    public boolean isUserAccepted() {
        return userAccepted;
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
