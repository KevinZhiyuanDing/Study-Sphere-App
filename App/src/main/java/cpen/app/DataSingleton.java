package cpen.app;

import cpen.ADT.Room;
import cpen.ADT.Session;
import cpen.ADT.User;
import cpen.network.Network;

/**
 * The DataSingleton class manages a single instance of user session-related data.
 */
public class DataSingleton {
    private User user = null; // Is the user logged in
    private Session sessionCheckRequest;
    private Network connection; // Is the connection that is maintained for client connection
    private Session sessionHostRequest; // Is the session that the host checks in acceptRequest page
    private Room roomToHostSession; // Is the room that the user clicks to attempt to book a session for

    /**
     * Holds the single instance of the DataSingleton class, ensuring that only one instance
     * of this class is created throughout the application lifecycle.
     */
    private static DataSingleton instance = new DataSingleton();

    /**
     * Returns the single instance of the DataSingleton class.
     *
     * @return the unique instance of DataSingleton that is maintained throughout the application lifetime
     */
    public static DataSingleton getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the current session check request.
     *
     * @return the Session of the current session check request
     */
    public Session getSessionCheckRequest() {
        return sessionCheckRequest;
    }

    public void setSessionCheckRequest(Session sessionCheckRequest) {
        this.sessionCheckRequest = sessionCheckRequest;
    }

    /**
     * Retrieves the current network connection instance.
     *
     * @return the Network for the client-server connection
     */
    public Network getConnection() {
        return connection;
    }

    public boolean setConnection(Network connection) {
        this.connection = connection;
        this.connection.connect();
        return connection.isConnected();
    }

    /**
     * Retrieves the current session host request
     * The session is the one the host checks on the accept request page.
     *
     * @return the Session representing the current session host request
     */
    public Session getSessionHostRequest() {
        return sessionHostRequest;
    }

    /**
     * Sets the session that the host checks in the acceptRequest page.
     *
     * @param sessionHostRequest the Session the host checks in the acceptRequest page.
     */
    public void setSessionHostRequest(Session sessionHostRequest) {
        this.sessionHostRequest = sessionHostRequest;
    }

    /**
     * Retrieves the room selected by the user for attempting to book a session.
     *
     * @return the Room instance that the user has chosen to host a session
     */
    public Room getRoomToHostSession() {
        return roomToHostSession;
    }

    /**
     * Sets the room for the current user session where they attempt to host a session.
     *
     * @param roomToHostSession the Room instance selected by the user to host a session
     */
    public void setRoomToHostSession(Room roomToHostSession) {
        this.roomToHostSession = roomToHostSession;
    }
}
