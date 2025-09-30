package cpen.network.Requests;

/**
 * Represents a request from client to server.
 * <p>
 * Implementations of this interface are expected to maintain the following properties:
 * - The request can be identified by its string value
 * - Request Type is a valid type of request
 */
public interface Request {
    RequestType requestType = null;

    /**
     * Encodes all the necessary information to make a request
     *
     * @return the encoded version of the request information
     */
    String string();

    /**
     * Extract the request's type
     *
     * @return the type of the request
     */
    RequestType getRequestType();
}
