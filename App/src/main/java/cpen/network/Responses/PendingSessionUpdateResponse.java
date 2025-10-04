// THIS WILL BE IMPLEMENTED IF WE HAVE TIME

package cpen.network.Responses;
import java.util.ArrayList;

import cpen.ADT.Session;
import cpen.ADT.User;

// we might not need this. I could just get the user everytime, user has ingo of created and pending sessions
public class PendingSessionUpdateResponse implements Response {
    private User user;
    private ArrayList<Session> createdSession; // session in question
    private ArrayList<Session> pendingSession;

    public PendingSessionUpdateResponse(String str) {
        // this.user =
        // this.createdSession =
        // this.createSessionSuccess =
    }

    /**
     * Extract the response's type
     *
     * @return the type of the response
     */
    @Override
    public ResponseType getThisResponseType() {
        return null;
    }
}