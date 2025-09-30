package cpen.network;

import cpen.network.Requests.Request;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Represents a network that connects the client to a server.
 * <p>
 * The {@code Network} ensures:
 * - serverIP is valid
 */
public class Network {
    private String serverIP;
    private Socket client;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;
    private boolean connected;

    private int timeout = 5000;

    // Representation Invariant (RI):
    // - serverIP, client, outputStream, inputStream are not null

    // Abstraction Function (AF):
    // - Represents a network that connects the client to a server.
    // - Connected is true if client successfully connects to server.

    /**
     * Constructs a network, where client and the server are unconnected.
     */
    public Network() {
        connected = false;
    }

    /**
     * Constructs a network, where client and the specified server are unconnected.
     *
     * @param serverIP the server that client is trying to connect to
     */
    public Network(String serverIP) {
        connected = false;
        this.serverIP = serverIP;
    }

    /**
     * Set timeout to a specified time interval.
     *
     * @param timeout the time interval for timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Set serverIP to a specified server, and attempt to connect client to it.
     *
     * @param serverIP the server that client is trying to connect to
     */
    public void setServerIP(String serverIP) throws IOException {
        this.serverIP = serverIP;
        connect();
    }

    /**
     * Attempt to connect client to the server.
     *
     * @return true if connect successfully, false otherwise
     */
    public boolean connect() {
        try {
            client = new Socket("localhost", 65432);
            client.setSoTimeout(timeout);
            outputStream = new DataOutputStream(client.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            return false;
        }
        connected = true;
        return true;
    }

    /**
     * Check whether client is connected to the server.
     *
     * @return true if connected, false if not
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Disconnects client from the server.
     *
     * @return true if disconnected successfully, false otherwise
     */
    public boolean disconnect() {
        if (!connected) {
            return false;
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            return false;
        }
        connected = false;
        return true;
    }

    /**
     * Checks if response is available.
     *
     * @return true if ready, false otherwise
     */
    public boolean responseAvailable() {
        try {
            boolean rdy = inputStream.ready();
            return rdy;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Blocking code, run responseAvailable before running this method.
     *
     * @return an instance of Response that the server responded with,
     * if an exception occurs, null will be returned.
     */
    public String receiveResponse() {
        if (isConnected()) {
            try {
                String str = inputStream.readLine();
                System.out.println(str);
                return str;
            } catch (IOException e) {
                System.out.println(e);
                return null;
            }
        }
        return null;
    }

    /**
     * Send specified request to the server.
     *
     * @param req the request client is attempting to send to server
     * @return true if request is successfully sent, false otherwise
     */
    public boolean sendRequest(Request req) {
        System.out.println(req.string());
        if (isConnected()) {
            try {
                outputStream.write(req.string().getBytes(StandardCharsets.UTF_8));
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
}
