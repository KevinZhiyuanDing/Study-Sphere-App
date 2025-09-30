import asyncio

import response

from network import *


def getRooms(req):
    res = response.Response()
    return res


def createSessions(req):
    res = response.Response()
    return res


def main():
    network = Network(getRooms, createSessions)
    try:
        asyncio.run(network.connect())
    except KeyboardInterrupt:
        print("Server stopped")


if __name__ == '__main__':
    main()
