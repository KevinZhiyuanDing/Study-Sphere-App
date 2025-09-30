import asyncio
import unittest
from unittest.mock import AsyncMock, MagicMock
from app.network.network import Network


class TestNetworkBasic(unittest.IsolatedAsyncioTestCase):
    async def asyncSetUp(self):
        # Mock output functions
        self.outputServer = MagicMock()
        self.outputClient = MagicMock()
        self.outputError = MagicMock()

        # Mock request handlers
        self.getRoomFunc = AsyncMock(return_value=MagicMock(string=MagicMock(return_value="RoomResponse")))
        self.createSessionFunc = AsyncMock()
        self.getSessionFunc = AsyncMock(return_value=MagicMock(string=MagicMock(return_value="SessionResponse")))
        self.acceptSessionFunc = AsyncMock(return_value=MagicMock(string=MagicMock(return_value="AcceptResponse")))
        self.loginFunc = AsyncMock(return_value=MagicMock(string=MagicMock(return_value="LoginResponse")))
        self.createUserFunc = AsyncMock(return_value=MagicMock(string=MagicMock(return_value="CreateUserResponse")))
        self.joinSessionFunc = AsyncMock(return_value=MagicMock(string=MagicMock(return_value="JoinSessionResponse")))

        # Initialize the Network class
        self.network = Network(
            getRoomFunc=self.getRoomFunc,
            createSessionFunc=self.createSessionFunc,
            getSessionFunc=self.getSessionFunc,
            acceptSessionFunc=self.acceptSessionFunc,
            loginFunc=self.loginFunc,
            createUserFunc=self.createUserFunc,
            joinSessionFunc=self.joinSessionFunc,
            outputServer=self.outputServer,
            outputClient=self.outputClient,
            outputError=self.outputError,
        )
        self.stopEvent = asyncio.Event()

        # Start the server
        self.server_task = asyncio.create_task(self.network.connect(self.stopEvent))

    async def asyncTearDown(self):
        # Stop the server
        self.stopEvent.set()
        await self.server_task

    async def test_client_connection(self):
        # Simulate a client connecting to the server
        reader, writer = await asyncio.open_connection("localhost", 65432)
        self.assertTrue(writer.get_extra_info("peername") is not None)

        writer.close()
        await writer.wait_closed()

    async def test_simple_invalid_request(self):
        # Simulate an invalid request
        reader, writer = await asyncio.open_connection("localhost", 65432)
        writer.write('{"requestType": "INVALID"}\n'.encode("utf-8"))
        await writer.drain()

        # Invalid request should not send a response
        response = await reader.read(1024)
        self.assertEqual(response, b"")

        writer.close()
        await writer.wait_closed()

