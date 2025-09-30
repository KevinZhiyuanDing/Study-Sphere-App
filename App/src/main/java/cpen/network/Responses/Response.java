package cpen.network.Responses;

/**
 * Represents a response from server to client.
 * <p>
 * Implementations of this interface are expected to maintain the following properties:
 * - Response Type is a valid type of response
 */
public interface Response {
    ResponseType responseType = null;

    /**
     * Extract the response's type
     *
     * @param str the encoded message to be decoded
     * @return the type of the response
     */
    static ResponseType getResponseType(String str) {
        return ResponseType.ACCEPT_SESSION;
    }

    /**
     * Extract the response's type
     *
     * @return the type of the response
     */
    ResponseType getThisResponseType();
}
