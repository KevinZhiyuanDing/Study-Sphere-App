import asyncio

from .request import *


class Network:
    def __init__(self, getRoomFunc, createSessionFunc, getSessionFunc, acceptSessionFunc, loginFunc, createUserFunc, joinSessionFunc,
                 outputServer, outputClient, outputError):
        self.server = None
        self.getRoomFunc = getRoomFunc
        self.createSessionFunc = createSessionFunc
        self.getSessionFunc = getSessionFunc
        self.acceptSessionFunc = acceptSessionFunc
        self.loginFunc = loginFunc
        self.createUserFunc = createUserFunc
        self.joinSessionFunc = joinSessionFunc
        self.clientAddresses = []
        self.serverAddress = ""
        self.outputServer = outputServer
        self.outputClient = outputClient
        self.outputError = outputError
        self.activeTasks = []

    def getAddress(self):
        return self.serverAddress

    def getClient(self):
        return list(self.clientAddresses)

    async def newClient(self, reader: asyncio.StreamReader, writer: asyncio.StreamWriter):
        task = asyncio.current_task()
        self.activeTasks.append(task)

        clientAddress = writer.get_extra_info('peername')
        self.clientAddresses.append(clientAddress)
        self.outputClient(f"[{clientAddress}] New client")

        try:
            while True:
                data = await reader.read(1024)

                if not data:
                    break

                message = data.decode('utf-8')
                res = None
                if getRequestType(message) == RequestType.GET_ROOMS:
                    res = self.getRoomFunc(GetRoomsRequest(message))
                elif getRequestType(message) == RequestType.CREATE_SESSION:
                    self.createSessionFunc(CreateSessionRequest(message))
                elif getRequestType(message) == RequestType.GET_SESSIONS:
                    res = self.getSessionFunc(GetSessionsRequest(message))
                elif getRequestType(message) == RequestType.ACCEPT_REQUEST:
                    res = self.acceptSessionFunc(AcceptSessionRequest(message))
                elif getRequestType(message) == RequestType.LOGIN:
                    res = self.loginFunc(LoginRequest(message))
                elif getRequestType(message) == RequestType.CREATE_USER:
                    res = self.createUserFunc(CreateNewUserRequest(message))
                elif getRequestType(message) == RequestType.JOIN_SESSION:
                    res = self.joinSessionFunc(JoinSessionRequest(message))
                else:
                    self.outputError(f"[{clientAddress} Invalid request received from client]")

                if res is not None:
                    writer.write((res.string() + "\n").encode('utf-8'))
                    await writer.drain()
        except asyncio.IncompleteReadError:
            self.outputError(f"[{clientAddress}] Client disconnected abruptly, incomplete data received")
        except ConnectionResetError:
            self.outputError(f"[{clientAddress}] Client disconnected abruptly")
        finally:
            try:
                writer.close()

                self.outputClient(f"[{clientAddress}] Client disconnected")
                try:
                    await writer.wait_closed()
                except ConnectionResetError:
                    self.outputError(f"[{clientAddress}] Unable to disconnect cleanly, removing client")
                except Exception as e:
                    self.outputError(f"[{clientAddress}] Unexpected exception with client, removing client")
                self.clientAddresses.remove(clientAddress)
            except RuntimeError:
                pass



    def disconnect(self):
        self.server.close()

    async def connect(self, stopEvent, host='localhost', port=65432):
        self.server = await asyncio.start_server(self.newClient, host, port)
        self.serverAddress = self.server.sockets[0].getsockname()
        self.outputServer(f"Server open, server ip: [{self.serverAddress}]")

        async with self.server:
            while not stopEvent.is_set():
                await asyncio.sleep(0.1)
