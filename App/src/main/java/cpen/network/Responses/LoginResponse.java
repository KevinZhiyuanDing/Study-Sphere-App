package cpen.network.Responses;

import org.json.JSONObject;

import cpen.ADT.User;

/**
 * Represents a response that sends info to client on whether user has been logged in.
 * <p>
 * This class implements {@link Response} and provides methods
 * for decoding the response and retrieving the type of response.
 * <p>
 * The {@code LoginResponse} ensures:
 * - Response is valid according to its type
 */
public class LoginResponse implements Response {
    private final ResponseType responseType = ResponseType.LOGIN;
    private boolean validUser;
    private User user;

    // Representation Invariant (RI):
    // - str is not null

    // Abstraction Function (AF):
    // - Represents a response that sends info to client on whether user has been logged in.
    // - Response is decoded

    /**
     * Finds on whether user has been logged in.
     *
     * @param str the encoded response
     */
    public LoginResponse(String str) {
        JSONObject login = new JSONObject(str);

        this.validUser = login.getBoolean("valid");
        this.user = new User(login.getString("userUsername"), "");
    }

    /**
     * Retrieves whether user is in the database.
     *
     * @return true if user is valid, false otherwise
     */
    public boolean isValidUser() {
        return validUser;
    }

    /**
     * Retrieves the user that is trying to login (current user).
     *
     * @return user
     */
    public User getUser() {
        return user;
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