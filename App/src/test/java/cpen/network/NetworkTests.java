package cpen.network;

import cpen.network.Requests.Request;
import cpen.network.Requests.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkTests {
    @Test
    public void testConnectServer() {
        Network connection = new Network("127.0.0.1");
        assertTrue(connection.connect());
        assertTrue(connection.isConnected());
        assertTrue(connection.disconnect());
    }

    @Test
    public void testAbruptDisconnect() {
        Network connection = new Network("127.0.0.1");
        assertFalse(connection.isConnected());
        assertTrue(connection.connect());
        assertTrue(connection.isConnected());
    }

    @Test
    public void testRequest() {
        Network connection = new Network("127.0.0.1");
        assertTrue(connection.connect());
        assertTrue(connection.isConnected());
        assertTrue(connection.disconnect());
    }

    @Test
    public void testResponse() {
        Network connection = new Network("127.0.0.1");
        assertTrue(connection.connect());
        assertTrue(connection.isConnected());
        assertTrue(connection.disconnect());
    }
}
