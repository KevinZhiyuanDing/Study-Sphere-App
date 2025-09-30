package cpen.network.Responses;

import org.json.JSONObject;

/**
 * Represents a response that sends info to client on whether host accepts or rejects
 * a requester's request to join study session.
 * <p>
 * This class implements {@link Response} and provides methods
 * for decoding the response and retrieving the type of response.
 * <p>
 * The {@code AcceptSessionResponse} ensures:
 * - Response is valid according to its type
 */
public class AcceptSessionResponse implements Response {
    private final ResponseType responseType = ResponseType.ACCEPT_SESSION;
    private boolean sessionAccepted;

    // Representation Invariant (RI):
    // - str is not null

    // Abstraction Function (AF):
    // - Represents a response that sends info to client on whether host accepts or rejects
    //      a requester's request to join study session.
    // - Response is decoded

    /**
     * Finds whether the host accepts/rejects a requester's request
     * to join study session from an encoded message.
     *
     * @param str the encoded response
     */
    public AcceptSessionResponse(String str) {
        JSONObject json = new JSONObject(str);

        this.sessionAccepted = json.getBoolean("accepted");
    }

    /**
     * Retrieves whether host accepts/rejects requester's request to host's study session.
     *
     * @return true if host accepts, false otherwise
     */
    public boolean isSessionAccepted() {
        return sessionAccepted;
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
