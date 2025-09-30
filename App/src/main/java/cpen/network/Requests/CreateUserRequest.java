package cpen.network.Requests;

import org.json.JSONObject;

import cpen.adt.User;

/**
 * Represents a request that sends info to server, to create a new user account.
 * <p>
 * This class implements {@link Request} and provides methods
 * for encoding the request and retrieving the type of request.
 * <p>
 * The {@code CreateUserRequest} ensures:
 * - Request is valid when being constructed
 */
// Request that new user be added to database
public class CreateUserRequest implements Request {
    RequestType requestType = RequestType.CREATE_USER;
    private final User user;

    // Representation Invariant (RI):
    // - user is not null

    // Abstraction Function (AF):
    // - Represents a request that sends info to create a new user account.
    // - Request can also be encoded

    /**
     * Constructs a request that sends info to server, to create a new user account.
     *
     * @param user the user being added to the database
     */
    public CreateUserRequest(User user) {
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
