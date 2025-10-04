package cpen.network.Requests;

import org.json.JSONObject;

import cpen.ADT.User;

/**
 * Represents a request that sends info to server, to login a user if user is in the database.
 * <p>
 * This class implements {@link Request} and provides methods
 * for encoding the request and retrieving the type of request.
 * <p>
 * The {@code LoginRequest} ensures:
 * - Request is valid when being constructed
 */
public class LoginRequest implements Request {
    RequestType requestType = RequestType.USER_LOGIN;
    private final User user;

    // Representation Invariant (RI):
    // - user is not null

    // Abstraction Function (AF):
    // - Represents a request that sends info, to login a user if user is in the database.
    // - Request can also be encoded

    /**
     * Constructs a request that sends info to server, to login a user if user is in the database.
     *
     * @param user the user trying to login
     */
    public LoginRequest(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        this.user = user;
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

        json.put("username", user.getUsername());
        json.put("password", user.getPassword());

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